package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "ingredients", path = "ingredients")
public interface IngredientsRepo extends JpaRepository<Ingredient, Long> {
}
