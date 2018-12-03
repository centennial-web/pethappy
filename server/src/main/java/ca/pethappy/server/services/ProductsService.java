package ca.pethappy.server.services;

import ca.pethappy.server.models.Product;
import ca.pethappy.server.repositories.ProductsRepository;
import ca.pethappy.server.repositories.projections.ProductWithoutDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductsService {
    private final ProductsRepository productsRepository;

    @Autowired
    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        return productsRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProductWithoutDescription> findAllWithoutDescription(Pageable pageable) {
        return productsRepository.findAllBy(pageable);
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }
}
