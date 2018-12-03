package ca.pethappy.pethappy.android.ui.cart;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.consts.Consts;
import ca.pethappy.pethappy.android.models.backend.CartItem;
import ca.pethappy.pethappy.android.utils.formatters.NumberFormatter;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {
    private final App app;
    private final List<CartItem> cartItems;
    private final CheckoutAdapterEventsListener checkoutAdapterEventsListener;

    CheckoutAdapter(App app, final List<CartItem> cartItems,
                    CheckoutAdapterEventsListener checkoutAdapterEventsListener) {
        this.app = app;
        this.cartItems = (cartItems == null) ? new ArrayList<>() : cartItems;
        this.checkoutAdapterEventsListener = checkoutAdapterEventsListener;
    }

    void updateData(final List<CartItem> cartItems) {
        final CartDiffCallback diffCallback = new CartDiffCallback(this.cartItems, cartItems);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.cartItems.clear();
        this.cartItems.addAll(cartItems);
        diffResult.dispatchUpdatesTo(this);
    }

    public void clear() {
        cartItems.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Get inflater
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        // Inflate the layout
        View productView = inflater.inflate(R.layout.activity_checkout_recyclerview_item, viewGroup, false);

        // Return a new holder instance
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        // Get product
        final CartItem cartItem = cartItems.get(index);

        // Fill the views
        viewHolder.nameTxt.setText(cartItem.product.name);
        viewHolder.priceTxt.setText("CDN$ " + NumberFormatter.getInstance().formatNumber2(cartItem.product.price));
        viewHolder.qtyTxt.setText("Qty: " + cartItem.quantity);
        viewHolder.totalTxt.setText("CDN$: " + NumberFormatter.getInstance().formatNumber2(cartItem.product.price.multiply(BigDecimal.valueOf(cartItem.quantity))));

        // Photo
        Picasso.get()
                .load(Consts.AWS_S3_URL + "/" + cartItem.product.imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.pictureImv);

        // Product click
        viewHolder.productView.setOnClickListener(v -> {
            if (checkoutAdapterEventsListener != null) {
                checkoutAdapterEventsListener.onItemClick(cartItem, viewHolder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView priceTxt;
        TextView qtyTxt;
        TextView totalTxt;
        ImageView pictureImv;
        ConstraintLayout productView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            qtyTxt = itemView.findViewById(R.id.qtyTxt);
            totalTxt = itemView.findViewById(R.id.totalTxt);
            pictureImv = itemView.findViewById(R.id.pictureImv);
            productView = itemView.findViewById(R.id.productView);
        }
    }

    public interface CheckoutAdapterEventsListener {
        void onItemClick(CartItem cartItem, final ViewHolder viewHolder);
    }
}
