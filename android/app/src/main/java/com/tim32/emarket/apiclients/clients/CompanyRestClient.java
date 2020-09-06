package com.tim32.emarket.apiclients.clients;

import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.Company;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest(rootUrl = RestApiUrl.root, converters = {MappingJackson2HttpMessageConverter.class})
public interface CompanyRestClient {

    @Get("/api/v1/company/{id}")
    Company findById(@Path Long id);
}
