package ca.pethappy.pethappy.android.models.backend;

import java.math.BigDecimal;

public class SubscriptionItem {
    public Long id;
    public Subscription subscription;
    public Product product;
    public BigDecimal price;
    public int quantity;
    public BigDecimal total;

    public SubscriptionItem() {
    }
}
