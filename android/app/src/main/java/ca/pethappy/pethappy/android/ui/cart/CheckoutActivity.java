package ca.pethappy.pethappy.android.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.models.User;
import ca.pethappy.pethappy.android.models.backend.Card;
import ca.pethappy.pethappy.android.models.backend.CartItem;
import ca.pethappy.pethappy.android.models.forms.SubscriptionForm;
import ca.pethappy.pethappy.android.models.forms.SubscriptionItemForm;
import ca.pethappy.pethappy.android.ui.base.BaseAuthenticatedActivity;
import ca.pethappy.pethappy.android.ui.products.ProductDetailsActivity;
import ca.pethappy.pethappy.android.utils.formatters.NumberFormatter;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;

public class CheckoutActivity extends BaseAuthenticatedActivity {
    private CheckoutAdapter checkoutAdapter;

    // Components
    private TextView totalBeforeTaxTxt;
    private TextView gstHstTxt;
    private TextView subsTotalTxt;
    private TextView address1Txt;
    private TextView address2Txt;
    private TextView address3Txt;
    private TextView cardNumberTxt;
    private TextView cardExpTxt;
    private TextView cardHolderTxt;
    private Spinner deliverySpinner;
    private Spinner daySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Components
        Button placeSubscriptionBtn = findViewById(R.id.placeSubscriptionBtn);
        totalBeforeTaxTxt = findViewById(R.id.totalBeforeTaxTxt);
        gstHstTxt = findViewById(R.id.gstHstTxt);
        subsTotalTxt = findViewById(R.id.subsTotalTxt);
        address1Txt = findViewById(R.id.address1Txt);
        address2Txt = findViewById(R.id.address2Txt);
        address3Txt = findViewById(R.id.address3Txt);
        cardNumberTxt = findViewById(R.id.cardNumberTxt);
        cardExpTxt = findViewById(R.id.cardExpTxt);
        cardHolderTxt = findViewById(R.id.cardHolderTxt);
        deliverySpinner = findViewById(R.id.deliverySpinner);
        daySpinner = findViewById(R.id.daySpinner);

        // Get app reference to be used inside other threads
        final App app = getApp();

        // Place order
        placeSubscriptionBtn.setOnClickListener(v -> {
            new SimpleTask<Void, Boolean>(
                    ignored -> {
                        List<CartItem> cartItems = app.cartServices.listItems();

                        // Total before tax
                        BigDecimal totalBeforeTax = BigDecimal.ZERO;
                        for (CartItem cartItem : cartItems) {
                            totalBeforeTax = totalBeforeTax.add(cartItem.product.price.multiply(BigDecimal.valueOf(cartItem.quantity)));
                        }

                        // Subscription
                        SubscriptionForm form = new SubscriptionForm();
                        form.customerId = app.getUserInfo().id;
                        form.deliveryEvery = (deliverySpinner.getSelectedItemPosition() + 1);
                        form.preferredDay = (daySpinner.getSelectedItemPosition() + 1);
                        form.cardId = app.userServices.getUserCards().get(0).id;
                        form.totalBeforeTax = totalBeforeTax;
                        form.taxesPercent = new BigDecimal("13");
                        form.taxesValue = form.totalBeforeTax.multiply(form.taxesPercent).divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
                        form.total = form.totalBeforeTax.add(form.taxesValue);

                        // Subscription Items
                        for (CartItem cartItem : cartItems) {
                            SubscriptionItemForm item = new SubscriptionItemForm();
                            item.productId = cartItem.product.id;
                            item.price = cartItem.product.price;
                            item.quantity = cartItem.quantity;
                            item.total = cartItem.product.price.multiply(BigDecimal.valueOf(cartItem.quantity));
                            form.items.add(item);
                        }

                        return app.subscriptionService.newSubscription(form);
                    },
                    created -> {
                        Toast.makeText(getApplicationContext(), "Subscription created", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    error -> {
                        Toast.makeText(getApplicationContext(), "Error creating subscription. Details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            ).execute((Void) null);
            Toast.makeText(getApplicationContext(), "PLACE ORDER", Toast.LENGTH_SHORT).show();
        });

        // Adapter
        checkoutAdapter = new CheckoutAdapter(getApp(), null, (cartItem, viewHolder) -> {
            Intent productDetailsIntent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
            productDetailsIntent.putExtra(ProductDetailsActivity.EXTRA_PRODUCT_ID, cartItem.product.id);
            startActivity(productDetailsIntent);
        });

        // Recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(checkoutAdapter);

        // Query on server
        new SimpleTask<Void, QueryReturn>(
                none -> new QueryReturn(
                        app.cartServices.listItems(),
                        app.userServices.getUserCards(),
                        app.userServices.getUser()),
                queryReturn -> {
                    // Feed the recycler view
                    checkoutAdapter.updateData(queryReturn.cartItems);

                    // Update total (bottom)
                    updateComponentsData(queryReturn);
                },
                error -> Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ).execute((Void) null);
    }

    static class QueryReturn {
        List<CartItem> cartItems;
        List<Card> cards;
        User user;

        QueryReturn(List<CartItem> cartItems, List<Card> cards, User user) {
            this.cartItems = cartItems == null ? new ArrayList<>() : cartItems;
            this.cards = cards == null ? new ArrayList<>() : cards;
            this.user = user;
        }
    }

    /**
     * Count and Sum the cartAdapter.getCartItems and
     * update the total label.
     */
    private void updateComponentsData(final QueryReturn queryReturn) {
        // Shipping address
        address1Txt.setText((TextUtils.isEmpty(queryReturn.user.unit) ? "" : queryReturn.user.unit + "-") + queryReturn.user.address);
        address2Txt.setText(queryReturn.user.city + " " + queryReturn.user.province + " " + queryReturn.user.postalCode);
        if (TextUtils.isEmpty(queryReturn.user.buzzer)) {
            address3Txt.setVisibility(View.GONE);
        } else {
            address3Txt.setVisibility(View.VISIBLE);
            address3Txt.setText("Buzzer " + queryReturn.user.buzzer);
        }

        // Credit card
        if (queryReturn.cards.size() > 0) {
            Card card = queryReturn.cards.get(0);
            cardNumberTxt.setText(card.number);
            cardExpTxt.setText("Expires in " + card.expMonth + "/" + card.expYear);
            cardHolderTxt.setText(card.nameOnCard);
        }

        // Total before tax
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : queryReturn.cartItems) {
            total = total.add(cartItem.product.price.multiply(BigDecimal.valueOf(cartItem.quantity)));
        }
        totalBeforeTaxTxt.setText("CDN$ " + NumberFormatter.getInstance().formatNumber2(total));

        // GST/HST
        BigDecimal gstHstPer = BigDecimal.valueOf(13);
        BigDecimal gstHstValue = total.multiply(gstHstPer).divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
        gstHstTxt.setText("CDN$ " + NumberFormatter.getInstance().formatNumber2(gstHstValue));

        // Subscription total
        BigDecimal subsTotal = total.add(gstHstValue);
        subsTotalTxt.setText("CDN$ " + NumberFormatter.getInstance().formatNumber2(subsTotal));
    }
}
