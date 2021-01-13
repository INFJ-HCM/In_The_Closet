package com.example.android.butcher2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.opencv.android.FpsMeter;

public class DrawView extends View {
    private static final String TAG = "PCH :"; //PCH
    private int mRatioWidth  = 0;
    private int mRatioHeight  = 0;

    private ArrayList<PointF> mDrawPoint = new ArrayList<PointF>();
    private int mWidth       = 0;
    private int mHeight      = 0;
    private float mRatioX    = 0;
    private float mRatioY    = 0;
    private int mImgWidth    = 0;
    private int mImgHeight   = 0;

    private int clothFlag = -1;
    private Bitmap clothBitmap = null;

    private Bitmap captureview;
    private boolean captureFlag = false;

    Normalization normalization = new Normalization();

    private BitmapDrawable[] clothArray = {
            (BitmapDrawable)getResources().getDrawable(R.drawable.amimtm, null),
            (BitmapDrawable)getResources().getDrawable(R.drawable.shirt, null),
            (BitmapDrawable)getResources().getDrawable(R.drawable.dress, null),
            (BitmapDrawable)getResources().getDrawable(R.drawable.blackcoat, null),
            (BitmapDrawable)getResources().getDrawable(R.drawable.redcoat, null),
    };

    private int mColorArray[] = {   //15개 중 0~13만 사용 총 14개 사용
            getResources().getColor(R.color.color_top, null),
            getResources().getColor(R.color.color_neck, null),
            getResources().getColor(R.color.color_l_shoulder, null),
            getResources().getColor(R.color.color_l_elbow, null),
            getResources().getColor(R.color.color_l_wrist, null),
            getResources().getColor(R.color.color_r_shoulder, null),
            getResources().getColor(R.color.color_r_elbow, null),
            getResources().getColor(R.color.color_r_wrist, null),
            getResources().getColor(R.color.color_l_hip, null),
            getResources().getColor(R.color.color_l_knee, null),
            getResources().getColor(R.color.color_l_ankle, null),
            getResources().getColor(R.color.color_r_hip, null),
            getResources().getColor(R.color.color_r_knee, null),
            getResources().getColor(R.color.color_r_ankle, null),
            getResources().getColor(R.color.color_background, null)
    };

//    private WindowManager mWindowManager;
//    private DisplayMetrics metrics;

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float circleRadius = 0;
    private void initcircleRadius(){

        if ( circleRadius == 0 ){

            int mDenstity = 3 * (int) getContext().getResources().getDisplayMetrics().density;
            circleRadius = (float)mDenstity;
        }
    }

    private Paint mPaint = null;
    private void initmPaint(){

        if ( mPaint == null ){
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            mPaint.setStyle(Paint.Style.FILL);

            int mDenstity = 2 * (int) getContext().getResources().getDisplayMetrics().density;
            int mScaledDensity = 13 * (int) getContext().getResources().getDisplayMetrics().scaledDensity;
            mPaint.setStrokeWidth((float) mDenstity);
            mPaint.setTextSize((float) mScaledDensity);

        }
    }

    // 画像のサイズをセットするメソッド
    public void setImgSize(int width, int height) {
        mImgWidth  = width;
        mImgHeight = height;
        requestLayout();
    }

    public void setCaptureview (Bitmap bitmap) {
        captureview = bitmap;
        Log.e("captureView 1", ":  " + captureview);
        captureFlag = true;
    }

    public void setCaptureFlag (boolean flag) {
        captureFlag = flag;
    }

    public void setClothFlag (int flag) {
        clothFlag = flag;
    }

