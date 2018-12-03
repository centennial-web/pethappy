package ca.pethappy.server.forms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionForm {
    private Long customerId;
    private int deliveryEvery;
    private int preferredDay;
    private Long cardId;
    private BigDecimal totalBeforeTax;
    private BigDecimal taxesPercent;
    private BigDecimal taxesValue;
    private BigDecimal total;
    private List<SubscriptionItemForm> items;

    public SubscriptionForm() {
        this.items = new ArrayList<>();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public int getDeliveryEvery() {
        return deliveryEvery;
    }

    public void setDeliveryEvery(int deliveryEvery) {
        this.deliveryEvery = deliveryEvery;
    }

    public int getPreferredDay() {
        return preferredDay;
    }

    public void setPreferredDay(int preferredDay) {
        this.preferredDay = preferredDay;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public BigDecimal getTotalBeforeTax() {
        return totalBeforeTax;
    }

    public void setTotalBeforeTax(BigDecimal totalBeforeTax) {
        this.totalBeforeTax = totalBeforeTax;
    }

    public BigDecimal getTaxesPercent() {
        return taxesPercent;
    }

    public void setTaxesPercent(BigDecimal taxesPercent) {
        this.taxesPercent = taxesPercent;
    }

    public BigDecimal getTaxesValue() {
        return taxesValue;
    }

    public void setTaxesValue(BigDecimal taxesValue) {
        this.taxesValue = taxesValue;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<SubscriptionItemForm> getItems() {
        return items;
    }

    public void setItems(List<SubscriptionItemForm> items) {
        this.items = items;
    }
}
