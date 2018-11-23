package ca.pethappy.pethappy.android.services;

import java.io.IOException;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.models.forms.AddCartItem;
import retrofit2.Response;

public class CartServices {
    private final App app;

    public CartServices(App app) {
        this.app = app;
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

    public void removeItemFromCart(long productId) throws IOException {

    }

    public int getItemCount() throws IOException {
        Response<Integer> response = app.noSecEndpoints.cartItemsCount(app.getDeviceId()).execute();
        Integer itemsCount;
        if (response.isSuccessful() && (itemsCount = response.body()) != null) {
            return itemsCount;
        }
        throw new IOException("Couldn't get cart items count");

    }
}
