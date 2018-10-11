package ca.pethappy.server.repos;

import ca.pethappy.server.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepo extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
