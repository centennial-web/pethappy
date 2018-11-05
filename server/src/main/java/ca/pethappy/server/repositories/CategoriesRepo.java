package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "categories", path = "categories")
public interface CategoriesRepo extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
