package com.example.petme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Credentials;
import android.os.Bundle;

import javax.net.ssl.SSLSessionContext;

public class AutoLoginActivity extends Activity {

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_login);
    }
}
