package ca.pethappy.server.models;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigserial")
    @GraphQLQuery(name = "id", description = "A product's id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "category_id")
    @GraphQLQuery(name = "category", description = "A product's category")
    private Category category;

    @OneToOne
    @JoinColumn(name = "manufacturer_id")
    @GraphQLQuery(name = "manufacturer", description = "A product's manufacturer")
    private Manufacturer manufacturer;

    @OneToOne
    @JoinColumn(name = "mainingredient_id")
    @GraphQLQuery(name = "ingredient", description = "A product's ingredient")
    private Ingredient ingredient;

    @Column(name = "name", length = 100, nullable = false)
    @GraphQLQuery(name = "name", description = "A product's name")
    private String name;

    @Column(name = "description", length = 100, nullable = false)
    @GraphQLQuery(name = "description", description = "A product's description")
    private String description;

    @Column(name = "image_url", length = 500)
    @GraphQLQuery(name = "image_url", description = "A product's image_url")
    private String imageUrl;

    @Column(name = "weight_kg")
    @GraphQLQuery(name = "weight_kg", description = "A product's weight_kg")
    private BigDecimal weightKg;

    @Column(name = "price", nullable = false)
    @GraphQLQuery(name = "price", description = "A product's price")
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    @GraphQLQuery(name = "quantity", description = "A product's quantity")
    private int quantity;
}
