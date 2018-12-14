package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Product;
import ca.pethappy.server.repositories.projections.ProductWithoutDescription;
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
}
