package ca.pethappy.pethappy.android.models.forms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionForm {
    public Long customerId;
    public int deliveryEvery;
    public int preferredDay;
    public Long cardId;
    public BigDecimal totalBeforeTax;
    public BigDecimal taxesPercent;
    public BigDecimal taxesValue;
    public BigDecimal total;
    public List<SubscriptionItemForm> items;

    public SubscriptionForm() {
        this.items = new ArrayList<>();
    }
}
