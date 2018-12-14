package ca.pethappy.server.services;

import ca.pethappy.server.models.Product;
import ca.pethappy.server.repositories.ProductsRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
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
public class ProductService implements CrudService<Product, Long> {
    private final ProductsRepository productsRepository;

    @Autowired
    public ProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    @Cacheable("products")
    @GraphQLQuery(name = "products")
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productsRepository.findAll();
    }

    @Override
    @GraphQLQuery(name = "product")
    @Transactional(readOnly = true)
    public Optional<Product> findById(@GraphQLArgument(name = "id") Long id) {
        return productsRepository.findById(id);
    }

    @Override
    @GraphQLMutation(name = "saveProduct")
    public Product save(@GraphQLArgument(name = "product") Product product) {
        return productsRepository.save(product);
    }

    @Override
    @GraphQLMutation(name = "deleteProduct")
    public void delete(@GraphQLArgument(name = "id") Long id) {
        productsRepository.deleteById(id);
    }

    @GraphQLQuery(name = "isAvailable")
    public boolean isAvailable(@GraphQLContext Product product) {
        return (product.getQuantity() > 0);
    }
}
