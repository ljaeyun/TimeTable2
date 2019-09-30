package com.example.myapplication;

import android.app.ListActivity;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class major_select extends AppCompatActivity {

    String dbname = "myDB";

    String tableName = "class";

    String sql;

    SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성

    Cursor resultset;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성

    ListView list;   // ListView 객체 생성

    String[] result;   // ArrayAdapter에 넣을 배열 생성

    ArrayList<HashMap<String, String>> classList;

    private static final String TAG_lecNum = "number";
    private static final String TAG_lecName = "name";
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.major_select);

        db = openOrCreateDatabase("test.db" , MODE_PRIVATE, null);   // 있으면 열고 없으면 DB를 생성

        list = (ListView)findViewById(R.id.listview);
        classList = new ArrayList<HashMap<String, String>>();


        try
        {
            db = this.openOrCreateDatabase("test.db", MODE_PRIVATE, null);


                db.execSQL("create table allsubject as select * from 과학과기술");
                db.execSQL("alter table allsubject add column typecode integer default 1");

                db.execSQL("create table test1 as select * from 언어와표현");
                db.execSQL("alter table test1 add column typecode integer default 2");

                db.execSQL("create table test2 as select * from 인간과철학");
                db.execSQL("alter table test2 add column typecode integer default 3");

                db.execSQL("create table test3 as select * from 사회와경제");
                db.execSQL("alter table test3 add column typecode integer default 4");

                db.execSQL("create table test4 as select * from 글로벌");
                db.execSQL("alter table test4 add column typecode integer default 5");

                db.execSQL("create table test5 as select * from 예술과체육");
                db.execSQL("alter table test5 add column typecode integer default 6");

                db.execSQL("create table test6 as select * from e러닝");
                db.execSQL("alter table test6 add column typecode integer default 7");

                db.execSQL("create table test7 as select * from 실용영어");
                db.execSQL("alter table test7 add column typecode integer default 8");

                for (int i = 1; i < 8; i++)
                    db.execSQL("insert into allsubject select * from test" + i);//모두 넣는다

                for (int i = 1; i < 8; i++)
                    db.execSQL("drop table test" + i);//모두 지운다

            db.close();
        }
        catch (SQLiteException se)
        {
            Toast.makeText(getApplicationContext(), se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("", se.getMessage());
        }
        showList();
    }


    protected  void showList()
    {
        try
        {
            SQLiteDatabase ReadDB = this.openOrCreateDatabase("test.db", MODE_PRIVATE, null);

            Cursor c = ReadDB.rawQuery("SELECT * FROM " + tableName + "", null);

            if(c!=null)
            {
                if(c.moveToFirst())
                {
                    do{
                        String number = c.getString((c.getColumnIndex("학정번호")));
                        String name = c.getString((c.getColumnIndex("과목명")));

                        HashMap<String, String> classes = new HashMap<String, String>();

                        classes.put(TAG_lecNum,number);
                        classes.put(TAG_lecName,name);

                        classList.add(classes);
                    }while (c.moveToNext());
                }
            }
            ReadDB.close();

            adapter = new SimpleAdapter(
                    this, classList, R.layout.class_listview,
                    new String[]{TAG_lecNum, TAG_lecName},
                    new int[]{R.id.lecNumber, R.id.lecName}
            );

            list.setAdapter(adapter);
        }catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("",  se.getMessage());
        }
    }

}

