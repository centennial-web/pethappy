package ca.pethappy.pethappy.android.services;

import java.io.IOException;
import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.models.backend.projections.OrderForListing;
import retrofit2.Response;

public class OrdersService {
    private final App app;

    public OrdersService(App app) {
        this.app = app;
    }

    public List<OrderForListing> ordersForListings(Long subscriptionId) throws IOException {
        Response<List<OrderForListing>> response = app.endpoints.ordersForListings(subscriptionId).execute();
        List<OrderForListing> body;
        if (response.isSuccessful() && (body = response.body()) != null) {
            return body;
        }
        throw new IOException("Couldn't list orders");
    }
}
