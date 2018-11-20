package ca.pethappy.pethappy.android.models.backend;

import java.math.BigDecimal;

public class Product {
    public Long id;
    public Category category;
    public Manufacturer manufacturer;
    public Ingredient ingredient;
    public String name;
    public String description;
    public String imageUrl;
    public BigDecimal weightKg;
    public BigDecimal price;
    public BigDecimal quantity;
}
