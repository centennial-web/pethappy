package ca.pethappy.server.api;

import ca.pethappy.server.services.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrdersController {
    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/api/orders/listing/{subscriptionId}")
    public ResponseEntity<?> ordersForListings(@PathVariable Long subscriptionId) {
        try {
            return new ResponseEntity<>(ordersService.findAllOrderForListingBySubscriptionId(subscriptionId), HttpStatus.OK);
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
