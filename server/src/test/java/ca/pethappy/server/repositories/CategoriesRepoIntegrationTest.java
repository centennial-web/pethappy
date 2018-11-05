package ca.pethappy.server.repositories;

import ca.pethappy.server.models.Category;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoriesRepoIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoriesRepo categoriesRepo;

    @Test
    public void whenFindByName_thenReturnCategory() {
        // Given
        Category c = new Category();
        c.setName("Dog Food");
        entityManager.persist(c);
        entityManager.flush();

        // When
        Category found = categoriesRepo.findByName("Dog Food");

        // Then
        Assertions.assertThat(found.getName()).isEqualTo(c.getName());
    }
}
