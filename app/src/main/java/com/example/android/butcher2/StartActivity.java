package com.example.android.butcher2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;

//songhui20201128
public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //songhui20201128 읽기권한체크
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

        //songhui20201101 로딩 액티비티 실행
        Intent intent=new Intent(this,Loading.class);

        ImageButton camerabutton = (ImageButton)findViewById(R.id.btn_camera);
        camerabutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CameraActivity.class);
                startActivity(intent);
            }
        });
        //songhui20201128 지정폴더 열기 - 안드로이드10 q적용X
        ImageButton lookbook = (ImageButton)findViewById(R.id.btn_lookbook);
        lookbook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Uri targetUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String targetDir = Environment.getExternalStorageDirectory().toString() + "/AnimationCapture";   // 특정 경로
                targetUri = targetUri.buildUpon().appendQueryParameter("bucketId", String.valueOf(targetDir.toLowerCase().hashCode())).build();
                Intent intent;
                intent = new Intent(Intent.ACTION_VIEW, targetUri);
                startActivity(intent);
            }
        });
    }
}
