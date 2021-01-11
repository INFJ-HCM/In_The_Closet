package com.example.android.butcher2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;


import java.time.Instant;

public class Loading extends Activity {
    public SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ImageView splashGif = (ImageView)findViewById(R.id.loading);
        DrawableImageViewTarget gifImage;
        gifImage = new DrawableImageViewTarget(splashGif);
        Glide.with(this).load(R.raw.loading).into(splashGif);
        startLoading();
    }
    //20210111박현아 최초실행 체크
    private void startLoading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

                if(isFirstRun) {
                    Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                    startActivity(intent);
                    prefs.edit().putBoolean("isFirstRun", false).apply();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);
    }


}