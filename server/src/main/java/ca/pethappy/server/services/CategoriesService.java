package ca.pethappy.server.services;

import ca.pethappy.server.models.Category;
import ca.pethappy.server.repositories.CategoriesRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CategoriesService implements CrudService<Category, Long> {
    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    @Cacheable("categories")
    @GraphQLQuery(name = "categories")
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoriesRepository.findAll();
    }

    @Override
    @GraphQLQuery(name = "category")
    @Transactional(readOnly = true)
    public Optional<Category> findById(@GraphQLArgument(name = "id") Long id) {
        return categoriesRepository.findById(id);
    }

    @Override
    @GraphQLMutation(name = "saveCategory")
    public Category save(@GraphQLArgument(name = "category") Category category) {
        return categoriesRepository.save(category);
    }

    @Override
    @GraphQLMutation(name = "deleteCategory")
    public void delete(@GraphQLArgument(name = "id") Long id) {
        categoriesRepository.deleteById(id);
    }
}
