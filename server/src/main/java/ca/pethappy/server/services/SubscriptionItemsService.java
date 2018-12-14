package ca.pethappy.server.services;

import ca.pethappy.server.models.SubscriptionItem;
import ca.pethappy.server.repositories.SubscriptionItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubscriptionItemsService {
    private final SubscriptionItemsRepository subscriptionItemsRepository;

    @Autowired
    public SubscriptionItemsService(SubscriptionItemsRepository subscriptionItemsRepository) {
        this.subscriptionItemsRepository = subscriptionItemsRepository;
    }

    @Transactional(readOnly = true)
    public List<SubscriptionItem> findAllBySubscriptionId(Long subscriptionId) {
        return subscriptionItemsRepository.findAllBySubscriptionId(subscriptionId);
    }
}
