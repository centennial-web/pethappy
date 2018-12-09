package ca.pethappy.pethappy.android.ui.subscriptions;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.models.backend.projections.SubscriptionForListing;
import ca.pethappy.pethappy.android.ui.base.fragments.BaseFragment;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;

public class SubscriptionsFragment extends BaseFragment {
    private SubscriptionAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    public SubscriptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subscriptions, container, false);

        // Swipe to refresh
        swipeContainer = rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this::query);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // AdaptergetPackageName
        adapter = new SubscriptionAdapter(getApp(), (subscription, cardView) -> {
//                Intent Intent = new Intent(getContext(), ProductDetailsActivity.class);
//                Intent.putExtra(ProductDetailsActivity.EXTRA_PRODUCT_ID, subscription.id);
//                startActivity(Intent);
        });

        // Recycler view
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Query subscriptions
        query();

        return rootView;
    }

    private void query() {
        final App app = getApp();
        new SimpleTask<Void, List<SubscriptionForListing>>(
                none -> {
                    List<SubscriptionForListing> list = app.subscriptionService.subscriptions();
                    return list;
                },
                payload -> {
                    adapter.updateData(payload);
                    swipeContainer.setRefreshing(false);
                },
                error -> {
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        ).execute((Void) null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
