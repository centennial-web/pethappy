package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "products", collectionResourceRel = "products")
public interface ProductsRepo extends JpaRepository<Product, Long> {
}
