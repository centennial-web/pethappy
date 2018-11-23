package ca.pethappy.pethappy.android.utils.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;

public class LongAdapter extends JsonAdapter<Long> {

    @Override
    public Long fromJson(JsonReader reader) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL)
            return reader.nextNull();
        return Long.valueOf(reader.nextString());
    }

    @Override
    public void toJson(JsonWriter writer, Long value) throws IOException {
        if (value == null) {
            writer.value("");
        } else {
            writer.value(value.toString());
        }
    }
}
