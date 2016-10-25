package com.sauyee333.visatest1.network;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.sauyee333.visatest1.R;
import com.sauyee333.visatest1.model.response.HelloResponse;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sauyee on 25/10/16.
 */

public class RetrofitClient {
    private static final String BASE_URL = "https://sandbox.api.visa.com/";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public RetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .addNetworkInterceptor(new StethoInterceptor()).build();

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    private static class SingletonHolder {
        private static final RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getHelloWorldApi(final NetworkCallback<HelloResponse> networkCallback, String token, String accept, String apikey) {
        retrofit2.Call<HelloResponse> call = retrofitInterface.getHelloWorld(token, accept, apikey);
        call.enqueue(networkCallback);
    }
}
