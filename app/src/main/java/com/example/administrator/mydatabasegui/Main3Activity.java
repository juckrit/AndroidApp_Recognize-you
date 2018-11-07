package com.example.administrator.mydatabasegui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Main3Activity extends AppCompatActivity {
    View textEntryView;
    Bitmap mBitmapFromDatabase;
    int memberId;
    int familyId;
    ImageButton imageButtonMain3;
    TextView TextViewFirstNameMain3;
    TextView TextViewLastNameMain3;
    TextView TextViewAgeMain3;
    TextView TextViewWeightMain3;
    TextView TextViewHeightMain3;
    String fname;
    String lname;
    int age;
    int weight;
    int height;
    String path;
    Bitmap bitmap2;
    boolean isSelecteed = false;
    String imgPathToBeInserted;
    Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", -1);

        imageButtonMain3 = (ImageButton) findViewById(R.id.imageButtonMain3);
        TextViewFirstNameMain3 = (TextView) findViewById(R.id.TextViewFirstNameMain3);
        TextViewLastNameMain3 = (TextView) findViewById(R.id.TextViewLastNameMain3);
        TextViewAgeMain3 = (TextView) findViewById(R.id.TextViewAgeMain3);
        TextViewWeightMain3 = (TextView) findViewById(R.id.TextViewWeightMain3);
        TextViewHeightMain3 = (TextView) findViewById(R.id.TextViewHeightMain3);

        getMemberDetail(memberId);
        imageButtonMain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (path == null) {
                    Toast.makeText(getApplication(), "This is default image", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
                    startActivity(intent);
                }

            }
        });

    }


    public void getMemberDetail(int id) {

        String sql = "SELECT * FROM familyMember WHERE id" + " = " + id;
        Cursor c = MainActivity.db.rawQuery(sql, null);
        int idIndex = c.getColumnIndex("id");
        int familyIdIndex = c.getColumnIndex("familyId");

        int firstNameIndex = c.getColumnIndex("firstName");
        int lastNameIndex = c.getColumnIndex("lastName");
        int ageIndex = c.getColumnIndex("age");
        int weightIndex = c.getColumnIndex("weight");
        int heightIndex = c.getColumnIndex("height");
        int imagDataIndex = c.getColumnIndex("imgPath");
        if (c.moveToFirst()) {
            do {
                familyId = c.getInt(familyIdIndex);
                fname = c.getString(firstNameIndex);
                lname = c.getString(lastNameIndex);
                age = c.getInt(ageIndex);
                weight = c.getInt(weightIndex);
                height = c.getInt(heightIndex);

                TextViewFirstNameMain3.setText("Firstname: " + fname);
                TextViewLastNameMain3.setText("Lastname: " + lname);
                TextViewAgeMain3.setText("Age: " + String.valueOf(age));
                TextViewWeightMain3.setText("Weight: " + String.valueOf(weight));
                TextViewHeightMain3.setText("Height: " + String.valueOf(height));
                path = c.getString(imagDataIndex);
                imgPathToBeInserted = path;
//                mBitmapFromDatabase = convertPathToBitmap(path);
//                imageButtonMain3.setImageBitmap(mBitmapFromDatabase);
                setImgButtonIMG(path);
            } while (c.moveToNext());
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

    }

    public void setImgButtonIMG(String path) {

        int size = 10; //minimize  as much as you want
        if (path != null) {
            Bitmap bitmapOriginal = BitmapFactory.decodeFile(path);
            if (bitmapOriginal != null) {
                int bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmapOriginal);

                if (bitmapByteCount > 10000000) {
                    Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmapOriginal, bitmapOriginal.getWidth() / size, bitmapOriginal.getHeight() / size, true);
                    mBitmapFromDatabase = bitmapsimplesize;
                    imageButtonMain3.setImageBitmap(bitmapsimplesize);
                } else {
//                this.ivImg.setImageBitmap(bitmapOriginal);
                    mBitmapFromDatabase = bitmapOriginal;
                    imageButtonMain3.setImageURI(Uri.fromFile(new File(path)));
                }

            } else {
                // Toast.makeText(getContext(),"File "+ path+"Not Exist",Toast.LENGTH_SHORT).show();
                Drawable myDrawable = getResources().getDrawable(R.drawable.boy);
                imageButtonMain3.setImageDrawable(myDrawable);
                mBitmapFromDatabase = ((BitmapDrawable) myDrawable).getBitmap();


            }

        } else {
//        this.ivImg.setImageURI(Uri.fromFile(new File(path))); //work but out of mem error
            //       this.ivImg.setImageBitmap(bitmapsimplesize);
            Drawable myDrawable = getResources().getDrawable(R.drawable.boy);

            imageButtonMain3.setImageDrawable(myDrawable);
        }
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getApplication().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        ImageButton imageButton = (ImageButton) textEntryView.findViewById(R.id.imageButton);
        Uri imageUri = data.getData();

