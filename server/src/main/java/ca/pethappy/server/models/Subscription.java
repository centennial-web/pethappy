package ca.pethappy.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    private Long id;
    private Date creationDate;
    private User customer;
    private int deliveryEvery;
    private int preferredDay;
    private Card card;
    private BigDecimal totalBeforeTax;
    private BigDecimal taxesPercent;
    private BigDecimal taxesValue;
    private BigDecimal total;
    private List<SubscriptionItem> items;
    private boolean cancelled;
    private List<Order> orders;

    public Subscription() {
        items = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigserial")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "creation_date")
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @ManyToOne
    @JoinColumn(name = "customer_id")
    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    @Column(name = "delivery_every")
    public int getDeliveryEvery() {
        return deliveryEvery;
    }

    public void setDeliveryEvery(int deliveryEvery) {
        this.deliveryEvery = deliveryEvery;
    }

    @Column(name = "preferred_day")
    public int getPreferredDay() {
        return preferredDay;
    }

    public void setPreferredDay(int preferredDay) {
        this.preferredDay = preferredDay;
    }

    @ManyToOne
    @JoinColumn(name = "card_id")
    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Column(name = "total_before_tax")
    public BigDecimal getTotalBeforeTax() {
        return totalBeforeTax;
    }

    public void setTotalBeforeTax(BigDecimal totalBeforeTax) {
        this.totalBeforeTax = totalBeforeTax;
    }

    @Column(name = "taxes_percent")
    public BigDecimal getTaxesPercent() {
        return taxesPercent;
    }

    public void setTaxesPercent(BigDecimal taxesPercent) {
        this.taxesPercent = taxesPercent;
    }

    @Column(name = "taxes_value")
    public BigDecimal getTaxesValue() {
        return taxesValue;
    }

    public void setTaxesValue(BigDecimal taxesValue) {
        this.taxesValue = taxesValue;
    }

    @Column(name = "total")
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @OneToMany(
            mappedBy = "subscription",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    public List<SubscriptionItem> getItems() {
        return items;
    }

    public void setItems(List<SubscriptionItem> items) {
        this.items = items;
    }

    @Column(name = "cancelled", nullable = false)
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
