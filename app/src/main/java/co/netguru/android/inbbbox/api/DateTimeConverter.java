package co.netguru.android.inbbbox.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Type;

public class DateTimeConverter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {

    private static final DateTimeFormatter API_DATE_TIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(API_DATE_TIME_FORMAT.format(src));
    }

    @Override
    public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return ZonedDateTime.parse(json.getAsString(), API_DATE_TIME_FORMAT);
    }
}