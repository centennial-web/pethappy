package ca.pethappy.server.api;

import ca.pethappy.server.models.Product;
import ca.pethappy.server.services.OldProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsController {
    private final OldProductsService oldProductsService;

    @Autowired
    public ProductsController(OldProductsService oldProductsService) {
        this.oldProductsService = oldProductsService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/products/findAll")
    public ResponseEntity<Page<Product>> findAll(@Nullable final Pageable pageable) {
        return new ResponseEntity<>(oldProductsService.findAll(pageable), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/products/findAllWithoutDescription")
    public ResponseEntity<?> findAllWithoutDescription(@Param("query") String query) {
        return new ResponseEntity<>(oldProductsService.findAllWithoutDescription(query), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/products/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(oldProductsService.findById(id), HttpStatus.OK);
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/products/recommendations")
    public ResponseEntity<?> recommendations() {
        try {
            return new ResponseEntity<>(oldProductsService.recommendations(), HttpStatus.OK);
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
