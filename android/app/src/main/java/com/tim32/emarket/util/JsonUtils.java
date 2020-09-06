package com.tim32.emarket.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tim32.emarket.apiclients.dto.Product;

import java.lang.reflect.Type;
import java.util.ArrayList;

public final class JsonUtils {

    private static final Gson gson = new Gson();

    public static final Type PRODUCT_LIST_TYPE = new TypeToken<ArrayList<Product>>() {
    }.getType();

    public static final Type LONG_LIST_TYPE = new TypeToken<ArrayList<Long>>() {
    }.getType();

    public static <T> String toJson(T t) {
        return gson.toJson(t);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }
}
