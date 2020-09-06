package com.ftn.market.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  private final UserDetailsService jwtUserDetailsService;

  private final JwtRequestFilter jwtRequestFilter;

  private static final String[] POST_WHITELIST = { "/login", "/api/v1/users", };

  private static final String[] GET_WHITELIST = { "/api/v1/image/{id}", "/api/v1/images/company/{companyId}",
      "/api/v1/images/shop/{shopId}", "/api/v1/images/product/{productId}", "/api/v1/products", "/api/v1/image/{id}",
      "/api/v1/products**", "/api/v1/shops**", "/api/v1/products/**", "/api/v1/company/{id}",
      "/api/v1/image/company/{id}", "/api/v1/image/shop/{id}", "/api/v1/image/product/{id}", "/api/v1/product/{id}",
      "/api/v1/shop/{id}", "/api/v1/shops/company/{companyId}" };

  @Autowired
  public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(final HttpSecurity httpSecurity) throws Exception {
    httpSecurity.cors();

    httpSecurity.csrf()
      .disable()
      .headers()
      .frameOptions()
      .disable()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    httpSecurity.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);

    httpSecurity.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll();

    httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, POST_WHITELIST).permitAll();

    httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, GET_WHITELIST).permitAll();

    httpSecurity.authorizeRequests().anyRequest().authenticated();
  }
}
