package com.tim32.emarket.service;

import com.tim32.emarket.apiclients.clients.ImageRestClient;
import com.tim32.emarket.apiclients.config.AuthStore;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.ImageListResponse;
import com.tim32.emarket.apiclients.dto.ImageResponse;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@EBean
public class ImageService {

    @RestService
    ImageRestClient imageRestClient;

    @Bean
    AuthStore authStore;

    public ImageResponse addImage(Long id, MultiValueMap<String, Object> imageRequest, String endpoint) {
        RestTemplate restTemplate = new RestTemplate();

        String s = null;

        if (id == null) {
            s = (RestApiUrl.root + endpoint);
        } else {
            s = (RestApiUrl.root + endpoint + id);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + authStore.getToken());

        ImageResponse result = null;

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(imageRequest, headers);

        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ResponseEntity<ImageResponse> responseEntity = restTemplate.postForEntity(s, requestEntity, ImageResponse.class);

        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode == HttpStatus.OK) {
            result = responseEntity.getBody();
        }

        return result;
    }

    public ImageListResponse getImages(Long id, String endpoint) {
        RestTemplate restTemplate = new RestTemplate();
        String s = (RestApiUrl.root + endpoint + id);
        ImageListResponse result = null;
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        ResponseEntity<ImageListResponse> responseEntity = restTemplate.getForEntity(s, ImageListResponse.class);
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode == HttpStatus.OK) {
            result = responseEntity.getBody();
        }

        return result;
    }

    public void deleteImage(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        String s = (RestApiUrl.root + RestApiUrl.image + id);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authStore.getToken());
        HttpEntity entity = new HttpEntity(headers);
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        try {
            restTemplate.exchange(s, HttpMethod.DELETE, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageResponse addCompanyImage(Long companyId, MultiValueMap<String, Object> imageRequest) {
        return addImage(companyId, imageRequest, RestApiUrl.companyImages);
    }

    public ImageResponse addShopImage(Long shopId, MultiValueMap<String, Object> imageRequest) {
        return addImage(shopId, imageRequest, RestApiUrl.shopImages);
    }

    public ImageResponse addProductImage(Long productId, MultiValueMap<String, Object> imageRequest) {
        return addImage(productId, imageRequest, RestApiUrl.productImages);
    }

    public ImageResponse addProfileImage(MultiValueMap<String, Object> imageRequest) {
        return addImage(null, imageRequest, RestApiUrl.profileImages);
    }

    public ImageListResponse getCompanyImages(Long companyId) {
        return getImages(companyId, RestApiUrl.companyImages);
    }

    public ImageListResponse getShopImages(Long shopId) {
        return getImages(shopId, RestApiUrl.shopImages);
    }

    public ImageListResponse getProductImages(Long productId) {
        return getImages(productId, RestApiUrl.productImages);
    }

    public String getShopImageUrl(Long id) {
        return RestApiUrl.shopImage + "/" + id;
    }
}
