package ca.pethappy.pethappy.android.models.backend.projections;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderForListing {
    public Long id;

    // Dates
    public Date creationDate;
    public Date deliveryDate;
    public Date deliveredDate;

    // Address
    public String firstName;
    public String lastName;
    public String cellPhone;
    public String address;
    public String unit;
    public String postalCode;
    public String city;
    public String province;
    public String buzzer;

    // Totals
    public BigDecimal totalBeforeTax;
    public BigDecimal taxesPercent;
    public BigDecimal taxesValue;
    public BigDecimal total;
}
