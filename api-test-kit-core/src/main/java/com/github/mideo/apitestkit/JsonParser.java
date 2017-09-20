package com.github.mideo.apitestkit;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public final class JsonParser {

    private static ObjectMapper mapper = new ObjectMapper();

    public static Map deserialize(String text)  {
        try {
            return mapper.readValue(text, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String text, Class<T> clazz)  {
        try {
            return (T) mapper.readValue(text, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String serialize(Map entity) {
        try {
            return mapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String serialize(T clazz) {
        try {
            return mapper.writeValueAsString(clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}