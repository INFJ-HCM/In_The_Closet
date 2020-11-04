package com.example.android.butcher2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//songhui20201101
public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //로딩 액티비티 실행
        Intent intent=new Intent(this,Loading.class);

        Button camerabutton = (Button)findViewById(R.id.btn_camera);
        camerabutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CameraActivity.class);
                startActivity(intent);
            }
        });
    }
}