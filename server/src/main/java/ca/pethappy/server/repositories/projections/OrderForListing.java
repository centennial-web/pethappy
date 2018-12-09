package ca.pethappy.server.repositories.projections;

import java.math.BigDecimal;
import java.util.Date;

public interface OrderForListing {
    Long getId();

    // Dates
    Date getCreationDate();
    Date getDeliveryDate();
    Date getDeliveredDate();

    // Address
    String getFirstName();
    String getLastName();
    String getCellPhone();
    String getAddress();
    String getUnit();
    String getPostalCode();
    String getCity();
    String getProvince();
    String getBuzzer();

    // Totals
    BigDecimal getTotalBeforeTax();
    BigDecimal getTaxesPercent();
    BigDecimal getTaxesValue();
    BigDecimal getTotal();
}
