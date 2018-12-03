package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionsRepository extends JpaRepository<Subscription, Long> {
}
