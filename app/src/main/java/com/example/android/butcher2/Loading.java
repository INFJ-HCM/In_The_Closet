package com.example.android.butcher2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;


import java.time.Instant;

public class Loading extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ImageView splashGif = (ImageView)findViewById(R.id.loading);
        DrawableImageViewTarget gifImage;
        gifImage = new DrawableImageViewTarget(splashGif);
        Glide.with(this).load(R.raw.loading).into(splashGif);
        startLoading();
    }
    private void startLoading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),TutorialActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}