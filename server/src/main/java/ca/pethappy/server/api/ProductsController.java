package ca.pethappy.server.api;

import ca.pethappy.server.models.Product;
import ca.pethappy.server.repositories.ProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ProductsController {

    private final ProductsRepo productsRepo;

    @Autowired
    public ProductsController(ProductsRepo productsRepo) {
        this.productsRepo = productsRepo;
    }

    @ResponseBody
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<Product> getProducts() {
        return productsRepo.findAll();
    }
}
