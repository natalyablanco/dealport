package com.project.hackathon.dealport;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.locuslabs.sdk.configuration.LocusLabs;
import com.locuslabs.sdk.maps.model.Airport;
import com.locuslabs.sdk.maps.model.AirportDatabase;
import com.locuslabs.sdk.maps.model.Floor;
import com.locuslabs.sdk.maps.model.Map;
import com.locuslabs.sdk.maps.model.Marker;
import com.locuslabs.sdk.maps.view.MapView;
import com.project.hackathon.dealport.api.DealPortApi;
import com.project.hackathon.dealport.api.DealPortService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {
    private AirportDatabase airportDatabase;
    private MapView mapView;
    private Context context;
    private Airport airport;
    private Map map;
    private DealPortApi service;
    private String KEY = "NAnEjbdUmp2pl8RCUskoe9FPP1NAxjhq7DBUIlID";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        LocusLabs.registerOnReadyListener(new LocusLabs.OnReadyListener() {
            @Override
            public void onReady() {
                airportDatabase = new AirportDatabase();
                loadAirportAndMap("lax");
            }
        });

        service = DealPortService.createService();

        Call<Object> callIdList = service.getAllIds(KEY);
        callIdList.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("TEST", response.body().toString());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("TEST", t.getMessage());

            }
        });

        Call<Object> callGetCategories = service.getAllCategories(KEY);
        callGetCategories.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("TEST", response.body().toString());

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("TEST", t.getMessage());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (airportDatabase != null) {
            airportDatabase.close();
            airportDatabase = null;
        }

        if (mapView != null) {
            mapView.close();
            mapView = null;
        }

    }

    @Override
    public void onBackPressed() {
        if (mapView == null || !mapView.onBackPressed()) {
            super.onBackPressed();
        }
    }

    private void loadAirportAndMap(String venueId) {
        final RelativeLayout rl = new RelativeLayout(context);

        AirportDatabase.OnLoadAirportAndMapListeners listeners =
                new AirportDatabase.OnLoadAirportAndMapListeners();
        listeners.loadedInitialViewListener = new AirportDatabase.OnLoadedInitialViewListener() {
            @Override
            public void onLoadedInitialView(View view) {
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null) parent.removeView(view);

                view.setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                rl.addView(view);
                setContentView(rl);
            }
        };
        listeners.loadCompletedListener = new AirportDatabase.OnLoadCompletedListener() {

            @Override
            public void onLoadCompleted(Airport _airport, Map _map, final MapView _mapView,
                    Floor floor, Marker marker) {
                mapView = _mapView;
                map = _map;
                //      addTwoFlightStatusMarkers();
            }
        };
        listeners.loadFailedListener = new AirportDatabase.OnLoadFailedListener() {
            @Override
            public void onLoadFailed(String exceptionMessage) {
            }
        };

        listeners.loadProgressListener = new AirportDatabase.OnLoadProgressListener() {
            @Override
            public void onLoadProgress(Integer percentComplete) {
            }
        };
        airportDatabase.loadAirportAndMap(venueId, null, listeners);
    }
}