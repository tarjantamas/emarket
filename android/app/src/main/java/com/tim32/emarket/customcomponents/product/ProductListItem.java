package com.tim32.emarket.customcomponents.product;

import com.tim32.emarket.apiclients.dto.Product;

public class ProductListItem {

    private final Product product;

    public ProductListItem(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return product.getName() + " (" + product.getId() + ")";
    }

    public Product getProduct() {
        return product;
    }
}
