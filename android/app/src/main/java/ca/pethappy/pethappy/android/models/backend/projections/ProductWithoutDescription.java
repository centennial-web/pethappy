package ca.pethappy.pethappy.android.models.backend.projections;

import java.math.BigDecimal;

import ca.pethappy.pethappy.android.models.backend.Category;
import ca.pethappy.pethappy.android.models.backend.Ingredient;
import ca.pethappy.pethappy.android.models.backend.Manufacturer;

public class ProductWithoutDescription {
    public Long id;
    public String name;
    public Category category;
    public Manufacturer manufacturer;
    public Ingredient ingredient;
    public String imageUrl;
    public BigDecimal weightKg;
    public BigDecimal price;
    public BigDecimal quantity;
}
