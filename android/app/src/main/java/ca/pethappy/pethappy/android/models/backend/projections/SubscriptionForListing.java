package ca.pethappy.pethappy.android.models.backend.projections;

import java.math.BigDecimal;
import java.util.Date;

public class SubscriptionForListing {
    public Long id;
    public Date creationDate;
    public int deliveryEvery;
    public int preferredDay;
    public BigDecimal totalBeforeTax;
    public BigDecimal taxesPercent;
    public BigDecimal taxesValue;
    public BigDecimal total;
    public CardForListing card;
    public boolean cancelled;
}
