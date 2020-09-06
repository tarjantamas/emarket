package com.tim32.emarket.apiclients.clients;

import com.tim32.emarket.apiclients.config.AuthInterceptor;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.Shop;
import org.androidannotations.rest.spring.annotations.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

@Rest(rootUrl = RestApiUrl.root, converters = {MappingJackson2HttpMessageConverter.class}, interceptors = AuthInterceptor.class)
public interface ShopSecureRestClient {

    @Post("/api/v1/shops")
    Shop create(@Body Shop shop);

    @Get("/api/v1/shops/company/{id}")
    List<Shop> findByCompanyId(@Path long id);

    @Get("/api/v1/shop/{id}")
    Shop findById(@Path Long id);

    @Get("/api/v1/shops")
    List<Shop> findAll();

    @Put("/api/v1/shop/{id}")
    Shop update(@Path Long id, @Body Shop shop);
}
