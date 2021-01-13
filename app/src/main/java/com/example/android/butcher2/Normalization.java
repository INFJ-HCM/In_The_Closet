package com.example.android.butcher2;

import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class Normalization {

    //Lshoulder = (x, y) = (mDrawPoint.get(2).x, mDrawPoint.get(2).y)      // i=2
    //Rshoulder = (x, y) = (mDrawPoint.get(5).x, mDrawPoint.get(5).y)      // i=5

    private PointF leftShoulder = null; // 왼쪽 어깨
    private PointF rightSholder = null; // 오른쪽 어깨
    private PointF neck = null;         // 넥

    private int displayWidth;           // 최대 화면 넓이 (해상도)
    private int displayHeight;       // 최대 화면 높이

    public ArrayList<Float> shoulderLength = new ArrayList<>();
    public ArrayList<PointF> neckList = new ArrayList<>();


    public void setPoint (PointF ls, PointF rs, PointF nk) {
        leftShoulder = ls;
        rightSholder = rs;
        neck = nk;

        if(checkArea()) { // 지정 범위 안에 있으면 리스트에 값 추가 시작.
            shoulderLength.add(rightSholder.x - leftShoulder.x);
            neckList.add(neck);
        }
    }

    public float sizeNormalization() {
        float len = 0;
        float avg = 0;
        if(shoulderLength.size() > 15) {
            for(int i=0; i<shoulderLength.size(); i++) {
                len += shoulderLength.get(i);
            }
            avg = len / shoulderLength.size();
        }
        shoulderLength.clear();
        return avg;
    }

    public PointF posNormalization () {
        float x = 0;
        float y = 0;
        float avgx = 0;
        float avgy = 0;

        PointF avg = new PointF();

        if(neckList.size() > 10) { // 20개 이상 쌓이면
            for(int i=0; i<neckList.size(); i++) {
                x += neckList.get(i).x;
                y += neckList.get(i).y;
            }
            avgx = x / neckList.size();
            avgy = y / neckList.size();
            Log.e("avgx", String.valueOf(avgx));
            Log.e("avgy", String.valueOf(avgy));
        }
        neckList.clear();

        Log.e("neckList 크기", String.valueOf(neckList.size()));

        avg.set(avgx, avgy);
        return avg;
    }

    public void setDisplay (int width, int height) {
        displayWidth = width;
        displayHeight = height;
    }

    public boolean checkArea () { // 옷이 그려지는 영역 지정 neck 값을 기준으로
        if(neck.x >= (displayWidth * 0.25) && neck.x <= ((double)displayWidth * 0.75) && neck.y >= ((double)displayHeight * 0.25) && neck.y <= ((double)displayHeight * 0.666)) { //1200x1920 기준 x: 300~900, y: 480~1278.72
            Log.e("CheckArea", "true");
            return true;
        }
        else {
            Log.e("CheckArea", "false");
            return false;
        }
    }
}
