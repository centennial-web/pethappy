package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Order;
import ca.pethappy.server.repositories.projections.OrderForListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
    List<OrderForListing> findAllOrderForListingBySubscriptionId(Long subscriptionId);
}
