package com.example.android.butcher2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

//songhui20201128
public class StartActivity extends Activity {

    final int PERMISSIONS_REQUEST_CODE = 1;

    String LogTT="[STT]";//LOG타이틀
    //음성 인식용
    Intent SttIntent;
    SpeechRecognizer mRecognizer;
    //음성 출력용
    TextToSpeech tts;

    // 화면 처리용
    Button btnSttStart; //사용안함
    EditText txtInMsg;
    EditText txtSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //음성인식
        SttIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getApplicationContext().getPackageName());
        SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");//한국어 사용
        mRecognizer=SpeechRecognizer.createSpeechRecognizer(StartActivity.this);
        mRecognizer.setRecognitionListener(listener);

        //음성출력 생성, 리스너 초기화
        tts=new TextToSpeech(StartActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=android.speech.tts.TextToSpeech.ERROR){
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
        //버튼설정
        btnSttStart=(Button)findViewById(R.id.btn_stt_start);
        btnSttStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("음성인식 시작!");
                if(ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(StartActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},1);
                    //권한을 허용하지 않는 경우
                }else{
                    //권한을 허용한 경우
                    try {
                        mRecognizer.startListening(SttIntent);
                    }catch (SecurityException e){e.printStackTrace();}
                }
            }
        });
        txtInMsg=(EditText)findViewById(R.id.txtInMsg);
        txtSystem=(EditText)findViewById(R.id.txtSystem);
        //어플이 실행되면 자동으로 1초뒤에 음성 인식 시작
//        new android.os.Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                txtSystem.setText("어플 실행됨--자동 실행-----------"+"\r\n"+txtSystem.getText());
//                tts.speak("시작",TextToSpeech.QUEUE_FLUSH,null, "myUtteranceID");
//                btnSttStart.performClick();
//            }
//        },1000);//바로 실행을 원하지 않으면 지워주시면 됩니다

        /**==========================================================================*/


        //songhui20201128 읽기권한체크
        requestPermission();

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            } else {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        1);
//            }
//        }

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
                Intent intent = new Intent(getApplicationContext(),LookBookActivity.class);
                startActivity(intent);

//                Uri targetUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                String targetDir = Environment.getExternalStorageDirectory().toString() + "/In the Closet";   // 특정 경로
//                targetUri = targetUri.buildUpon().appendQueryParameter("bucketId", String.valueOf(targetDir.toLowerCase().hashCode())).build();
//                Intent intent;
//                intent = new Intent(Intent.ACTION_VIEW, targetUri);
//                startActivity(intent);
            }
        });
    }

    private void requestPermission() {
        boolean shouldProviceRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);//사용자가 이전에 거절한적이 있어도 true 반환

        if (shouldProviceRationale) {
            //앱에 필요한 권한이 없어서 권한 요청
            ActivityCompat.requestPermissions(StartActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(StartActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            //권한있을때.
            //오레오부터 꼭 권한체크내에서 파일 만들어줘야함
            makeDir();
        }
    }

    public void makeDir() {
        String path = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY).getPath();
        Log.e("path", path);
        String directoryName = "abc";
        final File myDir = new File(path + "/" + directoryName);
        if (!myDir.exists()) {
            myDir.mkdir();
        } else {
            System.out.println("file: " + path + "/" + directoryName +"already exists");
        }
    }

    private RecognitionListener listener=new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            txtSystem.setText("");
            txtSystem.setText("onReadyForSpeech..........."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onBeginningOfSpeech() {
            txtSystem.setText("");
            txtSystem.setText("지금부터 말을 해주세요..........."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onRmsChanged(float v) {
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            txtSystem.setText("");
            txtSystem.setText("onBufferReceived..........."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onEndOfSpeech() {
            txtSystem.setText("");
            txtSystem.setText("onEndOfSpeech..........."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onError(int error) {
            txtSystem.setText("");
            txtSystem.setText("에러 발생..........."+"\r\n"+txtSystem.getText());
            // 천천히 다시 말해라
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    txtSystem.setText("오디오 에러"+"\r\n"+txtSystem.getText());
                    break;

                case SpeechRecognizer.ERROR_CLIENT:
                    txtSystem.setText("클라이언트 에러"+"\r\n"+txtSystem.getText());
                    break;

                 case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                     txtSystem.setText("퍼미션 없음"+"\r\n"+txtSystem.getText());
                     break;

                 case SpeechRecognizer.ERROR_NETWORK:
                     txtSystem.setText("네트워크 에러"+"\r\n"+txtSystem.getText());
                     break;

                 case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                     txtSystem.setText("네트워크 타임아웃"+"\r\n"+txtSystem.getText());
                     break;

                case SpeechRecognizer.ERROR_NO_MATCH:
                    txtSystem.setText("찾을 수 없음"+"\r\n"+txtSystem.getText());
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    txtSystem.setText("RECOGNIZER가 바쁨"+"\r\n"+txtSystem.getText());
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    txtSystem.setText("서버가 이상함"+"\r\n"+txtSystem.getText());
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    txtSystem.setText("말하는 시간초과"+"\r\n"+txtSystem.getText());
                    break;
                default:
                    txtSystem.setText("알 수 없는 오류"+"\r\n"+txtSystem.getText());
                    break;
            }
        }

        @Override
        public void onResults(Bundle results) {
            String key= "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult =results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            txtInMsg.setText("");
            txtInMsg.setText(rs[0]+"\r\n"+txtInMsg.getText());
            FuncVoiceOrderCheck(rs[0]);
            mRecognizer.startListening(SttIntent);

        }

        @Override
        public void onPartialResults(Bundle bundle) {
            txtSystem.setText("onPartialResults..........."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
            txtSystem.setText("onEvent..........."+"\r\n"+txtSystem.getText());
        }
    };

    //입력된 음성 메세지 확인 후 동작 처리
    private void FuncVoiceOrderCheck(String VoiceMsg){
        if(VoiceMsg.length()<1)return;

        VoiceMsg=VoiceMsg.replace(" ","");//공백제거

        if(VoiceMsg.indexOf("카카오톡")>-1 || VoiceMsg.indexOf("카톡")>-1){
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.kakao.talk");
            startActivity(launchIntent);
            onDestroy();
        }//카카오톡 어플로 이동

        if(VoiceMsg.indexOf("안녕")>-1){
            FuncVoiceOut("안녕하세요");//전등을 끕니다 라는 음성 출력
        }
    }



    //음성 메세지 출력용
    private void FuncVoiceOut(String OutMsg){
        if(OutMsg.length()<1)return;
        if(!tts.isSpeaking()) {
            Log.e("1","1");
            tts.setPitch(1.0f);//목소리 톤1.0
            tts.setSpeechRate(1.0f);//목소리 속도
            tts.speak(OutMsg,TextToSpeech.QUEUE_FLUSH,null, "id1");
        }
        Log.e("2","2");
        //어플이 종료할때는 완전히 제거
    }

    //카톡으로 이동을 했는데 음성인식 어플이 종료되지 않아 계속 실행되는 경우를 막기위해 어플 종료 함수
    @Override
    protected void onStop() {
        super.onStop();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts!=null){
            tts.stop();
            tts.shutdown();
            tts=null;
        }
        if(mRecognizer!=null){
            mRecognizer.destroy();
            mRecognizer.cancel();
            mRecognizer=null;
        }
    }


}
