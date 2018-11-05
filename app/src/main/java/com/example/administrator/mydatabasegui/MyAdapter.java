package com.example.administrator.mydatabasegui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter {
    private Context mContext;
    private ArrayList<MyItem> mMyItem;
    private int mLayout;

    public MyAdapter(Context context, int layout,ArrayList<MyItem> objects) {
        super(context, layout, objects);
        mContext = context;
        mMyItem = objects;
        mLayout = layout;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = layoutInflater.inflate(mLayout,parent,false);
        }
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        TextView fullnameTv = (TextView) rowView.findViewById(R.id.fullName);
        TextView ageTv = (TextView) rowView.findViewById(R.id.age);
        TextView weightTv = (TextView) rowView.findViewById(R.id.weight);
        TextView heightTv = (TextView) rowView.findViewById(R.id.height);

        Bitmap Bitmap = mMyItem.get(position).getmBitmap();
        imageView.setImageBitmap(Bitmap);
        String fullname = mMyItem.get(position).getFullname();
        fullnameTv.setText(fullname);
        int age = mMyItem.get(position).getAge();
        ageTv.setText("Age "+String.valueOf(age));
        int weight = mMyItem.get(position).getWeight();
        weightTv.setText("Weight "+String.valueOf(weight));
        int height = mMyItem.get(position).getHeight();
        heightTv.setText("Height "+String.valueOf(height));

        return rowView;
    }
}
