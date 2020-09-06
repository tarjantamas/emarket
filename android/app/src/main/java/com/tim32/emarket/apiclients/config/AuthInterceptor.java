package com.tim32.emarket.apiclients.config;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@EBean
public class AuthInterceptor implements ClientHttpRequestInterceptor {

    @Bean
    AuthStore authStore;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String token = authStore.getToken();
        if (token == null) {
            return execution.execute(request, body);
        }

        HttpHeaders httpHeaders = request.getHeaders();
        HttpAuthentication auth = new HttpAuthentication() {
            @Override
            public String getHeaderValue() {
                return "Bearer " + token;
            }
        };

        httpHeaders.setAuthorization(auth);
        return execution.execute(request, body);
    }
}
