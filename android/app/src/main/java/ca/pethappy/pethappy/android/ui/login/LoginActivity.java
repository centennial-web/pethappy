package ca.pethappy.pethappy.android.ui.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.consts.Consts;
import ca.pethappy.pethappy.android.models.forms.UserSettings;
import ca.pethappy.pethappy.android.ui.base.BaseActivity;
import ca.pethappy.pethappy.android.ui.register.RegisterActivity;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends BaseActivity {

    // Views
    private EditText emailTxt;
    private EditText passwordTxt;
    private Button loginBtn;
    private Button noAccYetBtn;
    private ProgressBar loginProgressBar;
    private Group loginGroup;

    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        loginBtn = findViewById(R.id.loginBtn);
        noAccYetBtn = findViewById(R.id.noAccYetBtn);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        loginGroup = findViewById(R.id.loginGroup);

        okHttpClient = new OkHttpClient
                .Builder()
                .build();

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
        noAccYetBtn.setOnClickListener(v -> registerNewUser());
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

            App app = getApp();

            new SimpleTask<Void, LoginReturn>(
                    none -> {
                        // Create basic auth credentials = base64(user : password)
                        String credentials = Credentials.basic(email, password, Charset.forName("UTF-8"));
                        Request request = new Request.Builder()
                                .header("Authorization", credentials)
                                .url(Consts.SERVER_URL + "/api/login")
                                .get().build();

                        // Execute request
                        try (Response response = okHttpClient.newCall(request).execute()) {
                            ResponseBody body;
                            if (response.isSuccessful() && (body = response.body()) != null) {
                                String token = body.string();
                                return new LoginReturn(token, isUsing2FA(token));
                            }
                        }
                        return null;
                    },
                    loginReturn -> {
                        showProgress(false);

                        // User logged successfully
                        if (loginReturn != null && loginReturn.token != null) {
                            // 2FA
                            if (loginReturn.use2fa) {
                                // Component layout
                                final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(20, 0, 20, 0);
                                final EditText input = new EditText(LoginActivity.this);
                                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                input.setMaxLines(1);
                                input.setLayoutParams(layoutParams);

                                // Dialog
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setView(input);
                                builder.setTitle("Enter your confirmation code");
                                builder.setCancelable(false);
                                builder.setIcon(R.drawable.ic_sms_green_24dp);

                                // Confirm click
                                builder.setPositiveButton("Confirm", (dialog, which) -> {
                                    String userCode = input.getText().toString(); // get code inserted by the user

                                    // Code is empty
                                    if (TextUtils.isEmpty(userCode)) {
                                        Toast.makeText(LoginActivity.this, "Invalid code", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    // Verify code on the server
                                    new SimpleTask<Void, String>(
                                            ignored -> {
                                                // Validate
                                                Request request = new Request.Builder()
                                                        .header("Authorization", loginReturn.token)
                                                        .url(Consts.SERVER_URL + "/api/users/" + app.getUserInfo(loginReturn.token).id + "/verificationCode")
                                                        .get().build();

                                                // Execute request
                                                try (Response response = okHttpClient.newCall(request).execute()) {
                                                    ResponseBody body;
                                                    if (response.isSuccessful() && (body = response.body()) != null) {
                                                        return body.string();
                                                    }
                                                }
                                                return null;
                                            },
                                            serverCode -> {
                                                if (serverCode.equalsIgnoreCase(userCode)) {
                                                    getApp().setLocalUserToken(loginReturn.token);
                                                    setResult(RESULT_OK);
                                                    finish();
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "Invalid code", Toast.LENGTH_SHORT).show();
                                                }
                                            },
                                            error -> Toast.makeText(LoginActivity.this, "Error checking code. Details: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                                    ).execute((Void) null);
                                });
                                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                                AlertDialog dialog = builder.create();
                                input.requestFocus();
                                dialog.show();
                            } else {
                                getApp().setLocalUserToken(loginReturn.token);
                                setResult(RESULT_OK);
                                finish();
                            }
                        } else {
                            setResult(RESULT_CANCELED);
                            passwordTxt.setError(getString(R.string.error_incorrect_password));
                            passwordTxt.requestFocus();
                        }
                    },
                    error -> {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        showProgress(false);
                    }
            ).execute((Void) null);
        }
    }

    private boolean isUsing2FA(String token) throws IOException {
        App app = LoginActivity.this.getApp();
        Long userId = app.getUserInfo(token).id;

        Request request = new Request.Builder()
                .header("Authorization", token)
                .url(Consts.SERVER_URL + "/api/settings/" + userId)
                .get().build();

        // Execute request
        try (Response response = okHttpClient.newCall(request).execute()) {
            ResponseBody body;
            if (response.isSuccessful() && (body = response.body()) != null) {
                UserSettings userSettings = app.moshi.adapter(UserSettings.class).fromJson(body.string());
                if (userSettings != null) {
                    return userSettings.use2fa;
                }
            }
        }
        throw new IOException("Couldn't get user settings");
    }

    private static class LoginReturn {
        String token;
        boolean use2fa;

        LoginReturn(String token, boolean use2fa) {
            this.token = token;
            this.use2fa = use2fa;
        }
    }

    private void registerNewUser() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isEmailValid(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // Enable/Disable controls
        emailTxt.setEnabled(!show);
        passwordTxt.setEnabled(!show);
        loginBtn.setEnabled(!show);
        noAccYetBtn.setEnabled(!show);

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
