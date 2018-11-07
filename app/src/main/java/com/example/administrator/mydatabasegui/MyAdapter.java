package com.example.administrator.mydatabasegui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.graphics.BitmapCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;



public class MyAdapter extends ArrayAdapter {
    private Context mContext;
    private ArrayList<MyItem> mMyItem;
    private int mLayout;
    ImageView imageView;

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
        imageView = (ImageView) rowView.findViewById(R.id.imageView);
        TextView fullnameTv = (TextView) rowView.findViewById(R.id.fullName);
        TextView ageTv = (TextView) rowView.findViewById(R.id.age);
        TextView weightTv = (TextView) rowView.findViewById(R.id.weight);
        TextView heightTv = (TextView) rowView.findViewById(R.id.height);

        setIvImg(mMyItem.get(position).getImgPath());
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

    public void setIvImg(String path) {

        int size = 50; //minimize  as much as you want
        if (path != null) {
            Bitmap bitmapOriginal = BitmapFactory.decodeFile(path);
            if (bitmapOriginal != null) {
                int bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmapOriginal);

                if (bitmapByteCount > 10000000) {
                    Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmapOriginal, bitmapOriginal.getWidth() / size, bitmapOriginal.getHeight() / size, true);
                    this.imageView.setImageBitmap(bitmapsimplesize);
                } else {
//                this.ivImg.setImageBitmap(bitmapOriginal);
                    this.imageView.setImageURI(Uri.fromFile(new File(path)));
                }
                bitmapOriginal.recycle();
            } else {
                // Toast.makeText(getContext(),"File "+ path+"Not Exist",Toast.LENGTH_SHORT).show();
                Drawable myDrawable = getContext().getResources().getDrawable(R.drawable.boy);//when use in adapter it have to use getContext() first

                this.imageView.setImageDrawable(myDrawable);

            }

        } else {
//        this.ivImg.setImageURI(Uri.fromFile(new File(path))); //work but out of mem error
            //       this.ivImg.setImageBitmap(bitmapsimplesize);
            Drawable myDrawable = getContext().getResources().getDrawable(R.drawable.boy);

            this.imageView.setImageDrawable(myDrawable);
        }
    }
}
