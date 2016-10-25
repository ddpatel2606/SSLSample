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
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
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

    public RetrofitClient(Context context, boolean twoWaySsl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.addInterceptor(interceptor);
        builder.addNetworkInterceptor(new StethoInterceptor());
        if (twoWaySsl) {
            SSLContext sslContext = getSSLConfig(context);
            if (sslContext != null) {
                builder.sslSocketFactory(sslContext.getSocketFactory());
            }
        }
        OkHttpClient client = builder.build();

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = null;

        private static RetrofitClient getINSTANCE(Context context, boolean twoWaySsl) {
            INSTANCE = new RetrofitClient(context, twoWaySsl);
            return INSTANCE;
        }
    }

    private SSLContext getSSLConfig(Context context) {
        SSLContext sslContext = null;
        CertificateFactory cf = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            InputStream keyStoreStream = context.getResources().openRawResource(R.raw.sample_cert);
            String pfxPassword = context.getResources().getString(R.string.certPassword);
            keyStore.load(keyStoreStream, pfxPassword.toCharArray());
            keyManagerFactory.init(keyStore, pfxPassword.toCharArray());

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);

            if (trustManagerFactory != null) {
                trustManagerFactory.init(keyStore);
            }
            sslContext = SSLContext.getInstance("TLS");
            if (sslContext != null && trustManagerFactory != null && trustManagerFactory.getTrustManagers() != null) {
                sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            }
        } catch (CertificateException ce) {
            ce.printStackTrace();
        } catch (KeyStoreException ke) {
            ke.printStackTrace();
        } catch (NoSuchAlgorithmException ne) {
            ne.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException me) {
            me.printStackTrace();
        } catch (UnrecoverableKeyException ue) {
            ue.printStackTrace();
        }
        return sslContext;
    }

    public static RetrofitClient getInstance(Context context, boolean twoWaySsl) {
        return SingletonHolder.getINSTANCE(context, twoWaySsl);
    }

    public void getHelloWorldApi(final NetworkCallback<HelloResponse> networkCallback, String token, String accept, String apikey) {
        retrofit2.Call<HelloResponse> call = retrofitInterface.getHelloWorld(token, accept, apikey);
        call.enqueue(networkCallback);
    }

    public void getHelloWorldTwoWaySsl(final NetworkCallback<HelloResponse> networkCallback, String accept, String token) {
        retrofit2.Call<HelloResponse> call = retrofitInterface.getHelloWorldTwoWaySsl(accept, token);
        call.enqueue(networkCallback);
    }
}
