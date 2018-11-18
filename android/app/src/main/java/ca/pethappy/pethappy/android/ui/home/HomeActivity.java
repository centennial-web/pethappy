package ca.pethappy.pethappy.android.ui.home;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import ca.pethappy.pethappy.android.ui.cart.CartFragment;
import ca.pethappy.pethappy.android.ui.preferences.PreferenceFragment;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.ui.base.BaseActivity;

public class HomeActivity extends BaseActivity {

    private BottomNavigationView mainNav;
    private FrameLayout mainFrame;
    private HomeFragment homeFragment;
    private CartFragment cartFragment;
    private PreferenceFragment preferenceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mainFrame = (FrameLayout)findViewById(R.id.main_frame);
        mainNav =(BottomNavigationView)findViewById(R.id.main_nav);

        homeFragment = new HomeFragment();
        cartFragment = new CartFragment();
        preferenceFragment = new PreferenceFragment();

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_cart:
                        setFragment(cartFragment);
                        return true;
                    case R.id.nav_preferences:
                        setFragment(preferenceFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }
}
