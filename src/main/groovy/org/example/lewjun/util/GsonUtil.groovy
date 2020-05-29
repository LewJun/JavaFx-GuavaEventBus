package org.example.lewjun.util

import com.google.gson.*
import com.google.gson.annotations.Expose

import java.lang.reflect.Type
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime

class GsonUtil {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Date.class, new JsonSerializer<Date>() {// Date转long
                @Override
                JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
                    new JsonPrimitive(date.getTime())
                }
            }).setDateFormat(DateFormat.LONG)
            .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {// long转Date
                Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    new Date(json.getAsJsonPrimitive().getAsLong())
                }
            }).setDateFormat(DateFormat.LONG)

            .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {// LocalDate转long
                @Override
                JsonElement serialize(LocalDate date, Type type, JsonSerializationContext jsonSerializationContext) {
                    new JsonPrimitive(DateUtils.asLong(date))
                }
            }).setDateFormat(DateFormat.LONG)
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {// long转LocalDate
                LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    DateUtils.asLocalDate(json.getAsJsonPrimitive().getAsLong())
                }
            }).setDateFormat(DateFormat.LONG)

            .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {// LocalDateTime转long
                @Override
                JsonElement serialize(LocalDateTime date, Type type, JsonSerializationContext jsonSerializationContext) {
                    new JsonPrimitive(DateUtils.asLong(date))
                }
            }).setDateFormat(DateFormat.LONG)
            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {// long转LocalDate
                LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    DateUtils.asLocalDateTime(json.getAsJsonPrimitive().getAsLong())
                }
            }).setDateFormat(DateFormat.LONG)
            .disableHtmlEscaping()
            .addDeserializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    final Expose expose = fieldAttributes.getAnnotation(Expose.class)
                    expose && !expose.deserialize()
                }

                @Override
                boolean shouldSkipClass(Class<?> aClass) {
                    false
                }
            }).create()

    static String objToJsonString(Object obj) {
        return GSON.toJson(obj)
    }

    static <T> T jsonStringToObj(String json, Type type) {
        return GSON.fromJson(json, type)
    }
}
