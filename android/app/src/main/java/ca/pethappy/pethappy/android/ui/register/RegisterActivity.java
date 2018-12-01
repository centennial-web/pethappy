package ca.pethappy.pethappy.android.ui.register;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Map;

import ca.pethappy.pethappy.android.App;
import ca.pethappy.pethappy.android.R;
import ca.pethappy.pethappy.android.api.ValidationErrors;
import ca.pethappy.pethappy.android.api.ValidationException;
import ca.pethappy.pethappy.android.models.User;
import ca.pethappy.pethappy.android.models.forms.UserRegistration;
import ca.pethappy.pethappy.android.utils.task.SimpleTask;
import ca.pethappy.pethappy.android.ui.base.BaseActivity;
import retrofit2.Response;

// https://guides.codepath.com/android/using-the-app-toolbar
public class RegisterActivity extends BaseActivity {

    // Components
    private TextInputLayout emailLay;
    private TextInputLayout firstNameLay;
    private TextInputLayout lastNameLay;
    private TextInputLayout cellPhoneLay;
    private TextInputLayout addressLay;
    private TextInputLayout unitLay;
    private TextInputLayout buzzerLay;
    private TextInputLayout postalCodeLay;
    private TextInputLayout cityLay;
    private TextInputLayout provinceLay;
    private TextInputLayout passwordLay;
    private TextInputLayout repeatPasswordLay;

    private App app;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        app = (App) getApplication();

        // Toolbar
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Use Registration");
        setSupportActionBar(toolbar);

        // Components
        emailLay = findViewById(R.id.emailLay);
        firstNameLay = findViewById(R.id.firstNameLay);
        lastNameLay = findViewById(R.id.lastNameLay);
        cellPhoneLay = findViewById(R.id.cellPhoneLay);
        addressLay = findViewById(R.id.addressLay);
        unitLay = findViewById(R.id.unitLay);
        buzzerLay = findViewById(R.id.buzzerLay);
        postalCodeLay = findViewById(R.id.postalCodeLay);
        cityLay = findViewById(R.id.cityLay);
        provinceLay = findViewById(R.id.provinceLay);
        passwordLay = findViewById(R.id.passwordLay);
        repeatPasswordLay = findViewById(R.id.repeatPasswordLay);

        // Setup
        emailLay.requestFocus();

        // Uppercase
        InputFilter[] editFilters = postalCodeLay.getEditText().getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        postalCodeLay.getEditText().setFilters(newFilters);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_register_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_register_menu_save:
                registerUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void registerUser() {
        // Reset errors
        emailLay.setError(null);
        firstNameLay.setError(null);
        lastNameLay.setError(null);
        cellPhoneLay.setError(null);
        addressLay.setError(null);
        unitLay.setError(null);
        buzzerLay.setError(null);
        postalCodeLay.setError(null);
        cityLay.setError(null);
        provinceLay.setError(null);
        passwordLay.setError(null);
        repeatPasswordLay.setError(null);

        // Get Values
        String email = emailLay.getEditText().getText().toString();
        String firstName = firstNameLay.getEditText().getText().toString();
        String lastName = lastNameLay.getEditText().getText().toString();
        String cellPhone = cellPhoneLay.getEditText().getText().toString();
        String address = addressLay.getEditText().getText().toString();
        String unit = unitLay.getEditText().getText().toString();
        String buzzer = buzzerLay.getEditText().getText().toString();
        String postalCode = postalCodeLay.getEditText().getText().toString();
        String city = cityLay.getEditText().getText().toString();
        String province = provinceLay.getEditText().getText().toString();
        String password = passwordLay.getEditText().getText().toString();
        String repeatPassword = repeatPasswordLay.getEditText().getText().toString();

        // Validate
        View focusView = null;
        boolean cancel = false;

        // Email
        if (TextUtils.isEmpty(email)) {
            emailLay.setError(getString(R.string.error_field_required));
            focusView = emailLay;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailLay.setError(getString(R.string.error_invalid_email));
            focusView = emailLay;
            cancel = true;
        }
        // First name
        if (TextUtils.isEmpty(firstName)) {
            firstNameLay.setError(getString(R.string.error_invalid_password));
            focusView = firstNameLay;
            cancel = true;
        }
        // Last name
        if (TextUtils.isEmpty(lastName)) {
            lastNameLay.setError(getString(R.string.error_invalid_password));
            focusView = lastNameLay;
            cancel = true;
        }
        // Mobile Phone #
        if (TextUtils.isEmpty(cellPhone)) {
            cellPhoneLay.setError(getString(R.string.error_invalid_password));
            focusView = cellPhoneLay;
            cancel = true;
        }
        // Address
        if (TextUtils.isEmpty(address)) {
            addressLay.setError(getString(R.string.error_invalid_password));
            focusView = addressLay;
            cancel = true;
        }
        // Postal code
        if (TextUtils.isEmpty(postalCode)) {
            postalCodeLay.setError(getString(R.string.error_invalid_password));
            focusView = postalCodeLay;
            cancel = true;
        }
        // City
        if (TextUtils.isEmpty(city)) {
            cityLay.setError(getString(R.string.error_invalid_password));
            focusView = cityLay;
            cancel = true;
        }
        // Province
        if (TextUtils.isEmpty(province)) {
            provinceLay.setError(getString(R.string.error_invalid_password));
            focusView = provinceLay;
            cancel = true;
        }

        // Execute request
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);

