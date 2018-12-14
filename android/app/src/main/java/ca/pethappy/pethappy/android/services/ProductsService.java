package ca.pethappy.pethappy.android.services;

import java.io.IOException;
import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.models.backend.projections.ProductWithoutDescription;
import ca.pethappy.pethappy.android.models.backend.projections.Recommendation;
import retrofit2.Response;

public class ProductsService {
    private final App app;

    public ProductsService(App app) {
        this.app = app;
    }

    public List<ProductWithoutDescription> productsFindAllWithoutDescription(String query) throws IOException {
        Response<List<ProductWithoutDescription>> response = app.endpoints.productsFindAllWithoutDescription(query).execute();
        List<ProductWithoutDescription> payload;
        if (response.isSuccessful() && (payload = response.body()) != null) {
            return payload;
        }
        throw new IOException("Couldn't list products: " + response.errorBody().string());
    }

    public List<Recommendation> getRecommendations() throws IOException {
        Response<List<Recommendation>> response = app.endpoints.getRecommendations().execute();
        List<Recommendation> payload;
        if (response.isSuccessful() && (payload = response.body()) != null) {
            return payload;
        }
        throw new IOException("Couldn't list recommendations: " + response.errorBody().string());
    }
}
