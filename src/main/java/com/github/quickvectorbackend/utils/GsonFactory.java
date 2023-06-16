package com.github.quickvectorbackend.utils;

import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.jts.JtsAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.LongSerializationPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class GsonFactory {

 public static final SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SS");

  public static  Gson GSON = null;

  public static Gson getGson(){
    if (GSON == null) {
      GSON = generatorGson();
    }
    return GSON;
  }

  private static Gson generatorGson() {

    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
    gsonBuilder
        .serializeNulls()
        .registerTypeAdapter(Integer.class,
            (JsonSerializer<Integer>) (src, typeOfSrc, context) -> new JsonPrimitive(
                String.valueOf(src))).registerTypeAdapter(Integer.class,
            (JsonDeserializer<Integer>) (json, typeOfT, context) -> Integer.parseInt(
                json.getAsString()))
        .registerTypeAdapter(LocalDateTime.class, jsonSerializerDateTime)
        .registerTypeAdapter(LocalDate.class, jsonSerializerDate)
        .registerTypeAdapter(Date.class, jsonDate)
        .registerTypeAdapter(LocalDateTime.class, jsonDeserializerDateTime)
        .registerTypeAdapter(LocalDate.class, jsonDeserializerDate)
        .registerTypeAdapter(Date.class, jsonDateTime)
        .registerTypeAdapterFactory(new JtsAdapterFactory())
        .registerTypeAdapterFactory(new GeometryAdapterFactory())

    ;

    return gsonBuilder.create();
  }


  final static JsonSerializer<LocalDateTime> jsonSerializerDateTime = (localDateTime, type, jsonSerializationContext)
      -> new JsonPrimitive(
      localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
  final static JsonSerializer<LocalDate> jsonSerializerDate = (localDate, type, jsonSerializationContext)
      -> new JsonPrimitive(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

  final static JsonSerializer<Date> jsonDate = (date, type, jsonSerializationContext)
      -> new JsonPrimitive(sdf.format(date));


  //反序列化
  final static JsonDeserializer<LocalDateTime> jsonDeserializerDateTime = (jsonElement, type, jsonDeserializationContext)
      -> LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(),
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  final static JsonDeserializer<LocalDate> jsonDeserializerDate = (jsonElement, type, jsonDeserializationContext)
      -> LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString(),
      DateTimeFormatter.ofPattern("yyyy-MM-dd"));


  final static JsonDeserializer<Date> jsonDateTime = (jsonElement, type, jsonDeserializationContext)
      -> {
    try {
      return sdf.parse(jsonElement.getAsJsonPrimitive().getAsString());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return new Date();
  };
}
