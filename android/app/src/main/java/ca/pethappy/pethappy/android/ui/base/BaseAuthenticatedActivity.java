package ca.pethappy.pethappy.android.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import ca.pethappy.pethappy.android.ui.login.LoginActivity;

/**
 * This base activity will redirect to login if nobody is logged
 */
public abstract class BaseAuthenticatedActivity extends BaseActivity {
    private static final int LOGIN_REQUEST = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The user is not logged if there is no token or if the token is expired
        if (!app.isUserLogged()) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, LOGIN_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_REQUEST) {
            if (resultCode != RESULT_OK) {
                finish();
            }
        }
    }
}
