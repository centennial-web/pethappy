package ca.pethappy.pethappy.android.api;

import ca.pethappy.pethappy.android.models.ApiUser;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SecEndpoints {

    @GET("/api/user")
    Call<ApiUser> apiUser();

}
