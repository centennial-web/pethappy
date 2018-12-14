package ca.pethappy.server.repositories.projections;

import java.math.BigDecimal;

public interface Recommendation {
    Long getid();
    Long getCategory_id();
    Long getManufacturer_id();
    String getName();
    String getImage_url();
    Long getMainingredient_id();
    BigDecimal getWeight_kg();
    BigDecimal getPrice();
    BigDecimal getQuantity();
    int getSells();
}
