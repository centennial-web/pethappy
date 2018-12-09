package ca.pethappy.pethappy.android.api;

import java.util.List;

import ca.pethappy.pethappy.android.api.page.Page;
import ca.pethappy.pethappy.android.models.ApiUser;
import ca.pethappy.pethappy.android.models.User;
import ca.pethappy.pethappy.android.models.backend.Card;
import ca.pethappy.pethappy.android.models.backend.CartItem;
import ca.pethappy.pethappy.android.models.backend.Product;
import ca.pethappy.pethappy.android.models.backend.Subscription;
import ca.pethappy.pethappy.android.models.backend.projections.OrderForListing;
import ca.pethappy.pethappy.android.models.backend.projections.ProductWithoutDescription;
import ca.pethappy.pethappy.android.models.backend.projections.SubscriptionForDetails;
import ca.pethappy.pethappy.android.models.backend.projections.SubscriptionForListing;
import ca.pethappy.pethappy.android.models.forms.AddCartItem;
import ca.pethappy.pethappy.android.models.forms.SubscriptionForm;
import ca.pethappy.pethappy.android.models.forms.UserRegistration;
import ca.pethappy.pethappy.android.models.forms.UserSettings;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Endpoints {

    @GET("/api/user")
    Call<ApiUser> apiUser();

    @POST("/api/register")
    Call<User> registerUser(@Body UserRegistration userRegistration);

    @GET("/api/products/findAll")
    Call<Page<Product>> productsFindAll();

    @GET("/api/products/findAllWithoutDescription")
    Call<Page<ProductWithoutDescription>> productsFindAllWithoutDescription();

    @GET("/api/products/findById/{id}")
    Call<Product> productsFindById(@Path("id") Long id);

    @GET("/api/carts/itemCount/{deviceId}")
    Call<Integer> cartItemsCount(@Path("deviceId") String deviceId);

    @POST("/api/carts/addItem")
    Call<Boolean> cartAddItem(@Body AddCartItem cartItem);

    @GET("/api/carts/items")
    Call<List<CartItem>> cartItems(@Query("deviceId") String deviceId, @Query("userId") Long userId);

    @DELETE("/api/carts/removeItem")
    Call<Boolean> cartDeleteItem(@Query("deviceId") String deviceId, @Query("userId") Long userId,
                                 @Query("productId") Long productId);

    @GET("/api/carts/itemQuantity")
    Call<Integer> cartItemQuantity(@Query("deviceId") String deviceId, @Query("userId") Long userId);

    @GET("/api/settings/{userId}")
    Call<UserSettings> userSettings(@Path("userId") Long userId);

    @PUT("/api/settings")
    Call<Boolean> updateSettings(@Body UserSettings userSettings);

    @GET("/api/users/{userId}")
    Call<User> getUser(@Path("userId") Long userId);

    @GET("/api/users/cards/{userId}")
    Call<List<Card>> getUserCards(@Path("userId") Long userId);

    @POST("/api/subscriptions/new")
    Call<Boolean> newSubscription(@Body SubscriptionForm subscriptionForm);

    @GET("/api/subscriptions/listing/{userId}")
    Call<List<SubscriptionForListing>> subscriptions(@Path("userId") Long userId);

    @GET("/api/subscriptions/details/{id}")
    Call<SubscriptionForDetails> subscription(@Path("id") Long id);

    @GET("/api/orders/listing/{subscriptionId}")
    Call<List<OrderForListing>> ordersForListings(@Path("subscriptionId") Long subscriptionId);
}
