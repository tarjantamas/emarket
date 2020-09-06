package com.tim32.emarket.apiclients.clients;

import com.tim32.emarket.apiclients.config.AuthInterceptor;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.Product;
import org.androidannotations.rest.spring.annotations.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest(rootUrl = RestApiUrl.root, converters = {MappingJackson2HttpMessageConverter.class}, interceptors = AuthInterceptor.class)
public interface ProductSecureRestClient {

    @Post("/api/v1/products")
    Product create(@Body Product product);

    @Get("/api/v1/product/{id}")
    Product findById(@Path Long id);

    @Put("/api/v1/product/{id}")
    void update(@Path Long id, @Body Product product);
}
