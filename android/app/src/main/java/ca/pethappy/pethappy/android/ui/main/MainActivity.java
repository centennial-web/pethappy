package ca.pethappy.pethappy.android.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.models.backend.CartItem;
import ca.pethappy.pethappy.android.ui.base.BaseAuthenticatedActivity;
import ca.pethappy.pethappy.android.ui.base.fragments.OnFragmentInteractionListener;
import ca.pethappy.pethappy.android.ui.cart.CartFragment;
import ca.pethappy.pethappy.android.ui.cart.CartListener;
import ca.pethappy.pethappy.android.ui.login.LoginActivity;
import ca.pethappy.pethappy.android.ui.products.ProductFragment;
import ca.pethappy.pethappy.android.ui.products.ProductFragmentListener;
import ca.pethappy.pethappy.android.ui.profile.ProfileFragment;
import ca.pethappy.pethappy.android.ui.settings.SettingsFragment;
import ca.pethappy.pethappy.android.ui.subscriptions.SubscriptionsFragment;
import ca.pethappy.pethappy.android.utils.badge.Badge;
import ca.pethappy.pethappy.android.utils.badge.QBadgeView;
import ca.pethappy.pethappy.android.utils.navigator.BottomNavigationViewEx;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;

public class MainActivity extends BaseAuthenticatedActivity implements
        OnFragmentInteractionListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        CartListener,
        ProductFragmentListener {
    private static final int OPEN_SUBSCRIPTIONS_REQUEST = 1;
    private static final int OPEN_SETTINGS_REQUEST = 2;

    private int activeFragmentId = R.id.navigation_products;

    // Fragments
    private FragmentManager fragmentManager;
    private ProductFragment productFragment;
    private SubscriptionsFragment subscriptionsFragment;
    private CartFragment cartFragment;
    private SettingsFragment settingsFragment;
    private ProfileFragment profileFragment;

    // Bottom navigation
    private BottomNavigationViewEx navigation;
    private Badge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Pet Happy");
        setSupportActionBar(toolbar);

        // Fragments
        fragmentManager = getSupportFragmentManager();
        productFragment = new ProductFragment();
        subscriptionsFragment = new SubscriptionsFragment();
        cartFragment = new CartFragment();
        settingsFragment = new SettingsFragment();
        profileFragment = new ProfileFragment();

        // Navigation
        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.enableAnimation(false);
        navigation.setItemHorizontalTranslationEnabled(false);
        navigation.setTextSize(10);

        // Badge
        badge = new QBadgeView(this)
                .setBadgeNumber(0)
                .setGravityOffset(15, 2, true)
                .setShowShadow(true)
                .bindTarget(navigation.getBottomNavigationItemView(1));
        new SimpleTask<Void, Integer>(
                ignored -> getApp().cartServices.cartItemQuantity(),
                itemQuantity -> badge.setBadgeNumber(itemQuantity),
                error -> badge.setBadgeNumber(0)
        ).execute((Void) null);

    }

    @Override
    protected void onResume() {
        super.onResume();

        openFragment(activeFragmentId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);

//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setSubmitButtonEnabled(true);
//        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                // Clear user store data
                getApp().logoutUser();

                // Send user to login activity
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean openFragment(int menuId) {
        switch (menuId) {
            case R.id.navigation_products:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, productFragment)
                        .commit();
                return true;
            case R.id.navigation_cart:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, cartFragment)
                        .commit();
                return true;
            case R.id.navigation_subscriptions:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, subscriptionsFragment)
                        .commit();
                return true;
            case R.id.navigation_profile:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, profileFragment)
                        .commit();
                return true;
            case R.id.navigation_settings:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, settingsFragment)
                        .commit();
                return true;
        }
        return false;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        activeFragmentId = menuItem.getItemId();
        return openFragment(activeFragmentId);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPEN_SUBSCRIPTIONS_REQUEST) {
            if (resultCode == RESULT_OK) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, subscriptionsFragment)
                        .commit();
                navigation.setSelectedItemId(R.id.navigation_subscriptions);
            } else {
                Toast.makeText(this, "You must be logged to access your subscriptions", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == OPEN_SETTINGS_REQUEST) {
            if (resultCode == RESULT_OK) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, settingsFragment)
                        .commit();
                navigation.setSelectedItemId(R.id.navigation_settings);
            } else {
                Toast.makeText(this, "You must be logged to access the settings", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void addItemToCart(long productId, List<CartItem> cartItems) {
        int quantity = 0;
        for (CartItem item : cartItems) {
            quantity += item.quantity;
        }
        updateBadgeNumber(quantity);
    }

    @Override
    public void removeItemFromCart(long productId, List<CartItem> cartItems) {
        int quantity = 0;
        for (CartItem item : cartItems) {
            quantity += item.quantity;
        }
        updateBadgeNumber(quantity);
    }

    @Override
    public void refreshCart() {
        App app = getApp();
        new SimpleTask<Void, Integer>(
                ignored -> app.cartServices.cartItemQuantity(),
                this::updateBadgeNumber,
                ignored -> {
                }
        ).execute((Void) null);
    }

    private void updateBadgeNumber(int number) {
        // Hide
        if (number <= 0) {
            badge.hide(true);
            return;
        }
        // Update number
        badge.setBadgeNumber(number);
    }

    @Override
    public void onQueryProductTextSubmit(String query) {

    }
}