    public void chooseCloth() {
        Resources res = getResources(); //drawable에 있는 그림을 로딩해주는 역할이다.
        BitmapDrawable bd = null;
        Bitmap bit = null;
        int width, height;
        float baseNum;
        //Log.e("clothFlag", String.valueOf(clothFlag));
        switch (clothFlag) {
            case 0: // ami_mtm
                bit = clothArray[0].getBitmap();
                clothBitmap = bit;

                if(mDrawPoint.get(5).x > 0 || mDrawPoint.get(2).x > 0  || mDrawPoint.get(5).x - mDrawPoint.get(2).x > 0 ) {
                    // 리사이징
                    width = bit.getWidth();
                    height = bit.getHeight();
                    baseNum = reSize(width);
                    if (baseNum <= 0) return;
                    clothBitmap = Bitmap.createScaledBitmap(bit, (int) (width * baseNum), (int) (height * baseNum), true);
                }
                break;
            case 1: // shirt
                bit = clothArray[1].getBitmap();
                clothBitmap = bit;

                if(mDrawPoint.get(5).x > 0 || mDrawPoint.get(2).x > 0  || mDrawPoint.get(5).x - mDrawPoint.get(2).x > 0 ) {
                    // 리사이징
                    width = bit.getWidth();
                    height = bit.getHeight();
                    baseNum = reSize(width);
                    if (baseNum <= 0) return;
                    clothBitmap = Bitmap.createScaledBitmap(bit, (int) (width * baseNum), (int) (height * baseNum), true);
                }
                break;

            case 2: // dress
                bit = clothArray[2].getBitmap();
                clothBitmap = bit;

                if(mDrawPoint.get(5).x > 0 || mDrawPoint.get(2).x > 0  || mDrawPoint.get(5).x - mDrawPoint.get(2).x > 0 ) {
                    // 리사이징
                    width = bit.getWidth();
                    height = bit.getHeight();
                    baseNum = reSize(width);
                    if (baseNum <= 0) return;
                    clothBitmap = Bitmap.createScaledBitmap(bit, (int) (width * baseNum), (int) (height * baseNum), true);
                }
                break;

            case 3: // black coat
                bit = clothArray[3].getBitmap();
                clothBitmap = bit;

                if(mDrawPoint.get(5).x > 0 || mDrawPoint.get(2).x > 0  || mDrawPoint.get(5).x - mDrawPoint.get(2).x > 0 ) {
                    // 리사이징
                    width = bit.getWidth();
                    height = bit.getHeight();
                    baseNum = reSize(width);
                    if (baseNum <= 0) return;
                    clothBitmap = Bitmap.createScaledBitmap(bit, (int) (width * baseNum), (int) (height * baseNum), true);
                }
                break;

            case 4: // redcoat
                bit = clothArray[4].getBitmap();
                clothBitmap = bit;

                if(mDrawPoint.get(5).x > 0 || mDrawPoint.get(2).x > 0  || mDrawPoint.get(5).x - mDrawPoint.get(2).x > 0 ) {
                    // 리사이징
                    width = bit.getWidth();
                    height = bit.getHeight();
                    baseNum = reSize(width);
                    if (baseNum <= 0) return;
                    clothBitmap = Bitmap.createScaledBitmap(bit, (int) (width * baseNum), (int) (height * baseNum), true);
                }
                break;
        }
    }

    public float reSize(int clothWidth) {
        float shoulderWidth = mDrawPoint.get(5).x - mDrawPoint.get(2).x; // 어깨사이 넓이
        float baseImageSize, baseNum = 0;
        double n = 1.0;
        if(shoulderWidth > 0 ) {
            switch(clothFlag) {
                case 0: // ami mtm
                    n = 1.7;
                    break;
                case 1: // shirt
                    n = 1.9;
                    break;
                case 2: // dress
                    n = 2.3;
                    break;
                case 3: // black coat
                    n = 2.0;
                    break;
                case 4: // red coat
                    n = 2.0;
                    break;

            }
            baseImageSize = (float)(shoulderWidth * n); // 옷 사이즈 = 어깨사이 넓이 * n
            baseNum = baseImageSize / clothWidth; // 기준 사이즈 배수 = 기준 사이즈 / 옷 사이즈
            Log.e("배율", String.valueOf(baseNum));
        }

        return baseNum;
    }

    // 配列のデータをポイント型に変換して点群配列に追加する
    public void setDrawPoint(float[][] point, float ratio){

        mRatioX = (float)mImgWidth / (float)mWidth;
        mRatioY = (float)mImgHeight / (float)mHeight;

        mDrawPoint.clear();
        float tempX;
        float tempY;
        for(int i = 0; i <= 13; i++) {
            tempX = point[0][i] / ratio / mRatioX;
            tempY = point[1][i] /ratio / mRatioY;
            PointF point_temp = new PointF(tempX,tempY);
            mDrawPoint.add(point_temp);
        }
    }

