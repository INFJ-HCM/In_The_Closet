package com.example.android.butcher2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TutorialActivity extends Activity implements View.OnClickListener {
    private TextView textHelp;
    private RelativeLayout rlOverlay;
    private LinearLayout llOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        initControls();
    }
    private void initControls() {
        textHelp = (TextView) findViewById(R.id.textHelp);
        textHelp.setOnClickListener(this);
        rlOverlay = (RelativeLayout) findViewById(R.id.rlOverlay);
        llOverlay = (LinearLayout) findViewById(R.id.llOverlay);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textHelp:
                if (textHelp.getText().toString().equals("Next>>")) {
//                    llOverlay.setVisibility(View.GONE);
                    llOverlay.setBackgroundResource(R.drawable.tutorial2);
                    textHelp.setText("Got It>>");
                } else {
                    rlOverlay.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(),StartActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }
}