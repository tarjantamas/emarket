package com.tim32.emarket.apiclients.clients;

import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.Shop;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

@Rest(rootUrl = RestApiUrl.root, converters = {MappingJackson2HttpMessageConverter.class})
public interface ShopRestClient {

    @Get("/api/v1/shop/{id}")
    Shop findById(@Path long id);

    @Get("/api/v1/shops?ids={commaSeparatedIds}")
    List<Shop> findByIds(@Path String commaSeparatedIds);
}
