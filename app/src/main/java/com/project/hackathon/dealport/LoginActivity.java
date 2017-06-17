package com.project.hackathon.dealport;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.project.hackathon.dealport.api.DealPortApi;
import com.project.hackathon.dealport.api.DealPortService;
import com.project.hackathon.dealport.model.Airports;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by natalyablanco on 17.06.17.
 */

public class LoginActivity extends Activity implements
        AdapterView.OnItemSelectedListener {
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.login_button)
    LoginButton loginButton;

    @BindView(R.id.spinner1)
    Spinner airportSpinner;

    private CallbackManager callbackManager;

    private DealPortApi service;
    public String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.bgVideoView)
    public VideoView videoView;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg_video);

        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        connectService();

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

    private void setAirportSpinner(List<String> airportList) {
        airportSpinner.setOnItemSelectedListener(this);

        Log.d(TAG,airportList.size() + " amount airports");
        ArrayAdapter adapter = new ArrayAdapter<>(LoginActivity.this,
                android.R.layout.simple_spinner_item, airportList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        airportSpinner.setAdapter(adapter);
    }

    private void connectService() {
        service = DealPortService.createSitaService();

        Call<Airports> callGetAirports = service.getAirports();

        callGetAirports.enqueue(new Callback<Airports>() {

            @Override
            public void onResponse(Call<Airports> call, Response<Airports> response) {
                Log.d(TAG, "List airports " + response.body().getAirports().size());
                setAirportSpinner(response.body().getAirportsName());
            }

            @Override
            public void onFailure(Call<Airports> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
