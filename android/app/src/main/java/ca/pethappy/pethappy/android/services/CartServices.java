package ca.pethappy.pethappy.android.services;

import java.io.IOException;
import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.models.backend.CartItem;
import ca.pethappy.pethappy.android.models.forms.AddCartItem;
import retrofit2.Response;

public class CartServices {
    private final App app;

    public CartServices(App app) {
        this.app = app;
    }

    public List<CartItem> listItems() throws IOException {
        Response<List<CartItem>> response = app.noSecEndpoints
                .cartItems(app.getDeviceId(), app.getUserInfo().id).execute();
        List<CartItem> body;
        if (response.isSuccessful() && (body = response.body()) != null) {
            return body;
        }
        throw new IOException("Couldn't list cart items");
    }

    public boolean addItemToCart(long productId) throws IOException {
        AddCartItem cartItem = new AddCartItem();
        cartItem.deviceId = app.getDeviceId();
        cartItem.productId = productId;
        cartItem.userId = app.getUserInfo().id;

        Response<Boolean> response = app.noSecEndpoints.cartAddItem(cartItem).execute();

        Boolean ok;
        if (response.isSuccessful() && (ok = response.body()) != null) {
            return ok;
        }
        throw new IOException("Couldn't add item to the cart");
    }

    public boolean removeItemFromCart(long productId) throws IOException {
        Response<Boolean> response = app.noSecEndpoints.cartDeleteItem(
                app.getDeviceId(),
                app.getUserInfo().id,
                productId).execute();

        Boolean ok;
        if (response.isSuccessful() && (ok = response.body()) != null) {
            return ok;
        }
        throw new IOException("Couldn't remove item to the cart");
    }

    public int cartItemQuantity() throws IOException {
        Response<Integer> response = app.noSecEndpoints
                .cartItemQuantity(app.getDeviceId(), app.getUserInfo().id).execute();
        Integer itemQuantity;
        if (response.isSuccessful() && (itemQuantity = response.body()) != null) {
            return itemQuantity;
        }
        throw new IOException("Couldn't get cart items quantity");
    }
}
