package ca.pethappy.server.services;

import ca.pethappy.server.forms.SubscriptionForm;
import ca.pethappy.server.forms.SubscriptionItemForm;
import ca.pethappy.server.models.*;
import ca.pethappy.server.repositories.SubscriptionsRepository;
import ca.pethappy.server.repositories.projections.SubscriptionForDetails;
import ca.pethappy.server.repositories.projections.SubscriptionForListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionsService {
    private final SubscriptionsRepository subscriptionsRepository;
    private final UsersService usersService;
    private final CardsService cardsService;
    private final ProductsService productsService;
    private final CartsService cartsService;

    @Autowired
    public SubscriptionsService(SubscriptionsRepository subscriptionsRepository,
                                UsersService usersService,
                                CardsService cardsService,
                                ProductsService productsService,
                                CartsService cartsService) {
        this.subscriptionsRepository = subscriptionsRepository;
        this.usersService = usersService;
        this.cardsService = cardsService;
        this.productsService = productsService;
        this.cartsService = cartsService;
    }

    @Transactional(readOnly = true)
    public SubscriptionForDetails findSubscriptionForDetailsById(Long id) {
        return subscriptionsRepository.findSubscriptionForDetailsById(id);
    }

    @Transactional(readOnly = true)
    public List<SubscriptionForListing> subscriptions(Long customerId) {
        return subscriptionsRepository.findAllByCustomerId(customerId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Subscription insert(SubscriptionForm subscriptionForm) {
        // Locate customer
        User customer = usersService.findById(subscriptionForm.getCustomerId());

        // Locate card
        Card card = cardsService.findById(subscriptionForm.getCardId());

        // Create a subscription
        Subscription subscription = new Subscription();
        subscription.setCreationDate(new Date());
        subscription.setCustomer(customer);
        subscription.setDeliveryEvery(subscriptionForm.getDeliveryEvery());
        subscription.setPreferredDay(subscriptionForm.getPreferredDay());
        subscription.setCard(card);
        subscription.setTotalBeforeTax(subscriptionForm.getTotalBeforeTax());
        subscription.setTaxesPercent(subscriptionForm.getTaxesPercent());
        subscription.setTaxesValue(subscriptionForm.getTaxesValue());
        subscription.setTotal(subscriptionForm.getTotal());

        // Create subscription items
        for (SubscriptionItemForm itemForm : subscriptionForm.getItems()) {
            // Locate product
            Product product = productsService.findById(itemForm.getProductId());

            SubscriptionItem item = new SubscriptionItem();
            item.setSubscription(subscription);
            item.setProduct(product);
            item.setPrice(itemForm.getPrice());
            item.setQuantity(itemForm.getQuantity());
            item.setTotal(itemForm.getTotal());
            subscription.getItems().add(item);
        }

        // Save
        subscriptionsRepository.save(subscription);

        // Delete cart items
        cartsService.deleteCartsByUserId(customer.getId());

        return subscription;
    }
}
