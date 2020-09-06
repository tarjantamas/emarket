package com.tim32.emarket.util;

import android.content.Context;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.dto.Product;

public final class ProductUtil {

    public static String getPriceString(Context context, Product product) {
        if (product.getPrice() == null) {
            return context.getResources().getString(R.string.price_not_set);
        }
        if (product.getUnit() == null) {
            return product.getPrice().toString();
        }
        return product.getPrice() + " " + product.getUnit();
    }

    private ProductUtil() {
    }
}
