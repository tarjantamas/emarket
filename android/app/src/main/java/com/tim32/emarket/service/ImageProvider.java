package com.tim32.emarket.service;

import org.androidannotations.annotations.EBean;

@EBean
public class ImageProvider {

    public String getProductImage(Long productId) {
        return "https://cdn.shopify.com/s/files/1/2679/6864/products/Produce_-_Carrots_A_Square.png?v=1578873933";
    }
}
