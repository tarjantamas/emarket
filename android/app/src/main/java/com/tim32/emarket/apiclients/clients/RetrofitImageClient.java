package com.tim32.emarket.apiclients.clients;

import org.androidannotations.rest.spring.annotations.Accept;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.util.MultiValueMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface RetrofitImageClient {

    @Multipart
    @POST("/api/v1/images/company/{companyId}")
    Call<ResponseBody> addCompanyImage(@Part("description") RequestBody description, @Part MultipartBody.Part body, @Path("companyId") Long companyId);

}
