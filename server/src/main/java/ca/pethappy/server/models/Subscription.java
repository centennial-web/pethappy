package ca.pethappy.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigserial")
    private Long id;

    @Column(name = "creation_date")
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "delivery_every")
    private int deliveryEvery;

    @Column(name = "preferred_day")
    private int preferredDay;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "total_before_tax")
    private BigDecimal totalBeforeTax;

    @Column(name = "taxes_percent")
    private BigDecimal taxesPercent;

    @Column(name = "taxes_value")
    private BigDecimal taxesValue;

    @Column(name = "total")
    private BigDecimal total;

    @OneToMany(
            mappedBy = "subscription",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SubscriptionItem> items = new ArrayList<>();

    @Column(name = "cancelled", nullable = false)
    private boolean cancelled;

    @JsonIgnore
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;
}
