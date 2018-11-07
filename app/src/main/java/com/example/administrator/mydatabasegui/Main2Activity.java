package com.example.administrator.mydatabasegui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    ListView listView;
    MyAdapter arrayAdapter;
    ArrayList<MyItem> arrayList;
    ArrayList<Integer> arrayListMemberId;
    int mFamilyId;
    TextView familyNameInMain2;
    View textEntryView;
    ImageButton imageButton;
    ImageView imageView;
    Bitmap mBitmap;
    Uri imageUri;

    ArrayList<byte[]> arrayListBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        final Intent intent = getIntent();
        mFamilyId = intent.getIntExtra("id", -1);
        arrayList = new ArrayList<>();
        arrayListMemberId = new ArrayList<>();
        arrayListBitmap = new ArrayList<>();
        MyItem myItem = new MyItem();
        imageUri = null;


        listView = (ListView) findViewById(R.id.myListView);
        familyNameInMain2 = (TextView) findViewById(R.id.familyNameInMain2);
        familyNameInMain2.setText(getFamilyName(mFamilyId));
        arrayAdapter = new MyAdapter(this, R.layout.mylistview, arrayList);
        listView.setAdapter(arrayAdapter);
        //Toast.makeText(getApplicationContext(), "id " + String.valueOf(mFamilyId), Toast.LENGTH_SHORT).show();
        //addMemberToDb(1, "f", "l", 1, 1, 1, "s");
        updateListView();
//        updateArrayMemberId();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //Toast.makeText(getApplicationContext(), "positionOfLV " + String.valueOf(position) + " Id " + arrayListMemberId.get(position) + " positionOfArray " + arrayListMemberId.indexOf(arrayListMemberId.get(position)), Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                builder.setTitle("Are you sure want to delete this member?");

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMemberFromDb(arrayListMemberId.get(position), position);


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "positionOfLV " + String.valueOf(position) + " Id " + arrayListMemberId.get(position) + " positionOfArray " + arrayListMemberId.indexOf(arrayListMemberId.get(position)), Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(getApplicationContext(), Main3Activity.class);
                intent2.putExtra("memberId", arrayListMemberId.get(position));
                startActivity(intent2);
            }
        });
    }

    //    public void updateArrayMemberId() {
