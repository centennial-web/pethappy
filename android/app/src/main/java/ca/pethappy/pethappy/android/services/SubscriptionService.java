package ca.pethappy.pethappy.android.services;

import java.io.IOException;

import ca.pethappy.pethappy.android.App;
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


}
