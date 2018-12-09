package ca.pethappy.pethappy.android.ui.subscriptions;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.models.backend.projections.SubscriptionForListing;
import ca.pethappy.pethappy.android.utils.formatters.DateFormatter;
import ca.pethappy.pethappy.android.utils.formatters.NumberFormatter;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {
    private final App app;
    private final List<SubscriptionForListing> data;
    private final SubscriptionAdapterEventsListener subscriptionAdapterEventsListener;

    SubscriptionAdapter(App app, SubscriptionAdapterEventsListener subscriptionAdapterEventsListener) {
        this.app = app;
        this.data = new ArrayList<>();
        this.subscriptionAdapterEventsListener = subscriptionAdapterEventsListener;
    }

    void updateData(List<SubscriptionForListing> data) {
        final SubscriptionDiffCallback diffCallback = new SubscriptionDiffCallback(this.data, data);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.data.clear();
        this.data.addAll(data);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Get inflater
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_subscription_recyclerview_item, viewGroup, false);

        // Return a new holder instance
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        // Get subscription
        final SubscriptionForListing subscription = data.get(index);

        // Fill the views
        viewHolder.deliveryEveryTxt.setText("Every " + subscription.deliveryEvery + (subscription.deliveryEvery == 1 ? " month" : " months"));
        viewHolder.preferredDayTxt.setText("on day " + subscription.preferredDay);
        viewHolder.creationDateTxt.setText("Subscribed on " + DateFormatter.getInstance().formatDateTime(subscription.creationDate));
        viewHolder.totalBeforeTaxTxt.setText("Total before taxes CDN$ " + NumberFormatter.getInstance().formatNumber2(subscription.totalBeforeTax));
        viewHolder.taxesPercentTxt.setText("Taxes " + NumberFormatter.getInstance().formatPercentage(subscription.taxesPercent));
        viewHolder.taxesValueTxt.setText(" / CDN$ " + NumberFormatter.getInstance().formatNumber2(subscription.taxesValue));
        viewHolder.totalTxt.setText("Total CDN$ " + NumberFormatter.getInstance().formatNumber2(subscription.total));

        if (subscription.cancelled) {
            viewHolder.cancelledLayout.setBackgroundColor(ContextCompat.getColor(app, R.color.cancelledColor));
        } else {
            viewHolder.cancelledLayout.setBackgroundColor(ContextCompat.getColor(app, R.color.accentColor));
        }

        // Subscription click
        viewHolder.cardview.setOnClickListener(cardView -> {
            if (subscriptionAdapterEventsListener != null) {
                subscriptionAdapterEventsListener.onItemClick(subscription, cardView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView deliveryEveryTxt;
        TextView preferredDayTxt;
        TextView creationDateTxt;
        TextView totalBeforeTaxTxt;
        TextView taxesPercentTxt;
        TextView taxesValueTxt;
        TextView totalTxt;
        CardView cardview;
        LinearLayout cancelledLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            deliveryEveryTxt = itemView.findViewById(R.id.deliveryEveryTxt);
            preferredDayTxt = itemView.findViewById(R.id.preferredDayTxt);
            creationDateTxt = itemView.findViewById(R.id.creationDateTxt);
            totalBeforeTaxTxt = itemView.findViewById(R.id.totalBeforeTaxTxt);
            taxesPercentTxt = itemView.findViewById(R.id.taxesPercentTxt);
            taxesValueTxt = itemView.findViewById(R.id.taxesValueTxt);
            totalTxt = itemView.findViewById(R.id.totalTxt);
            cardview = itemView.findViewById(R.id.cardview);
            cancelledLayout = itemView.findViewById(R.id.cancelledLayout);
        }
    }

    public interface SubscriptionAdapterEventsListener {
        void onItemClick(SubscriptionForListing subscription, final View cardView);
    }
}
