package com.example.android.butcher2;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.android.butcher2.support.PermissionSupport;

import java.io.File;

//songhui20201128
public class StartActivity extends Activity {
    private PermissionSupport permission;

    final int PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        permissionCheck();

        //songhui20201101 로딩 액티비티 실행
        Intent intent = new Intent(this, Loading.class);

        ImageButton camerabutton = (ImageButton) findViewById(R.id.btn_camera);
        camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });

        //songhui20201128 지정폴더 열기 - 안드로이드10 q적용X
        ImageButton lookbook = (ImageButton)findViewById(R.id.btn_lookbook);
        lookbook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LookBookActivity.class);
                startActivity(intent);
            }
        });
    }
    // 권한 체크 songhui 20210110
    private void permissionCheck(){

        // SDK 23버전 이하 버전에서는 Permission이 필요하지 않습니다.
        if(Build.VERSION.SDK_INT >= 23){
            // 방금 전 만들었던 클래스 객체 생성
            permission = new PermissionSupport(this, this);

            // 권한 체크한 후에 리턴이 false로 들어온다면
            if (!permission.checkPermission()){
                // 권한 요청을 해줍니다.
                permission.requestPermission();
            }
        }
    }
    // Request Permission에 대한 결과 값을 받아올 수 있습니다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 여기서도 리턴이 false로 들어온다면 (사용자가 권한 허용을 거부하였다면)
        if(!permission.permissionResult(requestCode, permissions, grantResults)){
            // 다시 Permission 요청을 걸었습니다.
            permission.requestPermission();
        }
    }
}
