package com.project.hackathon.dealport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.project.hackathon.dealport.api.DealPortApi;
import com.project.hackathon.dealport.api.DealPortService;
import com.project.hackathon.dealport.model.Airports;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by natalyablanco on 17.06.17.
 */

public class LoginActivity extends Activity {
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    private CallbackManager callbackManager;

    private String KEY_SITA = "3035d833bb6e531654a3cce03e6b1fde";
    private DealPortApi service;
    public String TAG = LoginActivity.class.getSimpleName();


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        service = DealPortService.createSitaService();

        Call<Airports> callGetAirports = service.getAirports();

        callGetAirports.enqueue(new Callback<Airports>() {

            @Override
            public void onResponse(Call<Airports> call, Response<Airports> response) {
                Log.d(TAG, "List airports " + response.body().getAirports());
            }

            @Override
            public void onFailure(Call<Airports> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
