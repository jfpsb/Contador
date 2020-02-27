package com.vandamodaintima.jfpsb.contador.sincronizacao;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Type;

public class ZonedDateTimeGsonAdapter implements JsonDeserializer<ZonedDateTime>, JsonSerializer<ZonedDateTime> {
    @Override
    public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String data = json.getAsJsonPrimitive().getAsString();
        return ZonedDateTime.parse(data, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    @Override
    public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }
}
