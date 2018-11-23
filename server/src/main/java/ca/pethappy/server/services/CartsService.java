package ca.pethappy.server.services;

import ca.pethappy.server.models.Cart;
import ca.pethappy.server.models.CartItem;
import ca.pethappy.server.models.Product;
import ca.pethappy.server.models.User;
import ca.pethappy.server.repositories.CartItemsRepository;
import ca.pethappy.server.repositories.CartsRepository;
import ca.pethappy.server.repositories.ProductsRepository;
import ca.pethappy.server.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartsService {
    private final CartsRepository cartsRepository;
    private final CartItemsRepository cartItemsRepository;
    private final UsersRepository usersRepository;
    private final ProductsRepository productsRepository;

    @Autowired
    public CartsService(CartsRepository cartsRepository, CartItemsRepository cartItemsRepository,
                        UsersRepository usersRepository, ProductsRepository productsRepository) {
        this.cartsRepository = cartsRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.usersRepository = usersRepository;
        this.productsRepository = productsRepository;
    }

    @Transactional(readOnly = true)
    public int getItemCount(UUID deviceId, Long userId) {
        Cart cart = cartsRepository.findByDeviceIdAndUserId(deviceId, userId);
        return cart.getItems().size();
    }

    @SuppressWarnings("Duplicates")
    @Transactional(propagation = Propagation.REQUIRED)
    public void addItem(String deviceId, Long userId, Long productId) throws IllegalArgumentException {
        Cart cart = cartsRepository.findByDeviceIdAndUserId(UUID.fromString(deviceId), userId);

        // Create if not exists
        if (cart == null) {
            // Get user
            User user = usersRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Create cart
            cart = new Cart();
            cart.setDeviceId(UUID.fromString(deviceId));
            cart.setUser(user);
            cartsRepository.save(cart);
        }

        // Get product
        Product product = productsRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // No items
        if (cart.getItems().size() <= 0) {
            // Add item to the cart
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        }
        // We have one or more items
        else {
            // Query for the product by id
            List<CartItem> cartItems = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId))
                    .collect(Collectors.toList());

            // The product is in the cart
            if (cartItems.size() > 0) {
                CartItem item = cartItems.get(0);
                item.setQuantity(item.getQuantity() + 1);
            }
            // The product is NOT in the cart
            else {
                CartItem item = new CartItem();
                item.setProduct(product);
                item.setQuantity(1);
                item.setCart(cart);
                cart.getItems().add(item);
            }
        }
    }

}