            new SimpleTask<Void, User>(
                    none -> {
                        UserRegistration userRegistration = new UserRegistration();
                        userRegistration.email = email;
                        userRegistration.firstName = firstName;
                        userRegistration.lastName = lastName;
                        userRegistration.password = password;
                        userRegistration.repeatPassword = repeatPassword;
                        userRegistration.cellPhone = cellPhone;
                        userRegistration.address = address;
                        userRegistration.unit = unit;
                        userRegistration.postalCode = postalCode;
                        userRegistration.city = city;
                        userRegistration.province = province;
                        userRegistration.buzzer = buzzer;

                        Response<User> response = app.endpoints.registerUser(userRegistration).execute();

                        switch (response.code()) {
                            case 201:
                                return response.body();
                            case 412:
                                throw new ValidationException(response.errorBody());
                            default:
                                throw new Exception("Error " + response.message());
                        }
                    },
                    user -> {
                        showProgress(false);
                        if (user != null) {
                            // TODO: 07/11/18 Store token
                            finish();
                        } else {
                            passwordLay.setError(getString(R.string.error_incorrect_password));
                            passwordLay.requestFocus();
                        }
                    },
                    error -> {
                        if (error instanceof ValidationException) {
                            ValidationErrors validationErrors = ((ValidationException) error).validationErrors;
                            for (Map.Entry<String, String> entry : validationErrors.errors.entrySet()) {
                                String name = entry.getKey() + "Lay";
                                String value = entry.getValue();
                                int id = getResources().getIdentifier(name, "id", RegisterActivity.this.getPackageName());
                                if (id != 0) {
                                    TextInputLayout lay = findViewById(id);
                                    lay.setError(value);
                                    lay.requestFocus();
                                }
                            }
                            showProgress(false);
                        } else {
                            Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            showProgress(false);
                        }
                    }
            ).execute((Void) null);
        }
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
//        // Enable/Disable controls
//        emailTxt.setEnabled(!show);
//        passwordTxt.setEnabled(!show);
//        loginBtn.setEnabled(!show);
//        noAccYetBtn.setEnabled(!show);
//
//        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//        loginGroup.setVisibility(show ? View.GONE : View.VISIBLE);
//        loginGroup.animate().setDuration(shortAnimTime).alpha(
//                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                loginGroup.setVisibility(show ? View.GONE : View.VISIBLE);
//            }
//        });
//
//        loginProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
//        loginProgressBar.animate().setDuration(shortAnimTime).alpha(
//                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                loginProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
//            }
//        });
    }

}
