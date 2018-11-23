package ca.pethappy.pethappy.android.utils.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

public class UUIDAdapter extends JsonAdapter<UUID> {

    @Override
    public UUID fromJson(JsonReader reader) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL)
            return reader.nextNull();
        return UUID.fromString(reader.nextString());
    }

    @Override
    public void toJson(JsonWriter writer, UUID value) throws IOException {
        if (value == null) {
            writer.value("");
        } else {
            writer.value(value.toString());
        }
    }
}
