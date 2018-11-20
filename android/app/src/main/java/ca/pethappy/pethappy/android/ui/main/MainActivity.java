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

import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.ui.base.BaseActivity;
import ca.pethappy.pethappy.android.ui.base.fragments.OnFragmentInteractionListener;
import ca.pethappy.pethappy.android.ui.cart.CartFragment;
import ca.pethappy.pethappy.android.ui.login.LoginActivity;
import ca.pethappy.pethappy.android.ui.products.ProductFragment;
import ca.pethappy.pethappy.android.ui.settings.SettingsFragment;
import ca.pethappy.pethappy.android.ui.subscriptions.SubscriptionsFragment;

public class MainActivity extends BaseActivity implements OnFragmentInteractionListener,
        BottomNavigationView.OnNavigationItemSelectedListener {
    private static final int OPEN_SUBSCRIPTIONS_REQUEST = 1;
    private static final int OPEN_SETTINGS_REQUEST = 2;

    // Fragments
    private FragmentManager fragmentManager;
    private ProductFragment productFragment;
    private SubscriptionsFragment subscriptionsFragment;
    private CartFragment cartFragment;
    private SettingsFragment settingsFragment;

    // Bottom navigation
    private BottomNavigationView navigation;

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

        // Set products as default fragment
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, productFragment)
                .commit();

        // Navigation
        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                app.logoutUser();
                Toast.makeText(this, "Log out successfully", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_products:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, productFragment)
                        .commit();
                return true;
            case R.id.navigation_subscriptions:
                if (!app.isUserLogged()) {
                    startActivityForResult(new Intent(this, LoginActivity.class), OPEN_SUBSCRIPTIONS_REQUEST);
                    return false;
                } else {
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, subscriptionsFragment)
                            .commit();
                    return true;
                }
            case R.id.navigation_cart:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, cartFragment)
                        .commit();
                return true;
            case R.id.navigation_settings:
                if (!app.isUserLogged()) {
                    startActivityForResult(new Intent(this, LoginActivity.class), OPEN_SETTINGS_REQUEST);
                    return false;
                } else {
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, settingsFragment)
                            .commit();
                    return true;
                }
        }
        return false;
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
}
