package ca.pethappy.server.services;

import ca.pethappy.server.repositories.OrdersRepository;
import ca.pethappy.server.repositories.projections.OrderForListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderForListing> findAllOrderForListingBySubscriptionId(Long subscriptionId) {
        return ordersRepository.findAllOrderForListingBySubscriptionId(subscriptionId);
    }
}
