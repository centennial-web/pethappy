package ca.pethappy.pethappy.android.services;

import java.io.IOException;
import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.models.backend.projections.SubscriptionForDetails;
import ca.pethappy.pethappy.android.models.backend.projections.SubscriptionForListing;
import ca.pethappy.pethappy.android.models.forms.SubscriptionForm;
import retrofit2.Response;

public class SubscriptionService {
    private final App app;

    public SubscriptionService(App app) {
        this.app = app;
    }

    public boolean newSubscription(SubscriptionForm subscriptionForm) throws IOException {
        Response<Boolean> response = app.endpoints.newSubscription(subscriptionForm).execute();
        Boolean ok;
        if (response.isSuccessful() && (ok = response.body()) != null) {
            return ok;
        }
        throw new IOException("Couldn't create subscription");
    }

    public List<SubscriptionForListing> subscriptions() throws IOException {
        Response<List<SubscriptionForListing>> response = app.endpoints.subscriptions(app.getUserInfo().id).execute();
        List<SubscriptionForListing> payload;
        if (response.isSuccessful() && (payload = response.body()) != null) {
            return payload;
        }
        throw new IOException("Couldn't list subscriptions");
    }

    public SubscriptionForDetails subscription(Long id) throws IOException {
        Response<SubscriptionForDetails> response = app.endpoints.subscription(id).execute();
        SubscriptionForDetails payload;
        if (response.isSuccessful() && (payload = response.body()) != null) {
            return payload;
        }
        throw new IOException("Couldn't find subscription");
    }
}
