package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientsRepo extends JpaRepository<Ingredient, Long> {
}
