package com.tim32.emarket.apiclients.clients;

import com.tim32.emarket.apiclients.config.AuthInterceptor;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.Company;
import com.tim32.emarket.apiclients.dto.Subscription;

import org.androidannotations.rest.spring.annotations.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

@Rest(rootUrl = RestApiUrl.root, converters = {MappingJackson2HttpMessageConverter.class}, interceptors = AuthInterceptor.class)
public interface SubscriptionsSecureRestClient {

    @Put("/api/v1/subscriptions/company/{id}")
    void subscribe(@Path Long id);

    @Get("/api/v1/subscriptions/companies")
    List<Subscription> findAllCompaniesSubscriptions();

    @Delete("/api/v1/subscription/company/{id}")
    void unsubscribe(@Path Long id);
}
