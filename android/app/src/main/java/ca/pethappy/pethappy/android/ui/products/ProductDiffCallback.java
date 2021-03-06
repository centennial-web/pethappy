package ca.pethappy.pethappy.android.ui.products;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import ca.pethappy.pethappy.android.models.backend.projections.ProductWithoutDescription;

public class ProductDiffCallback extends DiffUtil.Callback {
    private final List<ProductWithoutDescription> oldList;
    private final List<ProductWithoutDescription> newList;

    ProductDiffCallback(List<ProductWithoutDescription> oldList, List<ProductWithoutDescription> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).id.equals(newList.get(newItemPosition).id);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).id.equals(newList.get(newItemPosition).id);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
