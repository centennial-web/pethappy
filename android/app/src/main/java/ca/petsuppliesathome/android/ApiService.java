package ca.petsuppliesathome.android;

import java.util.List;

import ca.petsuppliesathome.android.models.Product;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/products")
    Call<List<Product>> getProducts();
}
