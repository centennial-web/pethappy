package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
//    @Query("select p from Product p " +
//            "inner join fetch p.category ")// +
//            "inner join fetch p.ingredient " +
//            "inner join fetch p.manufacturer")


    @Override
    @EntityGraph(attributePaths = {"category", "manufacturer", "ingredient"})
    Page<Product> findAll(Pageable pageable);
}
