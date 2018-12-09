package ca.pethappy.pethappy.android.ui.subscriptions;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import ca.pethappy.pethappy.android.models.backend.projections.SubscriptionForListing;

public class SubscriptionDiffCallback extends DiffUtil.Callback {
    private final List<SubscriptionForListing> oldList;
    private final List<SubscriptionForListing> newList;

    SubscriptionDiffCallback(List<SubscriptionForListing> oldList, List<SubscriptionForListing> newList) {
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
        SubscriptionForListing sOld = oldList.get(oldItemPosition);
        SubscriptionForListing sNew = newList.get(oldItemPosition);

        return sOld.id.equals(sNew.id)
                && sOld.cancelled == sNew.cancelled
                && sOld.card.id.equals(sNew.card.id)
                && sOld.deliveryEvery == sNew.deliveryEvery
                && sOld.preferredDay == sNew.preferredDay
                && sOld.total.equals(sNew.total);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
