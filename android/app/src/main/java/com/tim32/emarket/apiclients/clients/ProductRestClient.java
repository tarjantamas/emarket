package com.tim32.emarket.apiclients.clients;

import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.Product;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

@Rest(rootUrl = RestApiUrl.root, converters = {MappingJackson2HttpMessageConverter.class})
public interface ProductRestClient {

    @Get("/api/v1/products?lat={lat}&lng={lng}&rad={radius}&term={term}")
    List<Product> search(@Path Double lat, @Path Double lng, @Path Double radius, @Path String term);

    @Get("/api/v1/products?shop_id={shop_id}&term={term}&sort=price,desc")
    List<Product> searchByShopId(@Path Long shop_id, @Path String term);

    @Get("/api/v1/products/shop/{id}")
    List<Product> findProductsByShopId(@Path long id);

    @Get("/api/v1/products/company/{companyId}")
    List<Product> findByCompanyId(@Path long companyId);

    @Get("/api/v1/product/{id}")
    Product findById(@Path String id);
}
