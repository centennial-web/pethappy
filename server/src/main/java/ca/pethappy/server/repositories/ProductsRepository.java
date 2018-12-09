package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Product;
import ca.pethappy.server.repositories.projections.ProductWithoutDescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
    @Override
    @EntityGraph(attributePaths = {"category", "manufacturer", "ingredient"})
    Page<Product> findAll(Pageable pageable);

    Page<ProductWithoutDescription> findAllBy(Pageable pageable);
}
