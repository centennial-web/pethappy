package ca.pethappy.server.api;

import ca.pethappy.server.forms.SubscriptionForm;
import ca.pethappy.server.services.SubscriptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubscriptionsController {
    private final SubscriptionsService subscriptionsService;

    @Autowired
    public SubscriptionsController(SubscriptionsService subscriptionsService) {
        this.subscriptionsService = subscriptionsService;
    }

    @PostMapping("/api/subscriptions/new")
    public ResponseEntity<?> newSubscription(@RequestBody SubscriptionForm subscriptionForm) {
        try {
            subscriptionsService.insert(subscriptionForm);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/subscriptions/{userId}")
    public ResponseEntity<?> subscriptions(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>(subscriptionsService.subscriptions(userId), HttpStatus.OK);
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
