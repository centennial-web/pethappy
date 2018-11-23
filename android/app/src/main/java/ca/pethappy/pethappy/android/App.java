package ca.pethappy.pethappy.android;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

import ca.pethappy.pethappy.android.api.NoSecEndpoints;
import ca.pethappy.pethappy.android.api.SecEndpoints;
import ca.pethappy.pethappy.android.consts.Consts;
import ca.pethappy.pethappy.android.models.DecodedToken;
import ca.pethappy.pethappy.android.services.CartServices;
import ca.pethappy.pethappy.android.utils.moshi.BigDecimalAdapter;
import ca.pethappy.pethappy.android.utils.moshi.LongAdapter;
import ca.pethappy.pethappy.android.utils.moshi.UUIDAdapter;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static ca.pethappy.pethappy.android.consts.Consts.DEVICE_ID;
import static ca.pethappy.pethappy.android.consts.Consts.GUEST_TOKEN;
import static ca.pethappy.pethappy.android.consts.Consts.USER_TOKEN;

public class App extends Application {
    private SharedPreferences prefs;

    public SecEndpoints secEndpoints;
    public NoSecEndpoints noSecEndpoints;

    // Services
    public CartServices cartServices;

    // Json
    public Moshi moshi;

    @Override
    public void onCreate() {
        super.onCreate();

        // Services
        this.cartServices = new CartServices(this);

        // Prefs
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Json
        this.moshi = new Moshi.Builder()
                .add(BigDecimal.class, new BigDecimalAdapter().nullSafe())
                .add(UUIDAdapter.class, new UUIDAdapter().nullSafe())
                .add(LongAdapter.class, new LongAdapter().nullSafe())
                .build();

        // Sec (Any real user must be logged)
        OkHttpClient secOkHttp = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", getLocalUserToken())
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();
        Retrofit secRetrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(Consts.SERVER_URL)
                .client(secOkHttp)
                .build();
        secEndpoints = secRetrofit.create(SecEndpoints.class);

        // No Sec (It means that at least GUEST access can access these endpoints)
        OkHttpClient noSecOkHttp = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    String token;

                    // Use user token if possible
                    if (isUserLogged()) {
                        token = getLocalUserToken();
                    }
                    // User guest login
                    else {
                        if (isGuestLogged()) {
                            token = getLocalGuestToken();
                        } else {
                            token = doGuestLogin();
                            setLocalGuestToken(token);
                        }
                    }

                    // Inject token in the request header
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", token)
                            .build();

                    // Proceed with the request
                    return chain.proceed(newRequest);
                })
                .build();
        Retrofit noSecRetrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(Consts.SERVER_URL)
                .client(noSecOkHttp)
                .build();
        noSecEndpoints = noSecRetrofit.create(NoSecEndpoints.class);
    }

    public void setLocalUserToken(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    public void setLocalGuestToken(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(GUEST_TOKEN, token);
        editor.apply();
    }

    public String getLocalUserToken() {
        return prefs.getString(USER_TOKEN, null);
    }

    public String getLocalGuestToken() {
        return prefs.getString(GUEST_TOKEN, null);
    }

    public boolean isUserLogged() {
        String localUserToken = getLocalUserToken();
        if (localUserToken == null) {
            return false;
        }
        return JWT.decode(localUserToken).getExpiresAt().compareTo(new Date()) >= 0;
    }

    public boolean isGuestLogged() {
        String localGuestToken = getLocalUserToken();
        if (localGuestToken == null) {
            return false;
        }
        return JWT.decode(localGuestToken).getExpiresAt().compareTo(new Date()) >= 0;
    }

    public String doGuestLogin() throws IOException {
        String credentials = Credentials.basic(BuildConfig.GUEST_USER, BuildConfig.GUEST_PASSWORD, Charset.forName("UTF-8"));

        Request request = new Request
                .Builder()
                .header("Authorization", credentials)
                .url(Consts.SERVER_URL + "/api/login")
                .get()
                .build();

        Response response = new OkHttpClient.Builder().build().newCall(request).execute();
        if (response.isSuccessful()) {
            try (ResponseBody body = response.body()) {
                if (body != null) {
                    return body.string();
                }
            }
        }
        return null;
    }

    public void logoutUser() {
        setLocalUserToken(null);
    }

    public String getDeviceId() {
        String deviceId = prefs.getString(DEVICE_ID, null);

        if (deviceId == null) {
            // Create
            deviceId = UUID.randomUUID().toString();

            // Save
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(DEVICE_ID, deviceId);
            editor.apply();
        }
        return deviceId;
    }

    public DecodedToken getUserInfo() {
        String token;
        if ((token = getLocalUserToken()) == null) {
            token = getLocalGuestToken();
        }

        // Decode
        DecodedJWT decodedJWT = JWT.decode(token);

        // Create
        DecodedToken decodedToken = new DecodedToken();
        decodedToken.id = decodedJWT.getClaim("id").asLong();
        decodedToken.email = decodedJWT.getSubject();
        decodedToken.exp = decodedJWT.getExpiresAt();
        decodedToken.iat = decodedJWT.getIssuedAt();
        decodedToken.firstName = decodedJWT.getClaim("firstName").asString();
        decodedToken.lastName = decodedJWT.getClaim("lastName").asString();
        decodedToken.iss = decodedJWT.getIssuer();
        decodedToken.roles = decodedJWT.getClaim("roles").asList(String.class);
        decodedToken.roles = decodedJWT.getClaim("roles").asList(String.class);

        return decodedToken;
    }
}
