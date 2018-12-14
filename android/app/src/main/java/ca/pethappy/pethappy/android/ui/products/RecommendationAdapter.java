package ca.pethappy.pethappy.android.ui.products;

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

import java.util.ArrayList;
import java.util.List;

import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.consts.Consts;
import ca.pethappy.pethappy.android.models.backend.projections.Recommendation;
import ca.pethappy.pethappy.android.utils.formatters.NumberFormatter;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {
    private final List<Recommendation> data;
    private final RecommendationAdapterEventsListener recommendationAdapterEventsListener;

    RecommendationAdapter(RecommendationAdapterEventsListener recommendationAdapterEventsListener) {
        this.data = new ArrayList<>();
        this.recommendationAdapterEventsListener = recommendationAdapterEventsListener;
    }

    public void updateData(List<Recommendation> data) {
        final RecommendationDiffCallback diffCallback = new RecommendationDiffCallback(this.data, data);
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
        View productView = inflater.inflate(R.layout.fragment_product_recyclerview_recommendation, viewGroup, false);

        // Return a new holder instance
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        // Get product
        final Recommendation recommendation = data.get(index);

        // Fill the views
        viewHolder.nameTxt.setText(recommendation.name);
        viewHolder.priceTxt.setText("CDN$ " + NumberFormatter.getInstance().formatNumber2(recommendation.price));

        // Photo
        Picasso.get()
                .load(Consts.AWS_S3_URL + "/" + recommendation.image_url)
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.pictureImv);

        // Recommendation click
        viewHolder.container.setOnClickListener(v -> {
            if (recommendationAdapterEventsListener != null) {
                recommendationAdapterEventsListener.onItemClick(recommendation, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView priceTxt;
        ImageView pictureImv;
        ConstraintLayout container;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            pictureImv = itemView.findViewById(R.id.pictureImv);
            container = itemView.findViewById(R.id.container);
        }
    }

    public interface RecommendationAdapterEventsListener {
        void onItemClick(Recommendation recommendation, final View productCardView);
    }
}
