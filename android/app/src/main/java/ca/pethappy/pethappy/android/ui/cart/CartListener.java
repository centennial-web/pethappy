package ca.pethappy.pethappy.android.ui.cart;

import java.util.List;

import ca.pethappy.pethappy.android.models.backend.CartItem;

public interface CartListener {
    void addItemToCart(long productId, List<CartItem> cartItems);
    void removeItemFromCart(long productId, List<CartItem> cartItems);
    void refreshCart();
}
