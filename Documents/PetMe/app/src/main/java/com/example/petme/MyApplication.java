package com.example.petme;

import android.app.Application;
import android.content.Context;

/**
 * Created by Maria on 30/07/2017.
 */

public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
