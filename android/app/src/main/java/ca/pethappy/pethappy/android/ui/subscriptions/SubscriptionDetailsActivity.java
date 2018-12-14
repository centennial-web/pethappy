package ca.pethappy.pethappy.android.ui.subscriptions;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.models.backend.SubscriptionItem;
import ca.pethappy.pethappy.android.models.backend.projections.OrderForListing;
import ca.pethappy.pethappy.android.models.backend.projections.SubscriptionForDetails;
import ca.pethappy.pethappy.android.ui.base.BaseAuthenticatedActivity;
import ca.pethappy.pethappy.android.utils.formatters.DateFormatter;
import ca.pethappy.pethappy.android.utils.formatters.NumberFormatter;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class SubscriptionDetailsActivity extends BaseAuthenticatedActivity {
    public static final String EXTRA_SUBSCRIPTION_ID = "subscriptionId";

    private RecyclerView recyclerView;
    private SubscriptionItemsAdapter adapter;

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

        // Recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        recyclerView.addItemDecoration(itemDecor);

        // Adapter
        adapter = new SubscriptionItemsAdapter(getApp());
        recyclerView.setAdapter(adapter);

        // Get data
        App app = getApp();
        new SimpleTask<Void, Payload>(
                ignored -> {
                    SubscriptionForDetails subscription = app.subscriptionService.subscription(subscriptionId);
                    List<OrderForListing> orders = app.ordersService.ordersForListings(subscriptionId);
                    List<SubscriptionItem> items = app.subscriptionService.subscriptionItems(subscriptionId);
                    return new Payload(subscription, orders, items);
                },
                paylod -> {
                    // Subscription
                    ((TextView) findViewById(R.id.everyTxt)).setText("Every "
                            + paylod.subscription.deliveryEvery + (paylod.subscription.deliveryEvery == 1 ? " month on day " : " months on day ")
                            + paylod.subscription.preferredDay);
                    ((TextView) findViewById(R.id.creationDateTxt)).setText("Subscribed on " + DateFormatter.getInstance().formatDateTimeMid(paylod.subscription.creationDate));
                    ((TextView) findViewById(R.id.totalBeforeTaxTxt)).setText("Total before taxes CDN$ " + NumberFormatter.getInstance().formatNumber2(paylod.subscription.totalBeforeTax));
                    ((TextView) findViewById(R.id.taxesPercentTxt)).setText("Taxes " + NumberFormatter.getInstance().formatPercentage(paylod.subscription.taxesPercent));
                    ((TextView) findViewById(R.id.taxesValueTxt)).setText(" / CDN$ " + NumberFormatter.getInstance().formatNumber2(paylod.subscription.taxesValue));
                    ((TextView) findViewById(R.id.totalTxt)).setText("Total CDN$ " + NumberFormatter.getInstance().formatNumber2(paylod.subscription.total));

                    // Items
                    adapter.updateData(paylod.items);
                },
                error -> {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ).execute((Void) null);
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
        List<SubscriptionItem> items;

        Payload(SubscriptionForDetails subscription, List<OrderForListing> orders, List<SubscriptionItem> items) {
            this.subscription = subscription;
            this.orders = orders;
            this.items = items;
        }
    }
}
