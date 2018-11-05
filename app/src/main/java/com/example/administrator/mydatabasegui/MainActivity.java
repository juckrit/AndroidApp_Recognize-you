package com.example.administrator.mydatabasegui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> arrayList;
    ArrayList<Integer> arrayListId;
    static SQLiteDatabase db;
    Button myButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.myListView);
        myButton = (Button) findViewById(R.id.Btn_addNewFamily);
        arrayList = new ArrayList<>();
        arrayListId = new ArrayList<>();
        db = this.openOrCreateDatabase("Fimilys", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS family(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "familyName NVARCHAR(50) NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS familyMember(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, familyId INTEGER NOT NULL, firstName NVARCHAR(50), lastName NVARCHAR(50), age int(2), weight int(3), height int(3),imagData BLOB)");
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), arrayList.get(position), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), String.valueOf(arrayListId.get(position)), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                intent.putExtra("id", arrayListId.get(position));
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deleteFamilyFromDB(arrayList.get(position), position);
                return true;
            }
        });


        updateListView();
    }

    public void createData() {
        String sql = "INSERT INTO family(familyName) VALUES('KOONKHAOW')";
        SQLiteStatement sqLiteStatement = db.compileStatement(sql);
        sqLiteStatement.execute();

        updateListView();
    }

    public void updateListView() {
        Cursor c = db.rawQuery("SELECT * FROM family", null);
        int familyNameIndex = c.getColumnIndex("familyName");
        int idIndex = c.getColumnIndex("id");
        //c.moveToFirst();
        if (c.moveToFirst()) {
            arrayList.clear();
            arrayListId.clear();
            do {
                arrayList.add(c.getString(familyNameIndex));
                arrayListId.add(c.getInt(idIndex));
            } while (c.moveToNext());
        } else {

        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        arrayAdapter.notifyDataSetChanged();
    }

    public void addNewFamilyClick(View view) {
        try {
            final String[] familyName = new String[1];
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New Family Name");

// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    familyName[0] = input.getText().toString();
                    Toast.makeText(getApplicationContext(), "name" + familyName[0], Toast.LENGTH_SHORT).show();
                    addFamilyToDB(familyName[0]);

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
                    if(s.length()>=1)
                    {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                    else {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                    }
                }
            });




        } catch (Exception e) {
            Log.i("error", e.getMessage());
        }
    }

    public void addFamilyToDB(String familyName) {

        String sql = "INSERT INTO family(familyName) VALUES(?)";
        SQLiteStatement sqLiteStatement = db.compileStatement(sql);
        sqLiteStatement.bindString(1, familyName);
        sqLiteStatement.execute();
        //Toast.makeText(getApplicationContext(), sql, Toast.LENGTH_SHORT).show();

        updateListView();
    }

    public void deleteFamilyFromDB(String familyName, int position) {

        String sql = "DELETE FROM family WHERE familyName = ?";
        SQLiteStatement sqLiteStatement = db.compileStatement(sql);
        sqLiteStatement.bindString(1, familyName);
        sqLiteStatement.execute();
        arrayList.remove(position);
        arrayListId.remove(position);
        updateListView();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateListView();
    }


}
