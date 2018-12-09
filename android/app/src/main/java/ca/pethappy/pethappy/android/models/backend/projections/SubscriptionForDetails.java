package ca.pethappy.pethappy.android.models.backend.projections;

import java.math.BigDecimal;
import java.util.Date;

public class SubscriptionForDetails {
    public Long id;
    public Date creationDate;
    public int deliveryEvery;
    public int preferredDay;
    public BigDecimal totalBeforeTax;
    public BigDecimal taxesPercent;
    public BigDecimal taxesValue;
    public BigDecimal total;
    public CardForDetails card;
    public boolean cancelled;
}
