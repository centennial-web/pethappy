package ca.pethappy.pethappy.android.ui.subscriptions;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import ca.pethappy.pethappy.android.models.backend.SubscriptionItem;

public class SubscriptionItemsDiffCallback extends DiffUtil.Callback {
    private final List<SubscriptionItem> oldList;
    private final List<SubscriptionItem> newList;

    SubscriptionItemsDiffCallback(List<SubscriptionItem> oldList, List<SubscriptionItem> newList) {
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
        SubscriptionItem sOld = oldList.get(oldItemPosition);
        SubscriptionItem sNew = newList.get(oldItemPosition);

        return sOld.id.equals(sNew.id);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
