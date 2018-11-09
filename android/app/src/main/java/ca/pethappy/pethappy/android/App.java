package ca.pethappy.pethappy.android;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.nio.charset.Charset;

import ca.pethappy.pethappy.android.api.NoSecEndpoints;
import ca.pethappy.pethappy.android.api.SecEndpoints;
import ca.pethappy.pethappy.android.consts.Consts;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static ca.pethappy.pethappy.android.consts.Consts.GUEST_PASSWORD;
import static ca.pethappy.pethappy.android.consts.Consts.GUEST_USER;

public class App extends Application {
    private SharedPreferences prefs;

    public SecEndpoints secEndpoints;
    public NoSecEndpoints noSecEndpoints;

    @Override
    public void onCreate() {
        super.onCreate();

        // Prefs
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Json
        Moshi moshi = new Moshi.Builder()
                .build();

        // Sec
        OkHttpClient secOkHttp = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", getToken())
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

        // No Sec
        OkHttpClient noSecOkHttp = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", getGuestToken())
                            .build();
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

    public void setToken(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("TOKEN", token);
        editor.apply();
    }

    public String getToken() {
        return prefs.getString("TOKEN", null);
    }

    private String getGuestToken() throws IOException {
        String credentials = Credentials.basic(GUEST_USER, GUEST_PASSWORD, Charset.forName("UTF-8"));

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
}
