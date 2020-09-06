package com.tim32.emarket.util;

import android.os.Bundle;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

public final class BundleUtil {

    public static <T> T fromJsonExtraOrNull(Bundle bundle, String key, Class<T> clazz) {
        if (bundle == null) {
            return null;
        }
        String json = bundle.getString(key);
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JsonUtils.fromJson(json, clazz);
    }

    public static <T> T fromJsonExtraOrNull(Bundle bundle, String key, Type type) {
        if (bundle == null) {
            return null;
        }
        String json = bundle.getString(key);
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JsonUtils.fromJson(json, type);
    }

    public static String getStringOrNull(Bundle bundle, String key) {
        if (bundle == null) {
            return null;
        }
        return bundle.getString(key);
    }

    private BundleUtil() {
    }

    public static Long getLongOrNull(Bundle bundle, String key) {
        if (bundle == null) {
            return null;
        }
        long result = bundle.getLong(key);
        if (result == 0L) {
            return null;
        }
        return result;
    }

    public static Integer getIntOrNull(Bundle bundle, String key) {
        if (bundle == null) {
            return null;
        }
        int result = bundle.getInt(key);
        if (result == 0L) {
            return null;
        }
        return result;
    }

    public static Double getDoubleOrNull(Bundle bundle, String key) {
        if (bundle == null) {
            return null;
        }
        double result = bundle.getDouble(key);
        if (result == 0.0) {
            return null;
        }
        return result;
    }
}
