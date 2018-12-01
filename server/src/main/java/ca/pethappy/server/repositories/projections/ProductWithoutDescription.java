package ca.pethappy.server.repositories.projections;

import ca.pethappy.server.models.Category;
import ca.pethappy.server.models.Ingredient;
import ca.pethappy.server.models.Manufacturer;

import java.math.BigDecimal;

public interface ProductWithoutDescription {
    Long getId();
    String getName();
    Category getCategory();
    Manufacturer getManufacturer();
    Ingredient getIngredient();
    String getImageUrl();
    BigDecimal getWeightKg();
    BigDecimal getPrice();
    BigDecimal getQuantity();
}
