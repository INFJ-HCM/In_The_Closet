/* Copyright 2017 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.example.android.butcher2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraGLSurfaceView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/** Main {@code Activity} class for the Camera app. */
public class CameraActivity extends Activity{
  public static boolean isOpenCVInit = false;

  /** Tag for the {@link Log}. */
  private static final String TAG = "butcher2";

  public static void init() {
        System.loadLibrary("opencv_java3");
  }
  private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

      @Override
      public void onManagerConnected(int status) {
          switch (status) {
              case LoaderCallbackInterface.SUCCESS:
                  {
                      isOpenCVInit = true;
                  } break;

               default:
                  {
                      super.onManagerConnected(status);
                  } break;
          }
        }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);

    if (null == savedInstanceState) {
        //1. 들어감
        getFragmentManager()
          .beginTransaction()
          .replace(R.id.container, Camera2BasicFragment.newInstance())//2
          .commit();
    }

      Button capture = findViewById(R.id.capture);
      capture.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            //전체화면
              View rootView = getWindow().getDecorView(); //현재 화면 전체를 객체화한다.
              Toast.makeText(getApplicationContext(),"저장 했습니다.",Toast.LENGTH_SHORT).show();
              File screenShot = ScreenShot(rootView);
              if(screenShot!=null){
                //갤러리에 추가
                  sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
              }

          }
      });

  }

  @Override
  public void onResume()
  {
      super.onResume();
      if (!OpenCVLoader.initDebug()) {
          OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
      } else {
          mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
      }
  }

//20201125songhui 스크린샷
public File ScreenShot(View view){
    view.setDrawingCacheEnabled(true); //화면에 뿌릴때 캐시를 사용하게 한다

    Bitmap screenBitmap = view.getDrawingCache(); //캐시를 비트맵으로 변환

    String filename; //저장될 파일명
    filename = "sdfgjh.png";
    File file = new File(Environment.getExternalStorageDirectory()+"/Pictures", filename); //Pictures폴더 screenshot.png 파일

    FileOutputStream os = null;
    try{
        os = new FileOutputStream(file);
        screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os); //비트맵을 PNG파일로 변환
        os.close();
    }catch (IOException e){
        e.printStackTrace();
        return null;
        //[출처] <안드로이드 스튜디오> 현재화면을 캡쳐하여 저장해보자|작성자 쿠쿠
    }
    view.setDrawingCacheEnabled(false);
    return file;
}
    //[출처] <안드로이드 스튜디오> 현재화면을 캡쳐하여 저장해보자|작성자 쿠쿠

}