//        String sql = "SELECT id FROM familyMember WHERE familyId = " + mFamilyId;
//        Cursor c = MainActivity.db.rawQuery(sql, null);
//        int idIndex = c.getColumnIndex("id");
//        if (c.moveToFirst()) {
//            arrayListMemberId.clear();
//            do {
//                int id = c.getInt(idIndex);
//                arrayListMemberId.add(id);
//            } while (c.moveToNext());
//        }
//
//
//    }
    private Bitmap convertPathToBitmap(String path) {
        Bitmap bitmapsimplesize;

        int size = 10; //minimize  as much as you want
        if (path != null) {
            Bitmap bitmapOriginal = BitmapFactory.decodeFile(path);
            if (bitmapOriginal != null) {
                int bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmapOriginal);
                if (bitmapByteCount > 50000000) {
                    bitmapsimplesize = Bitmap.createScaledBitmap(bitmapOriginal, bitmapOriginal.getWidth() / size, bitmapOriginal.getHeight() / size, true);

                } else {

                    bitmapsimplesize = bitmapOriginal;
                }

            } else {
                bitmapsimplesize = bitmapOriginal;

            }

        } else {

            return null;

        }
        return null;
    }

    public void updateListView() {
        String sql = "SELECT * FROM familyMember WHERE familyId = " + mFamilyId;
        Cursor c = MainActivity.db.rawQuery(sql, null);
        int idIndex = c.getColumnIndex("id");
        int familyIdIndex = c.getColumnIndex("familyId");
        int firstNameIndex = c.getColumnIndex("firstName");
        int lastNameIndex = c.getColumnIndex("lastName");
        int ageIndex = c.getColumnIndex("age");
        int weightIndex = c.getColumnIndex("weight");
        int heightIndex = c.getColumnIndex("height");
        int imagPathIndex = c.getColumnIndex("imgPath");
        c.moveToFirst();
        String imgPath;

        if (c.moveToFirst()) {
            arrayList.clear();
            do {
                MyItem myItem = new MyItem();
                myItem.setFullname(c.getString(firstNameIndex) + " " + c.getString(lastNameIndex));
                myItem.setAge(c.getInt(ageIndex));
                myItem.setHeight(c.getInt(heightIndex));
                myItem.setWeight(c.getInt(weightIndex));
                imgPath = c.getString(imagPathIndex);
                myItem.setImgPathimgPath(imgPath);
                arrayList.add(myItem);
                imgPath = null;
            } while (c.moveToNext());
        }
        arrayAdapter.notifyDataSetChanged();
        String sql2 = "SELECT id FROM familyMember WHERE familyId = " + mFamilyId;
        Cursor c2 = MainActivity.db.rawQuery(sql2, null);
        int idIndex2 = c2.getColumnIndex("id");
        if (c2.moveToFirst()) {
            arrayListMemberId.clear();
            do {
                int id = c2.getInt(idIndex2);
                arrayListMemberId.add(id);
            } while (c2.moveToNext());
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
    }

    public void addNewMemberClick(View view) {
        final String[] firstName = new String[1];
        final String[] lastName = new String[1];
        final int[] age = new int[1];
        final int[] weight = new int[1];
        final int[] height = new int[1];

        LayoutInflater factory = LayoutInflater.from(this);
        textEntryView = factory.inflate(R.layout.text_entry, null);
        final EditText input1 = (EditText) textEntryView.findViewById(R.id.EditTextFirstName);
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.EditTextLastName);
        final EditText input3 = (EditText) textEntryView.findViewById(R.id.EditTextAge);
        final EditText input4 = (EditText) textEntryView.findViewById(R.id.EditTextWeight);
        final EditText input5 = (EditText) textEntryView.findViewById(R.id.EditTextHeight);
        final ImageButton imageButton = (ImageButton) textEntryView.findViewById(R.id.imageButton);
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
                .setTitle("Add New Member Details")
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

                                Bitmap bitmap = mBitmap;
                                String path;
                                if (imageUri == null) {
                                    path = null;
                                } else {

                                    path = getRealPathFromURI(imageUri);
                                }

                                addMemberToDb(mFamilyId, firstName, lasttName, age, weight, height, path);
                                mBitmap = null;
                                imageUri = null;
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


//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("New Member");
//
//// Set up the input
//        final EditText inputfirstName = new EditText(this);
//        final EditText inputlastName = new EditText(this);
//        final EditText inputage = new EditText(this);
//        final EditText inputweight = new EditText(this);
//        final EditText inputheight = new EditText(this);
//// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        inputfirstName.setInputType(InputType.TYPE_CLASS_TEXT);
//        inputlastName.setInputType(InputType.TYPE_CLASS_TEXT);
//        inputage.setInputType(InputType.TYPE_CLASS_NUMBER);
//        inputweight.setInputType(InputType.TYPE_CLASS_NUMBER);
//        inputheight.setInputType(InputType.TYPE_CLASS_NUMBER);
//        builder.setView(inputfirstName);
//        builder.setView(inputlastName);
//        builder.setView(inputage);
//        builder.setView(inputweight);
//        builder.setView(inputheight);
//
//// Set up the buttons
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                firstName[0] = inputfirstName.getText().toString();
//                lastName[0] = inputlastName.getText().toString();
//                age[0] = Integer.parseInt(inputage.getText().toString());
//                weight[0] = Integer.parseInt(inputweight.getText().toString());
//                height[0] = Integer.parseInt(inputheight.getText().toString());
//                Toast.makeText(getApplicationContext(), String.valueOf(age[0]), Toast.LENGTH_SHORT).show();
//                //addFamilyToDB(familyName[0]);
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        builder.show();


    }

    public void addMemberToDb(int familyId, String firstName, String lastName, int age, int weight, int height, String path) {
//        Log.i("result", String.valueOf(familyId));
////        Log.i("result", String.valueOf(firstName));
////        Log.i("result", String.valueOf(lastName));
////        Log.i("result", String.valueOf(age));
////        Log.i("result", String.valueOf(weight));
////        Log.i("result", String.valueOf(height));
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


//        String str = new String(imgByteArray, StandardCharsets.UTF_8);
        //  byte[] b = str.getBytes();

//        String sql = "INSERT INTO familyMember(familyId, firstName, lastName, age, weight, height) VALUES(%d,%s,%s,%d,%d,%d)";
//        String sqlFormatted = String.format("INSERT INTO familyMember(familyId, firstName, lastName, age, weight, height) VALUES(%d,'%s','%s',%d,%d,%d)", familyId, checkFirstName, checkLastName, age, weight, height);
//        Log.i("result", sqlFormatted);
//        MainActivity.db.execSQL(sqlFormatted);


        SQLiteDatabase database = MainActivity.db;
        ContentValues cv = new ContentValues();
        cv.put("familyId", familyId);
        cv.put("firstName", checkFirstName);
        cv.put("lastName", checkLastName);
        cv.put("age", age);
        cv.put("weight", weight);
        cv.put("height", height);
        cv.put("imgPath", path);

        database.insert("familyMember", null, cv);


        updateListView();
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
        imageUri = data.getData();

        mBitmap = null;
        try {
            mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (requestCode == 999) {
            imageButton.setImageBitmap(mBitmap);


        }

    }

    public void changeFamilyNameClick(View view) {
        try {
            final String[] familyName = new String[1];
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Change Family Name");

// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            final String oldFamilyName = getFamilyName(mFamilyId);
            input.setText(oldFamilyName);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    familyName[0] = input.getText().toString();
                    Toast.makeText(getApplicationContext(), "Newname " + familyName[0], Toast.LENGTH_SHORT).show();
                    changeFamilyNameToDB(oldFamilyName, familyName[0]);
                    familyNameInMain2.setText(getFamilyName(mFamilyId));
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            //builder.show();

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() >= 1) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    } else {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                    }
                }
            });

        } catch (Exception e) {
            Log.i("error changeFamilyName", e.getMessage());
        }
    }

    public String getFamilyName(int familyId) {
        String result = "";
        String sql = "SELECT familyName FROM family WHERE id =" + familyId;
        Cursor c = MainActivity.db.rawQuery(sql, null);
        int familyNameIndex = c.getColumnIndex("familyName");
        int idIndex = c.getColumnIndex("id");
        //c.moveToFirst();
        if (c.moveToFirst()) {

            do {
                result = c.getString(familyNameIndex);

            } while (c.moveToNext());
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        return result;
    }

    public void changeFamilyNameToDB(String oldFamilyName, String newFamilyName) {
        String strSQL = "UPDATE family SET familyName = ? WHERE familyName = ?";
        SQLiteStatement sqLiteStatement = MainActivity.db.compileStatement(strSQL);
        sqLiteStatement.bindString(1, newFamilyName);
        sqLiteStatement.bindString(2, oldFamilyName);
        sqLiteStatement.execute();

    }

    public void deleteMemberFromDb(int memberId, int position) {
        String sql = "DELETE FROM familyMember WHERE id = ?";
        SQLiteStatement sqLiteStatement = MainActivity.db.compileStatement(sql);
        sqLiteStatement.bindDouble(1, Double.valueOf(memberId));
        sqLiteStatement.execute();
        arrayListMemberId.remove(position);
        arrayList.remove(position);
        updateListView();
//        updateArrayMemberId();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateListView();
    }
}


