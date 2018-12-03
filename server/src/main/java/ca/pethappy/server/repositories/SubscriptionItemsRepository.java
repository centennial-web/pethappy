package ca.pethappy.server.repositories;

import ca.pethappy.server.models.SubscriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionItemsRepository extends JpaRepository<SubscriptionItem, Long> {
}
