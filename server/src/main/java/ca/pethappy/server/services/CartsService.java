package ca.pethappy.server.services;

import ca.pethappy.server.models.Cart;
import ca.pethappy.server.repositories.CartItemsRepository;
import ca.pethappy.server.repositories.CartsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CartsService {
    private final CartsRepository cartsRepository;
    private final CartItemsRepository cartItemsRepository;

    @Autowired
    public CartsService(CartsRepository cartsRepository, CartItemsRepository cartItemsRepository) {
        this.cartsRepository = cartsRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

    @Transactional(readOnly = true)
    public int getItemCount(UUID deviceId, Long userId) {
        Cart cart = cartsRepository.findByDeviceIdAndUserId(deviceId, userId);
        return cart.getItems().size();
    }

}
