package ca.pethappy.server;

import ca.pethappy.server.models.Category;
import ca.pethappy.server.models.Ingredient;
import ca.pethappy.server.repos.CategoriesRepo;
import ca.pethappy.server.repos.IngredientsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
public class App implements CommandLineRunner {

    private final DataSource dataSource;
    private final CategoriesRepo categoriesRepo;
    private final IngredientsRepo ingredientsRepo;

    @Autowired
    public App(DataSource dataSource, CategoriesRepo categoriesRepo,
               IngredientsRepo ingredientsRepo) {
        this.dataSource = dataSource;
        this.categoriesRepo = categoriesRepo;
        this.ingredientsRepo = ingredientsRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("DATASOURCE = " + dataSource);

        Category ccc = categoriesRepo.findByName("Fresh Food");

        List<Category> categories = categoriesRepo.findAll();
        for (Category c : categories) {
            System.out.println("Category: " + c.getName());
        }

        List<Ingredient> ingredients = ingredientsRepo.findAll();
        for (Ingredient c : ingredients) {
            System.out.println("Ingredient: " + c.getName());
        }
    }
}
