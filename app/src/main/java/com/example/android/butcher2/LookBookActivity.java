package com.example.android.butcher2;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class LookBookActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyData> myDataset;
    private ArrayList<Integer> imgID = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookbook);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myDataset = new ArrayList<>();
        mAdapter = new LookBookAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        readFile();

        Iterator<Integer> idIterator = imgID.iterator();
        while (idIterator.hasNext()) {
            System.out.println(idIterator.next());
        }

//        myDataset.add(new MyData("#InsideOut", R.drawable.camera_button2));
//        myDataset.add(new MyData("#Mini", R.drawable.cloth));
//        myDataset.add(new MyData("#ToyStroy", R.drawable.short_sleve));
    }


    private void readFile() {
        //String path = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath();

        //Uri externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String token = "#In_The_Closet";
        String compareName = "";

        Uri externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; //현아가 INTERNAL로 바꿔봄 //재식?ㅎ 그냥 불러봄^^
        Log.e("externalUri : ", externalUri.toString()); //externaluri : content://media/external/images/media
        // [START] chohui Park 20210110
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);


        //[출처] #모각코 #4일차 #학습결과|작성자 아롱다롱
        // [END] chohui Park 20210110
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.MIME_TYPE
        };

        Cursor cursor = getContentResolver().query(externalUri, projection, null, null, null);

        if (cursor == null || !cursor.moveToFirst()) {
            Log.e("TAG", "cursor null or cursor is empty"); //사진이 없을 때
            return;
        }

        do {
            compareName = cursor.getString(1).substring(0, 14); // #In_The_Closet만 자름 //맨 뒤에 _는 어디갔어 재식?
            Log.e("compareName", compareName); //20190305_19222 //가운데 _가 왜 붙어 재식? 이 친구가 가져오는 사진들은 어디에 저장되어있는거야 재식?
            if(compareName.equals(token)) { // 맞는 이름인지 비교 //근데 compareName이 #In_The_Closet이 아니야 재식?
                String contentUrl = externalUri.toString() + "/" + cursor.getString(0);
                Log.e("cursor.getString(1)", cursor.getString(1));

                try {
                    InputStream is = getContentResolver().openInputStream(Uri.parse(contentUrl));

                    if (is != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        myDataset.add(new MyData(cursor.getString(1), bitmap));
                        imgID.add(cursor.getInt(0)); // Add a bitmap
                        is.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } while (cursor.moveToNext());
    }
}
