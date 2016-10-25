package com.sauyee333.visatest1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Button;
import android.widget.TextView;

import com.sauyee333.visatest1.model.response.HelloResponse;
import com.sauyee333.visatest1.network.NetworkCallback;
import com.sauyee333.visatest1.network.RetrofitClient;
import com.sauyee333.visatest1.utils.XPayTokenGenerator;

import java.security.SignatureException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.helloToken)
    Button helloToken;

    @Bind(R.id.results)
    TextView results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.helloToken)
    public void helloTokenClickHandler() {
        clearResults();
        getHelloWorld(false);
    }

    @OnClick(R.id.helloTwoWalSsl)
    public void helloTwoWaySslClickHandler() {
        clearResults();
        getHelloWorldTwoWaySsl(true);
    }

    private void clearResults() {
        setResults("");
    }

    private void setResults(String input) {
        if (results != null) {
            results.setText(input);
        }
    }

    private void getHelloWorld(boolean twoWaySsl) {
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
            RetrofitClient.getInstance(getApplicationContext(), twoWaySsl).getHelloWorldApi(new NetworkCallback<HelloResponse>() {
                @Override
                protected void onSuccess(HelloResponse response) {
                    results.setText(response.getTimestamp() + " " + response.getMessage());
                }

                @Override
                protected void onFailure(boolean isNetworkFailure, String responseDetails, String responseStatus) {
                }
            }, "application/json", token, apiKey);
        }
    }

    private void getHelloWorldTwoWaySsl(boolean twoWaySsl) {
        String username = getResources().getString(R.string.userid);
        String password = getResources().getString(R.string.password);
        String credentials = username + ":" + password;
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        if (!TextUtils.isEmpty(basic)) {
            RetrofitClient.getInstance(getApplicationContext(), twoWaySsl).getHelloWorldTwoWaySsl(new NetworkCallback<HelloResponse>() {
                @Override
                protected void onSuccess(HelloResponse response) {
                    results.setText(response.getMessage());
                }

                @Override
                protected void onFailure(boolean isNetworkFailure, String responseDetails, String responseStatus) {
                }
            }, null, basic);
        }
    }
}
