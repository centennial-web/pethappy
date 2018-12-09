package ca.pethappy.pethappy.android.ui.subscriptions;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.models.backend.projections.OrderForListing;
import ca.pethappy.pethappy.android.models.backend.projections.SubscriptionForDetails;
import ca.pethappy.pethappy.android.ui.base.BaseAuthenticatedActivity;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;

public class SubscriptionDetailsActivity extends BaseAuthenticatedActivity {
    public static final String EXTRA_SUBSCRIPTION_ID = "subscriptionId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_details);

        // Toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Subscription Details");
        }

        // Subscription id
        long subscriptionId = getIntent().getLongExtra(EXTRA_SUBSCRIPTION_ID, 0L);

        App app = getApp();
        new SimpleTask<Void, Payload>(
                ignored -> {
                    SubscriptionForDetails subscription = app.subscriptionService.subscription(subscriptionId);
                    List<OrderForListing> orders = app.ordersService.ordersForListings(subscriptionId);
                    return new Payload(subscription, orders);
                },
                paylod -> {
                    Toast.makeText(getApplicationContext(), "Subscription: " + paylod.subscription.id, Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ).execute((Void)null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class Payload {
        SubscriptionForDetails subscription;
        List<OrderForListing> orders;

        public Payload(SubscriptionForDetails subscription, List<OrderForListing> orders) {
            this.subscription = subscription;
            this.orders = orders;
        }
    }
}
