package ca.pethappy.server.repositories;

import ca.pethappy.server.models.SubscriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionItemsRepository extends JpaRepository<SubscriptionItem, Long> {
    List<SubscriptionItem> findAllBySubscriptionId(Long subscriptionId);
}
