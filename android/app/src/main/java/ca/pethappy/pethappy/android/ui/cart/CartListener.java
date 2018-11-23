package ca.pethappy.pethappy.android.ui.cart;

import java.io.IOException;

public interface CartListener {
    boolean addItemToCart(long productId) throws IOException;
    void removeItemFromCart(long productId);
    int getItemCount() throws IOException;
}
