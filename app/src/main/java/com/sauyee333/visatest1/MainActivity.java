package com.sauyee333.visatest1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;

import com.sauyee333.visatest1.model.response.HelloResponse;
import com.sauyee333.visatest1.network.NetworkCallback;
import com.sauyee333.visatest1.network.RetrofitClient;
import com.sauyee333.visatest1.utils.XPayTokenGenerator;

import java.security.SignatureException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.hello)
    Button helloBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.hello)
    public void helloClickHandler() {
        getHelloWorld();
    }

    private void getHelloWorld() {
        String endPoint = "helloworld";
        String token = null;
        String apiKey = getResources().getString(R.string.apiKey);
        String sharedSecret = getResources().getString(R.string.sharedSecret);
        try {
            token = XPayTokenGenerator.generateXpaytoken(endPoint, "apikey=" + apiKey, "", sharedSecret);
        } catch (SignatureException se) {
            se.printStackTrace();
        }
        if (!TextUtils.isEmpty(token)) {
            RetrofitClient.getInstance().getHelloWorldApi(new NetworkCallback<HelloResponse>() {
                @Override
                protected void onSuccess(HelloResponse response) {
                    helloBtn.setText(response.getMessage());
                }

                @Override
                protected void onFailure(boolean isNetworkFailure, String responseDetails, String responseStatus) {
                }
            }, "application/json", token, apiKey);
        }
    }
}
