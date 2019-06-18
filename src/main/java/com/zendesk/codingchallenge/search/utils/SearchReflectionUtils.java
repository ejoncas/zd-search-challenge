package com.zendesk.codingchallenge.search.utils;

import com.google.gson.annotations.SerializedName;
import org.springframework.util.ReflectionUtils;

import java.util.function.BiConsumer;

public class SearchReflectionUtils {


    public static void doWithSerializedNames(Object obj, BiConsumer<String, String> consumer) {
        ReflectionUtils.doWithFields(obj.getClass(), f -> {
            SerializedName serializedName = f.getAnnotation(SerializedName.class);
            String name = serializedName == null ? f.getName() : serializedName.value();
            ReflectionUtils.makeAccessible(f);
            String value = String.valueOf(f.get(obj));
            consumer.accept(name, value);
        });
    }
}
