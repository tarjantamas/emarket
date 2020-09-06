package com.tim32.emarket.util;

import java.util.List;

public final class RestParamUtil {

    public static String toCommaSeparatedStrings(List<String> strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string).append(",");
        }
        String result = stringBuilder.toString();
        return result.substring(0, result.length() - 1);
    }

    private RestParamUtil() {
    }
}
