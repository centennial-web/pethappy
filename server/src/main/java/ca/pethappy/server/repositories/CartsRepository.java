package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartsRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByDeviceIdAndUserId(String deviceId, Long userId);
}