//        mBitmap = null;
        try {
            bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            imgPathToBeInserted = getRealPathFromURI(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (requestCode == 999) {
            isSelecteed = true;
            imageButton.setImageBitmap(bitmap2);


        }

    }

    public void updateMemberDetailClick(View view) {
        LayoutInflater factory = LayoutInflater.from(this);
        textEntryView = factory.inflate(R.layout.text_entry, null);
        final EditText input1 = (EditText) textEntryView.findViewById(R.id.EditTextFirstName);
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.EditTextLastName);
        final EditText input3 = (EditText) textEntryView.findViewById(R.id.EditTextAge);
        final EditText input4 = (EditText) textEntryView.findViewById(R.id.EditTextWeight);
        final EditText input5 = (EditText) textEntryView.findViewById(R.id.EditTextHeight);
        final ImageButton imageButton = (ImageButton) textEntryView.findViewById(R.id.imageButton);

        input1.setText(fname);
        input2.setText(lname);
        input3.setText(String.valueOf(age));
        input4.setText(String.valueOf(weight));
        input5.setText(String.valueOf(height));
        if (mBitmapFromDatabase == null) {
            Drawable myDrawable = getResources().getDrawable(R.drawable.boy);

            imageButton.setImageDrawable(myDrawable);
        } else {
            imageButton.setImageBitmap(mBitmapFromDatabase);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                //Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                //intent.setData(uri);
                intent.setType("image/*");
                startActivityForResult(intent, 999);

            }

        });


        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setIcon(R.mipmap.ic_add)
                .setTitle("Update Member Details")
                .setView(textEntryView)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                String firstName = input1.getText().toString();
                                String lasttName = input2.getText().toString();
                                String ageStr = input3.getText().toString();
                                String weightStr = input4.getText().toString();
                                String heightStr = input5.getText().toString();
                                int age;
                                int weight;
                                int height;
                                if (TextUtils.isEmpty(ageStr)) {
                                    age = 0;
                                } else {
                                    age = Integer.valueOf(ageStr);
                                }
                                if (TextUtils.isEmpty(weightStr)) {
                                    weight = 0;
                                } else {
                                    weight = Integer.valueOf(weightStr);
                                }
                                if (TextUtils.isEmpty(heightStr)) {
                                    height = 0;
                                } else {
                                    height = Integer.valueOf(heightStr);
                                }
                                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);
                                if (isSelecteed) {

//                                    updateMemberToDb(memberId, familyId, firstName, lasttName, age, weight, height, bitmap2);
                                    updateMemberToDb(memberId, familyId, firstName, lasttName, age, weight, height, imgPathToBeInserted);

                                } else {

//                                    bitmap2 = mBitmapFromDatabase;
//                                    updateMemberToDb(memberId, familyId, firstName, lasttName, age, weight, height, bitmap2);
                                    updateMemberToDb(memberId, familyId, firstName, lasttName, age, weight, height, imgPathToBeInserted);
                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                /*
                                 * User clicked cancel so do some stuff
                                 */
                            }
                        });
        alert.show();
    }

    public void updateMemberToDb(int id, int familyId, String firstName, String lastName, int age, int weight, int height, String path) {
        String checkFirstName = "";
        String checkLastName = "";

        if (TextUtils.isEmpty(firstName)) {
            checkFirstName = "Unknown";
        } else {
            checkFirstName = firstName;
        }
        if (TextUtils.isEmpty(lastName)) {
            checkLastName = "Unknown";
        } else {
            checkLastName = lastName;
        }


        SQLiteDatabase database = MainActivity.db;
        ContentValues cv = new ContentValues();
        cv.put("familyId", familyId);
        cv.put("firstName", checkFirstName);
        cv.put("lastName", checkLastName);
        cv.put("age", age);
        cv.put("weight", weight);
        cv.put("height", height);
        cv.put("imgPath", path);

        String whereCl = "id=" + id;
        database.update("familyMember", cv, whereCl, null);
        Toast.makeText(getApplicationContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
        getMemberDetail(id);
    }
}
