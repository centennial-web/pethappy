package ca.pethappy.server.api;

import ca.pethappy.server.forms.AddCartItem;
import ca.pethappy.server.models.Cart;
import ca.pethappy.server.services.CartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
public class CartsController {
    private final CartsService cartsService;

    @Autowired
    public CartsController(CartsService cartsService) {
        this.cartsService = cartsService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/api/carts/itemCount/{deviceId}")
    public ResponseEntity<?> cartItemsCount(@PathVariable String deviceId, Principal principal) {
        int items = cartsService.getItemCount(UUID.fromString(deviceId), 1L);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/api/carts/addItem", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cartAddItem(@RequestBody AddCartItem cartItem) {
        try {
            cartsService.addItem(cartItem.getDeviceId(), cartItem.getUserId(), cartItem.getProductId());
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
