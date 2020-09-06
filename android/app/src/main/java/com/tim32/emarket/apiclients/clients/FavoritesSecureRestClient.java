package com.tim32.emarket.apiclients.clients;

import com.tim32.emarket.apiclients.config.AuthInterceptor;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.Favorite;
import org.androidannotations.rest.spring.annotations.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

@Rest(rootUrl = RestApiUrl.root, converters = {MappingJackson2HttpMessageConverter.class}, interceptors = AuthInterceptor.class)
public interface FavoritesSecureRestClient {

    @Delete("/api/v1/favorite/shop/{shopId}")
    void remove(@Path Long shopId);

    @Put("/api/v1/favorites/shop/{shopId}")
    void add(@Path Long shopId);

    @Get("/api/v1/favorites/shops")
    List<Favorite> findAll();

    @Get("/api/v1/favorites/shops?updatedAtFromMS={syncedAt}")
    List<Favorite> findAll(@Path Long syncedAt);
}
