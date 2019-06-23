package com.zendesk.codingchallenge.search.utils;

import com.google.gson.annotations.SerializedName;
import com.zendesk.codingchallenge.search.exception.SearchCommandFailedException;
import org.springframework.util.ReflectionUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiConsumer;

/**
 * This class is a field introspector that takes care of {@link SerializedName} annotations.
 * <p>
 * This is useful in cases where your JSON serialization does not match your pojo naming conventions.
 */
public final class PojoJsonIntrospectorUtils {

    private PojoJsonIntrospectorUtils() {
        throw new RuntimeException("No instances allowed");
    }

    public static void doWithSerializedNames(Object obj, BiConsumer<String, Object> consumer) {
        Class<?> clazz = obj.getClass();
        try {
            //Introspector class has an internal cache so calling this twice won't multiple reflection operations
            for (PropertyDescriptor descriptor : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
                String fieldName = descriptor.getName();
                if (descriptor.getReadMethod() != null && !"class".equals(fieldName)) {
                    Field field = ReflectionUtils.findField(clazz, fieldName);
                    if (field == null) {
                        throw new SearchCommandFailedException(
                                "Found property [" + fieldName + "] but could not find field definition. Check POJO conventions for class " + clazz
                        );
                    }
                    SerializedName gsonName = field.getAnnotation(SerializedName.class);
                    String name = gsonName == null ? fieldName : gsonName.value();
                    Object fieldValue = descriptor.getReadMethod().invoke(obj);
                    consumer.accept(name, fieldValue);
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new SearchCommandFailedException("Failed to introspect pojo " + clazz, e);
        }
    }

}
