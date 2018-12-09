package ca.pethappy.server.repositories.projections;

import java.math.BigDecimal;
import java.util.Date;

public interface SubscriptionForDetails {
    Long getId();
    Date getCreationDate();
    int getDeliveryEvery();
    int getPreferredDay();
    BigDecimal getTotalBeforeTax();
    BigDecimal getTaxesPercent();
    BigDecimal getTaxesValue();
    BigDecimal getTotal();
    CardForDetails getCard();
    boolean getCancelled();
}
