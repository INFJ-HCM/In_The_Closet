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
import android.view.View;
import android.widget.TextView;

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
    private LinearLayoutManager mLayoutManager; //LinearLayoutManager로 변경
    private ArrayList<MyData> myDataset;
    private ArrayList<Integer> imgID = new ArrayList<Integer>();

    TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookbook);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        empty = (TextView)findViewById(R.id.empty);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true); //recyvlerview 뒤에서부터 보여주기(1)
        mLayoutManager.setStackFromEnd(true); //recyvlerview 뒤에서부터 보여주기(1)
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


        empty.setVisibility(View.GONE);

        if (myDataset.size() <= 0) {
            System.out.println("TextView를 지우자.");
            empty.setVisibility(View.VISIBLE);
            //setContentView(R.layout.empty_lookbook);
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

        Uri externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        System.out.println(externalUri);

        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.MIME_TYPE
        };

        Cursor cursor = getContentResolver().query(externalUri, projection, null, null, null);

        if (cursor == null || !cursor.moveToFirst()) {
            Log.e("TAG", "cursor null or cursor is empty");
            return;
        }

        do {
            compareName = cursor.getString(1).substring(0,14); // #In_The_Closet만 자름
            Log.e("compareName", compareName);//지금여기까지 들어옴

            if(compareName.equals(token)) { // 맞는 이름인지 비교
                String contentUrl = externalUri.toString() + "/" + cursor.getString(0);
                Log.e("cursor.getString(1)", cursor.getString(1));

                try {
                    InputStream is = getContentResolver().openInputStream(Uri.parse(contentUrl));

                    if (is != null) {
                        //들어옴
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        //System.out.println(bitmap);
                        myDataset.add(new MyData(cursor.getString(1), bitmap));
                        //System.out.println(myDataset);
                        imgID.add(cursor.getInt(0)); // Add a bitmap
                        //System.out.println(imgID);
                        is.close();

                        //여기까지도됨
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
