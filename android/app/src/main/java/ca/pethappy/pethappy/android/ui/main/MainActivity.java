package ca.pethappy.pethappy.android.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.ui.base.BaseActivity;
import ca.pethappy.pethappy.android.ui.home.HomeActivity;
import ca.pethappy.pethappy.android.ui.login.LoginActivity;
import ca.pethappy.pethappy.android.ui.profile.ProfileActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button testLoginBtn = findViewById(R.id.testLoginBtn);
        testLoginBtn.setOnClickListener(v -> {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
//            MainActivity.this.finish();
        });
        Button navHome = findViewById(R.id.homeBtn);
        navHome.setOnClickListener(v -> {
            Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(loginIntent);
//            MainActivity.this.finish();
        });

        // Logout
        Button logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> app.logoutUser());

        // Profile
        Button profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }
}
