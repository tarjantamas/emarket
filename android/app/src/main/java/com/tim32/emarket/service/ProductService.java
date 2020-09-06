package com.tim32.emarket.service;

import com.tim32.emarket.apiclients.clients.ProductRestClient;
import com.tim32.emarket.apiclients.dto.Product;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;

@EBean
public class ProductService {

    @RestService
    ProductRestClient productRestClient;

    public List<Product> search(String searchTerm, Double lng, Double lat, Double radius) {
        try {
            return productRestClient.search(lat, lng, radius, searchTerm);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Product> searchByShopId(Long shop_id, String searchTerm) {
        try {
            return productRestClient.searchByShopId(shop_id, searchTerm);
        } catch (Exception e) {
            return null;
        }
    }
}
