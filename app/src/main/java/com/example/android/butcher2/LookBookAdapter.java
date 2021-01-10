package com.example.android.butcher2;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LookBookAdapter extends RecyclerView.Adapter<LookBookAdapter.ViewHolder> {
    private ArrayList<MyData> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.image); //lookbook image
            mTextView = (TextView)view.findViewById(R.id.textview); //lookbook text
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public LookBookAdapter(ArrayList<MyData> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LookBookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_lookbook, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { //view_lookbook.xml에 보이는 이미지&텍스트
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String textDate = mDataset.get(position).text.substring(15, 19)+"-"+mDataset.get(position).text.substring(19, 21)+"-"+mDataset.get(position).text.substring(21, 23)
                +"  "+mDataset.get(position).text.substring(23, 25)+":"+mDataset.get(position).text.substring(25, 27)+":"+mDataset.get(position).text.substring(27, 29);
        holder.mTextView.setText(textDate); //YYYY-MM-DD HH:MM:SS
        holder.mImageView.setImageBitmap(mDataset.get(position).img);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

class MyData{
    public String text;
    public Bitmap img;
    public MyData(String text, Bitmap img){
        this.text = text;
        this.img = img;
    }

}
