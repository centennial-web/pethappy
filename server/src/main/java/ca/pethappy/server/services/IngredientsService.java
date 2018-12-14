package ca.pethappy.server.services;

import ca.pethappy.server.models.Ingredient;
import ca.pethappy.server.repositories.IngredientsRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IngredientsService implements CrudService<Ingredient, Long> {
    private final IngredientsRepository ingredientsRepository;

    @Autowired
    public IngredientsService(IngredientsRepository ingredientsRepository) {
        this.ingredientsRepository = ingredientsRepository;
    }

    @Override
    @Cacheable("ingredients")
    @GraphQLQuery(name = "ingredients")
    @Transactional(readOnly = true)
    public List<Ingredient> findAll() {
        return ingredientsRepository.findAll();
    }

    @Override
    @GraphQLQuery(name = "ingredient")
    @Transactional(readOnly = true)
    public Optional<Ingredient> findById(@GraphQLArgument(name = "id") Long id) {
        return ingredientsRepository.findById(id);
    }

    @Override
    @GraphQLMutation(name = "saveIngredient")
    public Ingredient save(@GraphQLArgument(name = "ingredient") Ingredient ingredient) {
        return ingredientsRepository.save(ingredient);
    }

    @Override
    @GraphQLMutation(name = "deleteIngredient")
    public void delete(@GraphQLArgument(name = "id") Long id) {
        ingredientsRepository.deleteById(id);
    }
}
