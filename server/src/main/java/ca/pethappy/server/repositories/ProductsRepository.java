package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Product;
import ca.pethappy.server.repositories.projections.ProductWithoutDescription;
import ca.pethappy.server.repositories.projections.Recommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
    @Override
    @EntityGraph(attributePaths = {"category", "manufacturer", "ingredient"})
    Page<Product> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"category", "manufacturer", "ingredient"})
    List<Product> findAll();

    List<ProductWithoutDescription> findAllBy();

    @Query("select p from Product p where upper(p.description) like %:query% or upper(p.category.name) like %:query% or upper(p.ingredient.name) like %:query%  or upper(p.manufacturer.name) like %:query%")
    List<ProductWithoutDescription> queryAllByTerm(String query);

    @Query(value = "select p.id, p.category_id, p.manufacturer_id, p.name, " +
            "p.image_url, p.mainingredient_id, p.weight_kg, p.price, p.quantity, " +
            "i.quantity sells from subscriptions s " +
            "inner join subscription_items i on i.subscription_id = s.id " +
            "inner join products p on p.id = i.product_id " +
            "group by p.id, i.quantity " +
            "order by i.quantity desc limit 5", nativeQuery = true)
    List<Recommendation> getRecommendations();
}
