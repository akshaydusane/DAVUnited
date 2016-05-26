package com.davunited.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.os.Handler;

import com.davunited.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG="DAVUnited";
    private static int SPLASH_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash_screen);
        Log.d(TAG, "SplashScreenActivity");


        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, ParentScreenActivity.class));

            }
        },SPLASH_TIMEOUT);

    }
}
