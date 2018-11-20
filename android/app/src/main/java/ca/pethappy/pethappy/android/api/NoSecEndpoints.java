package ca.pethappy.pethappy.android.api;

import ca.pethappy.pethappy.android.api.page.Page;
import ca.pethappy.pethappy.android.models.User;
import ca.pethappy.pethappy.android.models.backend.Product;
import ca.pethappy.pethappy.android.models.forms.UserRegistration;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NoSecEndpoints {

    @POST("/api/register")
    Call<User> registerUser(@Body UserRegistration userRegistration);

    @GET("/api/products/findAll")
    Call<Page<Product>> productsFindAll();

}
