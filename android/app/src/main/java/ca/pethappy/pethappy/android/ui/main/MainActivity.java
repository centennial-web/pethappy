package ca.pethappy.pethappy.android.ui.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.ui.base.BaseActivity;
import ca.pethappy.pethappy.android.ui.base.fragments.OnFragmentInteractionListener;
import ca.pethappy.pethappy.android.ui.cart.CartFragment;
import ca.pethappy.pethappy.android.ui.products.ProductFragment;
import ca.pethappy.pethappy.android.ui.settings.SettingsFragment;
import ca.pethappy.pethappy.android.ui.subscriptions.SubscriptionsFragment;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {
    private FragmentManager fragmentManager;

    // Fragments
    private ProductFragment productFragment;
    private SubscriptionsFragment subscriptionsFragment;
    private CartFragment cartFragment;
    private SettingsFragment settingsFragment;

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

        // Navigation
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                app.logoutUser();
                Toast.makeText(this, "You are not logged anymore", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_products:
                FragmentTransaction trans1 = fragmentManager.beginTransaction();
                trans1.replace(R.id.fragmentContainer, productFragment).commit();
                return true;
            case R.id.navigation_dashboard:
                FragmentTransaction trans2 = fragmentManager.beginTransaction();
                trans2.replace(R.id.fragmentContainer, subscriptionsFragment).commit();
                return true;
            case R.id.navigation_cart:
                FragmentTransaction trans3 = fragmentManager.beginTransaction();
                trans3.replace(R.id.fragmentContainer, cartFragment).commit();
                return true;
            case R.id.navigation_settings:
                FragmentTransaction trans4 = fragmentManager.beginTransaction();
                trans4.replace(R.id.fragmentContainer, settingsFragment).commit();
                return true;
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
