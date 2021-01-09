package com.example.android.butcher2;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.io.File;

public class CustomGalleryAdapter extends BaseAdapter {
    int CustomGalleryItemBg;
    String mBasePath;
    Context mContext;
    String[] mImgs;
    Bitmap bm;
    DataSetObservable mDataSetObservable = new DataSetObservable(); // DataSetObservable(DataSetObserver)의 생성

    public String TAG = "Gallery Adapter Example :: ";

    public CustomGalleryAdapter(Context context, String basepath){
        this.mContext = context;
        this.mBasePath = basepath;

        File file = new File(mBasePath);
        if(!file.exists()){
            if(!file.mkdirs()){
                Log.d(TAG, "failed to create directory");
            }
        }
        mImgs = file.list();

        TypedArray array = mContext.obtainStyledAttributes(R.styleable.GalleryTheme);
        CustomGalleryItemBg = array.getResourceId(R.styleable.GalleryTheme_android_galleryItemBackground, 0);
        array.recycle();
    }

    @Override
    public int getCount() {
        File dir = new File(mBasePath);
        mImgs = dir.list();
        return mImgs.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    // Adapter 내 Item에서 직접 주소를 받아오도록 method 추가.
    // 이전에는 MainActivity와 주소 및 position이 달라 비정상적인 앱의 종료가 발생한 것으로 보인다
    public String getItemPath(int position){
        String path = mBasePath + File.separator + mImgs[position];
        return path;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Override this method according to your need
    @Override
    public View getView(int index, View view, ViewGroup viewGroup)

    {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(mContext);

        File dir = new File(mBasePath);
        mImgs = dir.list();
        bm = BitmapFactory.decodeFile(mBasePath+ File.separator +mImgs[index]);

        Bitmap bm2 = ThumbnailUtils.extractThumbnail(bm, 300, 300);
        i.setLayoutParams(new Gallery.LayoutParams(300, 300));
        i.setImageBitmap(bm2);
        i.setVisibility(ImageView.VISIBLE);

        i.setBackgroundResource(CustomGalleryItemBg);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);

        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
        return i;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer){ // DataSetObserver의 등록(연결)
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer){ // DataSetObserver의 해제
        mDataSetObservable.unregisterObserver(observer);
    }

    @Override
    public void notifyDataSetChanged(){ // 위에서 연결된 DataSetObserver를 통한 변경 확인
        mDataSetObservable.notifyChanged();
    }
}