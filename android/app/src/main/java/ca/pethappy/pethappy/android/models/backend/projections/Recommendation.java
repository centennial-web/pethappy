package ca.pethappy.pethappy.android.models.backend.projections;

import java.math.BigDecimal;

public class Recommendation {
    public Long id;
    public Long category_id;
    public Long manufacturer_id;
    public String name;
    public String image_url;
    public Long mainingredient_id;
    public BigDecimal weight_kg;
    public BigDecimal price;
    public BigDecimal quantity;
    public int sells;
}
