package com.tim32.emarket.apiclients.clients;

import com.tim32.emarket.apiclients.config.AuthInterceptor;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.Company;
import com.tim32.emarket.apiclients.dto.RegisterCompany;
import org.androidannotations.rest.spring.annotations.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

@Rest(rootUrl = RestApiUrl.root, converters = {MappingJackson2HttpMessageConverter.class}, interceptors = AuthInterceptor.class)
public interface CompanySecureRestClient {

    @Post("/api/v1/companies")
    Company create(@Body RegisterCompany registerCompany);

    @Get("/api/v1/companies/my")
    List<Company> findLoggedUsersCompanies();

    @Get("/api/v1/company/{id}")
    Company findById(@Path Long id);
}
