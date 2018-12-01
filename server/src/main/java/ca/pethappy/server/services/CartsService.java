package ca.pethappy.server.services;

import ca.pethappy.server.models.Cart;
import ca.pethappy.server.models.CartItem;
import ca.pethappy.server.models.Product;
import ca.pethappy.server.models.User;
import ca.pethappy.server.repositories.CartsRepository;
import ca.pethappy.server.repositories.ProductsRepository;
import ca.pethappy.server.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartsService {
    private final CartsRepository cartsRepository;
    private final UsersRepository usersRepository;
    private final ProductsRepository productsRepository;

    @Autowired
    public CartsService(CartsRepository cartsRepository, UsersRepository usersRepository,
                        ProductsRepository productsRepository) {
        this.cartsRepository = cartsRepository;
        this.usersRepository = usersRepository;
        this.productsRepository = productsRepository;
    }

    @SuppressWarnings("Duplicates")
    @Transactional(propagation = Propagation.REQUIRED)
    public int getItemCount(String deviceId, Long userId) throws IllegalArgumentException {
        Optional<Cart> cartOpt = cartsRepository.findByDeviceIdAndUserId(deviceId, userId);

        // Return the current cart items count
        if (cartOpt.isPresent()) {
            return cartOpt.get().getItems().size();
        }
        // Create new cart for the user
        else {
            User user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setDeviceId(deviceId);
            cartsRepository.save(newCart);
            return 0;
        }
    }

    @SuppressWarnings("Duplicates")
    @Transactional(propagation = Propagation.REQUIRED)
    public int getItemQuantity(String deviceId, Long userId) throws IllegalArgumentException {
        return getOrCreateCart(deviceId, userId).getItems().stream().mapToInt(CartItem::getQuantity).sum();
    }

    @SuppressWarnings("Duplicates")
    @Transactional(propagation = Propagation.REQUIRED)
    public List<CartItem> cartItems(String deviceId, Long userId) throws IllegalArgumentException {
        return getOrCreateCart(deviceId, userId).getItems();
    }

    @SuppressWarnings("Duplicates")
    @Transactional(propagation = Propagation.REQUIRED)
    public void addItem(String deviceId, Long userId, Long productId) throws IllegalArgumentException {
        Cart cart = getOrCreateCart(deviceId, userId);

        // Get product
        Product product = productsRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // No items
        if (cart.getItems().size() == 0) {
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
            List<CartItem> cartItems = cart.getItems()
                    .stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
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

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeItem(String deviceId, Long userId, Long productId) throws IllegalArgumentException {
        List<CartItem> cartItems = getOrCreateCart(deviceId, userId).getItems();

        // Update product quantity
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);

            // This is the cart item we want?
            if (item.getProduct().getId().equals(productId)) {
                // Remove the item if only one item in cart
                if (item.getQuantity() == 1) {
                    cartItems.remove(i);
                }
                // Just decrement quantity
                else {
                    item.setQuantity(item.getQuantity() - 1);
                }
                // We found the item we want to update/delete
                break;
            }
        }
    }

    private Cart getOrCreateCart(final String deviceId, final Long userId) throws IllegalArgumentException {
        return cartsRepository.findByDeviceIdAndUserId(deviceId, userId).orElseGet(() -> {
            // Get user
            User user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Create cart
            Cart newCart = new Cart();
            newCart.setDeviceId(deviceId);
            newCart.setUser(user);
            cartsRepository.save(newCart);

            return newCart;
        });
    }
}
