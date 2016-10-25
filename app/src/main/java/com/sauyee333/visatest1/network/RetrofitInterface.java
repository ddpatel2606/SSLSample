package com.sauyee333.visatest1.network;

import com.sauyee333.visatest1.model.response.HelloResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by sauyee on 25/10/16.
 */

public interface RetrofitInterface {

    @GET("vdp/helloworld")
    Call<HelloResponse> getHelloWorld(@Header("Accept") String Accept, @Header("x-pay-token") String xPayToken, @Query("apikey") String apikey);
}
