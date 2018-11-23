package ca.pethappy.pethappy.android.ui.products;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.consts.Consts;
import ca.pethappy.pethappy.android.models.backend.Product;
import ca.pethappy.pethappy.android.utils.formatters.NumberFormatter;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final List<Product> products;
    private final ProductAdapterEventsListener productAdapterEventsListener;

    public ProductAdapter(ProductAdapterEventsListener productAdapterEventsListener) {
        this.products = new ArrayList<>();
        this.productAdapterEventsListener = productAdapterEventsListener;
    }

    public void updateData(List<Product> products) {
        final ProductDiffCallback diffCallback = new ProductDiffCallback(this.products, products);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.products.clear();
        this.products.addAll(products);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Get inflater
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        // Inflate the layout
        View productView = inflater.inflate(R.layout.fragment_product_recyclerview_item, viewGroup, false);

        // Return a new holder instance
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        // Get product
        final Product product = products.get(index);

        // Fill the views
        viewHolder.nameTxt.setText(product.name);
        viewHolder.manufacturerTxt.setText("by " + product.manufacturer.name);
        viewHolder.categoryTxt.setText(product.category.name);
        viewHolder.ingredientTxt.setText("Main ingredient is " + product.ingredient.name);
        viewHolder.weightKgTxt.setText(NumberFormatter.getInstance().formatNumber2(product.weightKg) + " Kg");
        viewHolder.priceTxt.setText("CDN$ " + NumberFormatter.getInstance().formatNumber2(product.price));

        // Photo
        Picasso.get()
                .load(Consts.AWS_S3_URL + "/" + product.imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.pictureImv);

        // Product click
        viewHolder.productCardview.setOnClickListener(v -> {
            if (productAdapterEventsListener != null) {
                productAdapterEventsListener.onItemClick(product, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView manufacturerTxt;
        TextView categoryTxt;
        TextView ingredientTxt;
        TextView weightKgTxt;
        TextView priceTxt;
        ImageView pictureImv;
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
        }
    }

    public interface ProductAdapterEventsListener {
        void onItemClick(Product product, View view);
    }
}
