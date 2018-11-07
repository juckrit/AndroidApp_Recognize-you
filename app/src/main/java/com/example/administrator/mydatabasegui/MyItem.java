package com.example.administrator.mydatabasegui;

import android.graphics.Bitmap;

public class MyItem {
    private String imgPath;
    private String fullname;
    private int age;
    private int weight;
    private int height;

    public MyItem() {
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPathimgPath(String imgPath) {
        this.imgPath = imgPath;
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
