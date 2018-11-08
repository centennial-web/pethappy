package ca.pethappy.pethappy.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import ca.pethappy.pethappy.android.activities.NewLoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button testLoginBtn = findViewById(R.id.testLoginBtn);
        testLoginBtn.setOnClickListener(v -> {
            Intent loginIntent = new Intent(MainActivity.this, NewLoginActivity.class);
            startActivity(loginIntent);
//            MainActivity.this.finish();
        });
    }
}
