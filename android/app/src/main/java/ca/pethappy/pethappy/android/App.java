package ca.pethappy.pethappy.android;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.squareup.moshi.Moshi;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import ca.pethappy.pethappy.android.api.Endpoints;
import ca.pethappy.pethappy.android.consts.Consts;
import ca.pethappy.pethappy.android.models.DecodedToken;
import ca.pethappy.pethappy.android.services.CartServices;
import ca.pethappy.pethappy.android.utils.moshi.BigDecimalAdapter;
import ca.pethappy.pethappy.android.utils.moshi.LongAdapter;
import ca.pethappy.pethappy.android.utils.moshi.UUIDAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static ca.pethappy.pethappy.android.consts.Consts.DEVICE_ID;
import static ca.pethappy.pethappy.android.consts.Consts.USER_TOKEN;

public class App extends Application {
    private SharedPreferences prefs;

    public Endpoints endpoints;

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

        // Okhttp
        OkHttpClient okHttp = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", getLocalUserToken())
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(Consts.SERVER_URL)
                .client(okHttp)
                .build();

        // Endpoints
        endpoints = retrofit.create(Endpoints.class);
    }

    public void setLocalUserToken(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    public String getLocalUserToken() {
        return prefs.getString(USER_TOKEN, null);
    }

    public boolean isUserLogged() {
        String localUserToken = getLocalUserToken();
        if (localUserToken == null) {
            return false;
        }
        return JWT.decode(localUserToken).getExpiresAt().compareTo(new Date()) >= 0;
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
        String token = getLocalUserToken();
        if (token == null) {
            return null;
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
