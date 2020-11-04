package org.opencv.android;

import java.text.DecimalFormat;

import org.opencv.core.Core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class FpsMeter {
    private static final String TAG = "FpsMeter";
    private static final int STEP = 20;
    private static final DecimalFormat FPS_FORMAT = new DecimalFormat("0.00");

    private int mFramesCouner;
    private double mFrequency;
    private long mprevFrameTime;
    private String mStrfps;
    Paint mPaint;
    boolean mIsInitialized = false;
    int mWidth = 0;
    int mHeight = 0;

    public void init() {
        mFramesCouner = 0;
        mFrequency = Core.getTickFrequency();
        mprevFrameTime = Core.getTickCount();
        mStrfps = "";

        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(80);
        Log.e("여기다", "알아서 들어오지?");
    }

    public void measure() {
        if (!mIsInitialized) {
            init();
            mIsInitialized = true;
            Log.e("여기는", "몇번 오냐");
        } else {
            Log.e("1","1");
            mFramesCouner++;
            Log.e("2","2");
            if (mFramesCouner % STEP == 0) {
                Log.e("3","3");

                long time = Core.getTickCount();
                double fps = STEP * mFrequency / (time - mprevFrameTime);

                String tmp = String.valueOf(time);
                String tmp2 = String.valueOf(fps);

                Log.e("time", tmp);
                Log.e("fps", tmp2);

                mprevFrameTime = time;
                if (mWidth != 0 && mHeight != 0)
                    mStrfps = FPS_FORMAT.format(fps) + " FPS@" + Integer.valueOf(mWidth) + "x" + Integer.valueOf(mHeight);
                else
                    mStrfps = FPS_FORMAT.format(fps) + " FPS";
//                Log.i(TAG, mStrfps);
                Log.e("Hello", mStrfps);
            }
        }
        //Log.e("hello", mStrfps);
    }

    public void setResolution(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public void draw(Canvas canvas, float offsetx, float offsety) {
        Log.e(TAG, mStrfps);
        canvas.drawText(mStrfps, offsetx, offsety, mPaint);
    }

    public String getFps()
    {
        Log.e(TAG, mStrfps);
        return mStrfps;
    }
}
