package ca.pethappy.pethappy.android.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import okhttp3.ResponseBody;

public class ValidationException extends Exception {
    public final ValidationErrors validationErrors;

    public ValidationException(ResponseBody responseBody) throws IOException {
        super("Validation error");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ValidationErrors> val = moshi.adapter(ValidationErrors.class);
        this.validationErrors = val.fromJson(responseBody.string());
    }
}
