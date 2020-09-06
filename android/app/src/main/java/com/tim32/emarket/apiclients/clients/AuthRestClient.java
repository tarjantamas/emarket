package com.tim32.emarket.apiclients.clients;

import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.LoginRequest;
import com.tim32.emarket.apiclients.dto.LoginResponse;
import com.tim32.emarket.apiclients.dto.RegisterUser;
import com.tim32.emarket.apiclients.dto.User;
import com.tim32.emarket.apiclients.dto.UserUpdateRequest;

import org.androidannotations.rest.spring.annotations.Accept;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Put;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest(rootUrl = RestApiUrl.root, converters = {MappingJackson2HttpMessageConverter.class})
public interface AuthRestClient {

    @Post("/login")
    @Accept(MediaType.APPLICATION_JSON)
    LoginResponse login(@Body LoginRequest loginRequest);

    @Post("/api/v1/users")
    User register(@Body RegisterUser registerUser);
}
