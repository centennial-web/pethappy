package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Subscription;
import ca.pethappy.server.repositories.projections.SubscriptionForListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionsRepository extends JpaRepository<Subscription, Long> {
    List<SubscriptionForListing> findAllByCustomerId(Long customerId);
}
