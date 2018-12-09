package ca.pethappy.server.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    private Long id;
    private Subscription subscription;
    private List<OrderItems> items;

    // Dates
    private Date creationDate;
    private Date deliveryDate;
    private Date deliveredDate;

    // Address
    private String firstName;
    private String lastName;
    private String cellPhone;
    private String address;
    private String unit;
    private String postalCode;
    private String city;
    private String province;
    private String buzzer;

    // Totals
    private BigDecimal totalBeforeTax;
    private BigDecimal taxesPercent;
    private BigDecimal taxesValue;
    private BigDecimal total;

    public Order() {
        this.items = new ArrayList<>();
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @Column(name = "creation_date", nullable = false)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Column(name = "delivery_date", nullable = false)
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Column(name = "delivered_date")
    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "cell_phone", nullable = false)
    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    @Column(name = "address", nullable = false)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Column(name = "postal_code", nullable = false)
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Column(name = "city", nullable = false)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "province", nullable = false)
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Column(name = "buzzer", nullable = false)
    public String getBuzzer() {
        return buzzer;
    }

    public void setBuzzer(String buzzer) {
        this.buzzer = buzzer;
    }

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    public List<OrderItems> getItems() {
        return items;
    }

    public void setItems(List<OrderItems> items) {
        this.items = items;
    }

    @Column(name = "total_before_tax", nullable = false)
    public BigDecimal getTotalBeforeTax() {
        return totalBeforeTax;
    }

    public void setTotalBeforeTax(BigDecimal totalBeforeTax) {
        this.totalBeforeTax = totalBeforeTax;
    }

    @Column(name = "taxes_percent", nullable = false)
    public BigDecimal getTaxesPercent() {
        return taxesPercent;
    }

    public void setTaxesPercent(BigDecimal taxesPercent) {
        this.taxesPercent = taxesPercent;
    }

    @Column(name = "taxes_value", nullable = false)
    public BigDecimal getTaxesValue() {
        return taxesValue;
    }

    public void setTaxesValue(BigDecimal taxesValue) {
        this.taxesValue = taxesValue;
    }

    @Column(name = "total", nullable = false)
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
