package com.example.administrator.mydatabasegui;

import android.graphics.Bitmap;

public class MyItem {
    private Bitmap mBitmap;
    private String fullname;
    private int age;
    private int weight;
    private int height;

    public MyItem() {
    }

    public MyItem(Bitmap mBitmap, String fullname, int age, int weight, int height) {
        this.mBitmap = mBitmap;
        this.fullname = fullname;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
