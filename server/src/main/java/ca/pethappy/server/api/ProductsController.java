package ca.pethappy.server.api;

import ca.pethappy.server.models.Product;
import ca.pethappy.server.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsController {
    private final ProductsService productsService;

    @Autowired
    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/products/findAll")
    public ResponseEntity<Page<Product>> findAll(@Nullable final Pageable pageable) {
        return new ResponseEntity<>(productsService.findAll(pageable), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/products/findAllWithoutDescription")
    public ResponseEntity<?> findAllWithoutDescription(@Nullable final Pageable pageable) {
        return new ResponseEntity<>(productsService.findAllWithoutDescription(pageable), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/products/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(productsService.findById(id), HttpStatus.OK);
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
