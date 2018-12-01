package ca.pethappy.pethappy.android.ui.cart;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.consts.Consts;
import ca.pethappy.pethappy.android.models.backend.CartItem;
import ca.pethappy.pethappy.android.utils.formatters.NumberFormatter;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final App app;
    private final List<CartItem> cartItems;
    private final CartAdapterEventsListener cartAdapterEventsListener;

    CartAdapter(App app, CartAdapterEventsListener cartAdapterEventsListener) {
        this(app, null, cartAdapterEventsListener);
    }

    private CartAdapter(App app, final List<CartItem> cartItems,
                        CartAdapterEventsListener cartAdapterEventsListener) {
        this.app = app;
        this.cartItems = (cartItems == null) ? new ArrayList<>() : cartItems;
        this.cartAdapterEventsListener = cartAdapterEventsListener;
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
        View productView = inflater.inflate(R.layout.fragment_cart_recyclerview_item, viewGroup, false);

        // Return a new holder instance
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        // Get product
        final CartItem cartItem = cartItems.get(index);

        // Fill the views
        viewHolder.nameTxt.setText(cartItem.product.name);
        viewHolder.manufacturerTxt.setText("by " + cartItem.product.manufacturer.name);
        viewHolder.categoryTxt.setText(cartItem.product.category.name);
        viewHolder.ingredientTxt.setText("Main ingredient is " + cartItem.product.ingredient.name);
        viewHolder.weightKgTxt.setText(NumberFormatter.getInstance().formatNumber2(cartItem.product.weightKg) + " Kg");
        viewHolder.priceTxt.setText("CDN$ " + NumberFormatter.getInstance().formatNumber2(cartItem.product.price));
        viewHolder.quantityTxt.setText("Quantity: " + cartItem.quantity);

        // Photo
        Picasso.get()
                .load(Consts.AWS_S3_URL + "/" + cartItem.product.imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.pictureImv);

        // Product click
        viewHolder.productCardview.setOnClickListener(v -> {
            if (cartAdapterEventsListener != null) {
                cartAdapterEventsListener.onItemClick(cartItem, viewHolder);
            }
        });

        // Plus click
        viewHolder.plusBtn.setOnClickListener(v -> {
            if (cartAdapterEventsListener != null) {
                cartAdapterEventsListener.onPlusClick(cartItem, viewHolder);
            }
        });

        // Minus click
        viewHolder.minusBtn.setEnabled(cartItem.quantity > 0);
        viewHolder.minusBtn.setOnClickListener(v -> {
            if (cartAdapterEventsListener != null) {
                cartAdapterEventsListener.onMinusClick(cartItem, viewHolder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView manufacturerTxt;
        TextView categoryTxt;
        TextView ingredientTxt;
        TextView weightKgTxt;
        TextView priceTxt;
        ImageView pictureImv;
        TextView quantityTxt;
        Button plusBtn;
        Button minusBtn;
        CardView productCardview;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            manufacturerTxt = itemView.findViewById(R.id.manufacturerTxt);
            categoryTxt = itemView.findViewById(R.id.categoryTxt);
            ingredientTxt = itemView.findViewById(R.id.ingredientTxt);
            weightKgTxt = itemView.findViewById(R.id.weightKgTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            pictureImv = itemView.findViewById(R.id.pictureImv);
            productCardview = itemView.findViewById(R.id.productCardview);
            quantityTxt = itemView.findViewById(R.id.quantityTxt);
            plusBtn = itemView.findViewById(R.id.plusBtn);
            minusBtn = itemView.findViewById(R.id.minusBtn);
        }
    }

    public interface CartAdapterEventsListener {
        void onItemClick(CartItem cartItem, final ViewHolder viewHolder);

        void onPlusClick(CartItem cartItem, final ViewHolder viewHolder);

        void onMinusClick(CartItem cartItem, final ViewHolder viewHolder);
    }
}
