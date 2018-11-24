package ca.pethappy.server.api;

import ca.pethappy.server.forms.AddCartItem;
import ca.pethappy.server.services.CartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CartsController {
    private final CartsService cartsService;

    @Autowired
    public CartsController(CartsService cartsService) {
        this.cartsService = cartsService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/api/carts/itemQuantity")
    public ResponseEntity<?> cartItemQuantity(@RequestParam("deviceId") String deviceId,
                                              @RequestParam("userId") Long userId) {
        try {
            int quantity = cartsService.getItemQuantity(UUID.fromString(deviceId), userId);
            return new ResponseEntity<>(quantity, HttpStatus.OK);
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.BAD_REQUEST);
        }
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

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/api/carts/removeItem")
    public ResponseEntity<?> cartRemoveItem(@RequestParam("deviceId") String deviceId,
                                            @RequestParam("userId") Long userId,
                                            @RequestParam("productId") Long productId) {
        try {
            cartsService.removeItem(deviceId, userId, productId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/api/carts/items")
    public ResponseEntity<?> cartItems(@RequestParam("deviceId") String deviceId, @RequestParam("userId") Long userId) {
        try {
            return new ResponseEntity<>(cartsService.cartItems(deviceId, userId), HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
