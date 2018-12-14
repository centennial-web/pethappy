package ca.pethappy.server.services;

import ca.pethappy.server.models.Product;
import ca.pethappy.server.repositories.ProductsRepository;
import ca.pethappy.server.repositories.projections.ProductWithoutDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OldProductsService {
    private final ProductsRepository productsRepository;

    @Autowired
    public OldProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        return productsRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<ProductWithoutDescription> findAllWithoutDescription(String query) {
        if (query == null || query.trim().equals("")) {
            return productsRepository.findAllBy();
        } else {
            return productsRepository.queryAllByTerm(query.toUpperCase());
        }
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }
}
