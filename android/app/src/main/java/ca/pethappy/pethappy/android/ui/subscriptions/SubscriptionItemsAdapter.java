package ca.pethappy.pethappy.android.ui.subscriptions;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.models.backend.SubscriptionItem;
import ca.pethappy.pethappy.android.utils.formatters.NumberFormatter;

public class SubscriptionItemsAdapter extends RecyclerView.Adapter<SubscriptionItemsAdapter.ViewHolder> {
    private final App app;
    private final List<SubscriptionItem> data;

    SubscriptionItemsAdapter(App app) {
        this.app = app;
        this.data = new ArrayList<>();
    }

    void updateData(List<SubscriptionItem> data) {
        final SubscriptionItemsDiffCallback diffCallback = new SubscriptionItemsDiffCallback(this.data, data);
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
        View view = inflater.inflate(R.layout.fragment_subscription_item, viewGroup, false);

        // Return a new holder instance
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        // Get subscription
        final SubscriptionItem item = data.get(index);

        // Fill the views
        viewHolder.productNameTxt.setText(item.product.name);
        viewHolder.productQtyTxt.setText("Qty: " + item.quantity);
        viewHolder.priceTxt.setText("Price: CDN$ " + NumberFormatter.getInstance().formatNumber2(item.price));
        viewHolder.totalTxt.setText("Total: CDN$ " + NumberFormatter.getInstance().formatNumber2(item.total));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTxt;
        TextView productQtyTxt;
        TextView priceTxt;
        TextView totalTxt;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            productNameTxt = itemView.findViewById(R.id.productNameTxt);
            productQtyTxt = itemView.findViewById(R.id.productQtyTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            totalTxt = itemView.findViewById(R.id.totalTxt);
        }
    }
}
