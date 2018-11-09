package ca.pethappy.pethappy.android.api;

import com.squareup.moshi.Moshi;

import ca.pethappy.pethappy.android.consts.Consts;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class Api {
    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb2RnYXJjaWFsaW1hQGdtYWlsLmNvbSIsImZpcnN0TmFtZSI6IlJvZHJpZ28iLCJsYXN0TmFtZSI6IkxpbWEiLCJyb2xlcyI6WyJST0xFX0FETUlOIl0sImlzcyI6IlBldCBIYXBweSBTZXJ2ZXIiLCJpZCI6MSwiZXhwIjoxNTQxODE4Mzk2LCJpYXQiOjE1NDE3MzE5OTYsImVtYWlsIjoicm9kZ2FyY2lhbGltYUBnbWFpbC5jb20ifQ.AQFi8c4qlRWalVDBZ02q6aykddjZ5rHBdXf2k-4ijvc";
    private static final Api secure = new Api();
    public static Api secure() {
        return secure;
    }
    public final SecEndpoints secEndpoints;

    private Api() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", TOKEN)
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();

        Moshi moshi = new Moshi.Builder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(Consts.SERVER_URL)
                .client(okHttpClient)
                .build();

        secEndpoints = retrofit.create(SecEndpoints.class);
    }
}
