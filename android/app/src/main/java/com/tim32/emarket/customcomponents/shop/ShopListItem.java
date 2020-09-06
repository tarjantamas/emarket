package com.tim32.emarket.customcomponents.shop;

import com.tim32.emarket.apiclients.dto.Shop;

public class ShopListItem {

    private final Shop shop;

    public ShopListItem(Shop shop) {
        this.shop = shop;
    }

    @Override
    public String toString() {
        return shop.getName();
    }

    public Shop getShop() {
        return shop;
    }
}
