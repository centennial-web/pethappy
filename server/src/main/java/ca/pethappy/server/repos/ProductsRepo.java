package ca.pethappy.server.repos;

import ca.pethappy.server.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductsRepo extends JpaRepository<Product, Long> {
}