    // アスペクト比を計算するメソッド
    public void setAspectRatio(int width, int height){
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be nagative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    //private FpsMeter fpsMeter = new FpsMeter();

    // 描写するメソッド
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initcircleRadius();
        initmPaint();

        /*
        문재식 Fps 그리기
        */
        //fpsMeter.measure();
       // fpsMeter.draw(canvas, 500, 200);

        if (mDrawPoint.isEmpty()) {
            return;
        }

//        PointF prePointF = null;
//        mPaint.setColor((int)0xff6fa8dc);
//        PointF p1 = mDrawPoint.get(1);

//        for (int i = 0; i < mDrawPoint.size(); i++) {
//            if (i == 1) continue;
//            switch (i) {    // Neck = (x, y) = (p1.x, p1.y)
//                case 0:
//                    canvas.drawLine(mDrawPoint.get(i).x, mDrawPoint.get(i).y, p1.x, p1.y, mPaint); // top~Neck
//                    //double Pythagoras = Math.sqrt(Math.pow(mDrawPoint.get(i).x - p1.x, 2) + Math.pow(mDrawPoint.get(i).y - p1.y, 2)); //PCH
//                    //Log.i("Pythagoras" ,  String.valueOf(Pythagoras)); //PCH
//                    break;
//                case 2:
//                    canvas.drawLine(p1.x, p1.y, mDrawPoint.get(i).x, mDrawPoint.get(i).y, mPaint); //Neck~Lshoulder
//                    break;
//                case 5:
//                    canvas.drawLine(p1.x, p1.y, mDrawPoint.get(i).x, mDrawPoint.get(i).y, mPaint); //Neck~Rshoulder
//                    break;
//                case 8:
//                    canvas.drawLine(p1.x, p1.y, mDrawPoint.get(i).x, mDrawPoint.get(i).y, mPaint); //Neck~LHip
//                    break;
//                case 11:
//                    canvas.drawLine(p1.x, p1.y, mDrawPoint.get(i).x, mDrawPoint.get(i).y, mPaint); //Neck~RHip
//                    break;
//                default:
//                    if (prePointF != null) {
//                        mPaint.setColor(0xff6fa8dc);
//                        canvas.drawLine(prePointF.x, prePointF.y, mDrawPoint.get(i).x, mDrawPoint.get(i).y, mPaint);
//                    }
//                    break;
//            }
//            prePointF = mDrawPoint.get(i);
//        }

//        for (int i = 0; i < mDrawPoint.size(); i++) {
//            mPaint.setColor((int)mColorArray[i]);
//            str = String.valueOf(mDrawPoint.get(i).x) + " + " + String.valueOf(mDrawPoint.get(i).y);
//            if (i == 1 || i == 2 || i == 5) {
//                Log.e(String.valueOf(i), str);
//            }
//
//            canvas.drawCircle(mDrawPoint.get(i).x, mDrawPoint.get(i).y,circleRadius, mPaint);
//        }

        if(captureFlag) { // 캡쳐 버튼을 누를 때만 그려라
            canvas.drawBitmap(captureview, 0, 0,null);
            captureFlag = false;
        }

        chooseCloth();

        switch (clothFlag) {

            case -1:
                return;

            case 0: // ami mtm
                canvas.drawBitmap(clothBitmap, mDrawPoint.get(1).x - (clothBitmap.getWidth()/2), mDrawPoint.get(1).y - 70,null);
                break;

            case 1: //shirt
                canvas.drawBitmap(clothBitmap, mDrawPoint.get(1).x - (clothBitmap.getWidth()/2), mDrawPoint.get(1).y - 40,null);
                break;
            case 2: // dress
                canvas.drawBitmap(clothBitmap, mDrawPoint.get(1).x - (clothBitmap.getWidth()/2), mDrawPoint.get(1).y - 100,null);
                break;

            case 3: // black coat
                canvas.drawBitmap(clothBitmap, mDrawPoint.get(1).x - (clothBitmap.getWidth()/2), mDrawPoint.get(1).y - 50,null);
                break;

            case 4: // red coat
                canvas.drawBitmap(clothBitmap, mDrawPoint.get(1).x - (clothBitmap.getWidth()/2), mDrawPoint.get(1).y - 50,null);
                break;
        }
    }

    // Neck = (x, y) = (p1.x, p1.y)
    // top = (x, y) = (mDrawPoint.get(0).x, mDrawPoint.get(0).y)            // i=0
    // Lshoulder = (x, y) = (mDrawPoint.get(2).x, mDrawPoint.get(2).y)      // i=2
    // LHip = (x, y) = (mDrawPoint.get(8).x, mDrawPoint.get(8).y)           // i=8
    // Rshoulder = (x, y) = (mDrawPoint.get(5).x, mDrawPoint.get(5).y)      // i=5
    // RHip = (x, y) = (mDrawPoint.get(11).x, mDrawPoint.get(11).y)         // i=11

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                mWidth  = width;
                mHeight = width * mRatioHeight / mRatioWidth;
            } else {
                mWidth  = height * mRatioWidth / mRatioHeight;
                mHeight = height;
            }
        }
        setMeasuredDimension(mWidth, mHeight);

        if ( width != 0 &&  height !=0 ){
            mWidth  = width;
            mHeight = height;
        }

    }

}
