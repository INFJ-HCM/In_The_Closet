package com.example.android.butcher2;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Normalization {

    //private ArrayList<PointF> mDrawPoint = new ArrayList<PointF>();
    //Lshoulder = (x, y) = (mDrawPoint.get(2).x, mDrawPoint.get(2).y)      // i=2
    //Rshoulder = (x, y) = (mDrawPoint.get(5).x, mDrawPoint.get(5).y)      // i=5


    public void setNormalization(Context context, ArrayList<PointF> mDrawPoint) {

        double value = 0.0;
        int width, height;
        Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        width = size.x; // 실제 해상도 넓이 ex) 1200
        height = size.y; // 실제 해상도 높이 ex) 1920

        System.out.println("display width: " + width); // 갤탭 1200x1920
        System.out.println("display height: " + height);

        //System.out.println("Lshoulder : " + mDrawPoint.get(2).x + ", " + mDrawPoint.get(2).y);
       // System.out.println("Rshoulder : " + mDrawPoint.get(5).x + ", " + mDrawPoint.get(5).y);
       // System.out.println("shoulderWidth : " + (mDrawPoint.get(5).x - mDrawPoint.get(2).x));

        SimpleDateFormat day = new SimpleDateFormat("HH-mm-ss"); // 현재 시각
        Date date = new Date();
        System.out.println("현재시각 : " + day.format(date));


        //return value;
    }
}
