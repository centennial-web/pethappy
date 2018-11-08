package ca.pethappy.pethappy.android.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.nio.charset.Charset;

import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.consts.Consts;
import ca.pethappy.pethappy.android.tasks.base.SimpleTask;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NewLoginActivity extends AppCompatActivity {

    private EditText emailTxt;
    private EditText passwordTxt;
    private Button loginBtn;
    private Button noAccYetBtn;
    private ProgressBar loginProgressBar;
    private Group loginGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        loginBtn = findViewById(R.id.loginBtn);
        noAccYetBtn = findViewById(R.id.noAccYetBtn);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        loginGroup = findViewById(R.id.loginGroup);

        // If password focused and user clicks DONE
        passwordTxt.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        // Login button click
        loginBtn.setOnClickListener(v -> attemptLogin());

        // No account yet
        noAccYetBtn.setOnClickListener(v -> createAccount());
    }

    private void attemptLogin() {
        // Already attempting
        if (!loginBtn.isEnabled()) {
            return;
        }

        // Reset errors.
        emailTxt.setError(null);
        passwordTxt.setError(null);

        // Store values at the time of the login attempt.
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordTxt.setError(getString(R.string.error_invalid_password));
            focusView = passwordTxt;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailTxt.setError(getString(R.string.error_field_required));
            focusView = emailTxt;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailTxt.setError(getString(R.string.error_invalid_email));
            focusView = emailTxt;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            new SimpleTask<Integer, String>(
                    top -> {
                        String credentials = Credentials.basic(email, password, Charset.forName("UTF-8"));
                        OkHttpClient client = new OkHttpClient
                                .Builder()
                                .build();
                        Request request = new Request
                                .Builder()
                                .header("Authorization", credentials)
                                .url(Consts.SERVER_URL + "/api/login")
                                .get()
                                .build();
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            try (ResponseBody body = response.body()) {
                                if (body != null) {
                                    return body.string();
                                }
                            }
                        }
                        return null;
                    },
                    result -> {
                        showProgress(false);
                        if (result != null) {
                            finish();
                        } else {
                            passwordTxt.setError(getString(R.string.error_incorrect_password));
                            passwordTxt.requestFocus();
                        }
                    },
                    error -> {
                        Toast.makeText(NewLoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        showProgress(false);
                    }
            ).execute(10);
        }
    }

    private void createAccount() {

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        loginGroup.setVisibility(show ? View.GONE : View.VISIBLE);
        loginGroup.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginGroup.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        loginProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        loginProgressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

}
