package ca.petsuppliesathome.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ca.petsuppliesathome.android.models.Product;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    private List<Product> products;
    private Context context;

    ProductsAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        if (!TextUtils.isEmpty(product.getImageUrl())) {
            Picasso.with(context).load(Consts.URL_BASE + "/imgs/" + product.getImageUrl())
                    .error(R.drawable.ic_pets_black_24dp)
                    .placeholder(R.drawable.ic_pets_black_24dp)
                    .into(holder.imageView);
        }

        holder.nameTextView.setText(product.getName());
        holder.descriptionTextView.setText(product.getDescription());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView descriptionTextView;

        ProductViewHolder(View itemView) {
            super(itemView);

            this.imageView = itemView.findViewById(R.id.imageView);
            this.nameTextView = itemView.findViewById(R.id.nameTextView);
            this.descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
