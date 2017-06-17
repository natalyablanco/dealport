package com.project.hackathon.dealport;

import android.app.Application;

import com.locuslabs.sdk.configuration.LocusLabs;

/**
 * Created by natalyablanco on 17.06.17.
 */

public class app extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LocusLabs.initialize(getApplicationContext(), "A1JVJTZQHDZ20Q");

    }
}
