package com.netopstec.productcollector.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String obj2String(Object o) {
        if (o != null) {
            try {
                return OBJECT_MAPPER.writeValueAsString(o);
            } catch (JsonProcessingException e) {
                log.error("error in JsonUtils --> obj2String: ", e);
            }
        }
        return null;
    }

    /**
     * 字符串转对象
     *
     * @param <T>   the type parameter
     * @param str   json字符串
     * @param clazz 转化的对象类型
     * @return the t 返回对象, 如果str为空或异常，返回null.
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isNotEmpty(str)) {
            try {
                return OBJECT_MAPPER.readValue(str, clazz);
            } catch (IOException e) {
                log.error("error in JsonUtils --> string2Obj: ", e);
            }
        }
        return null;
    }


    /**
     * String 2 list list.
     *
     * @param <T> the type parameter
     * @param str the str
     * @return the list
     * @throws Exception the exception
     */
    public static <T> List<T> string2List(String str, Class<T> clazz) throws Exception {
        if (StringUtils.isNotEmpty(str)) {
            try {
                JavaType javaType = getCollectionType(ArrayList.class, clazz);
                return OBJECT_MAPPER.readValue(str, javaType);
            } catch (Exception e) {
                log.error("error in JsonUtils --> string2List: ", e);
                throw e;
            }
        }
        return null;
    }

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

}
