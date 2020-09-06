package com.tim32.emarket.apiclients.clients;

import com.tim32.emarket.apiclients.config.AuthInterceptor;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.data.entity.Setting;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Put;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest(rootUrl = RestApiUrl.root, converters = {MappingJackson2HttpMessageConverter.class}, interceptors = AuthInterceptor.class)
public interface SettingsSecureRestClient {

    @Get("/api/v1/setting/my")
    Setting getUserSettings();

    @Put("/api/v1/setting/my")
    Setting updateUserSettings(@Body Setting seting);
}
