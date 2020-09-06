package com.tim32.emarket.apiclients.clients;

import com.tim32.emarket.apiclients.config.AuthInterceptor;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.ImageResponse;
import org.androidannotations.rest.spring.annotations.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Rest(rootUrl = RestApiUrl.root, converters = {MappingJackson2HttpMessageConverter.class, FormHttpMessageConverter.class}, interceptors = AuthInterceptor.class)
public interface ImageRestClient {

    @Get("/api/v1/images/company/{companyId}")
    List<ImageResponse> getCompanyImageIds(@Path Long companyId);


    //
    @Post("/api/v1/images/company/{companyId}")
    @RequiresHeader(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Long addCompanyImage(@Path Long companyId, @Part HttpEntity<MultiValueMap<String, Object>> data);
}
