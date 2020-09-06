package com.tim32.emarket.apiclients.clients;

import com.tim32.emarket.apiclients.config.AuthInterceptor;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.User;
import com.tim32.emarket.apiclients.dto.UserUpdateRequest;
import org.androidannotations.rest.spring.annotations.*;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest(rootUrl = RestApiUrl.root, converters = {MappingJackson2HttpMessageConverter.class}, interceptors = AuthInterceptor.class)
public interface UserSecureRestClient {

    @Get("/api/v1/user/me")
    User getLoggedInUser();

    @Put("/api/v1/user/me")
    @Accept(MediaType.APPLICATION_JSON)
    User updateUser(@Body UserUpdateRequest userUpdateRequest);
}
