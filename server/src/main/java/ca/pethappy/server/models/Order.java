package ca.pethappy.server.models;

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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigserial")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItems> items = new ArrayList<>();

    // Dates
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Column(name = "delivery_date", nullable = false)
    private Date deliveryDate;

    @Column(name = "delivered_date")
    private Date deliveredDate;

    // Address
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "cell_phone", nullable = false)
    private String cellPhone;

    @Column(nullable = false)
    private String address;

    @Column
    private String unit;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String buzzer;

    // Totals
    @Column(name = "total_before_tax", nullable = false)
    private BigDecimal totalBeforeTax;

    @Column(name = "taxes_percent", nullable = false)
    private BigDecimal taxesPercent;

    @Column(name = "taxes_value", nullable = false)
    private BigDecimal taxesValue;

    @Column(nullable = false)
    private BigDecimal total;
}
