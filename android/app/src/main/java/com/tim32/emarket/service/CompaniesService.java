package com.tim32.emarket.service;

import com.tim32.emarket.apiclients.clients.CompanyRestClient;
import com.tim32.emarket.apiclients.clients.CompanySecureRestClient;
import com.tim32.emarket.apiclients.config.AuthStore;
import com.tim32.emarket.apiclients.dto.Company;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.Collections;
import java.util.List;

@EBean
public class CompaniesService {

    @RestService
    CompanySecureRestClient companySecureRestClient;

    @RestService
    CompanyRestClient companyRestClient;

    @Bean
    AuthStore authStore;

    public List<Company> getCompanies() {
        try {
            return companySecureRestClient.findLoggedUsersCompanies();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public Company findById(long companyId) {
        try {
            return companyRestClient.findById(companyId);
        } catch (Exception e) {
            return null;
        }
    }
}
