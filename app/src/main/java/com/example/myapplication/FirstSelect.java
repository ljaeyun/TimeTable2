package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FirstSelect extends AppCompatActivity {
    public static Context mContext = null;
    Integer sid, syear, smajor, subMajor = -1, doubleMajor = -1;
    ArrayList<ClassSubject> classlist;

    SQLiteDatabase database;

    CheckBox chksubMajor, chkdoubleMajor, rotc;
    Spinner spinnerMajor1, spinnerMajor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        Intent intent1 = getIntent();

        sid = intent1.getIntExtra("studentId", 1);
        syear = intent1.getIntExtra("studentYear", 1);
        smajor = intent1.getIntExtra("studentMajor", 1);
        makedb();//교양 db생성
        classlist = new ArrayList<>();

        chksubMajor = (CheckBox) findViewById(R.id.subMajor);
        chkdoubleMajor = (CheckBox) findViewById(R.id.doubleMajor);

        spinnerMajor1 = (Spinner) findViewById(R.id.spinnerMajor1);
        spinnerMajor1.setVisibility(View.GONE);

        spinnerMajor2 = (Spinner) findViewById(R.id.spinnerMajor2);
        spinnerMajor2.setVisibility(View.GONE);

        rotc = (CheckBox) findViewById(R.id.option6);
        rotc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rotc.isChecked()) {// 나중에 애니메이션효과 추가2
                    if (syear == 2 || syear == 3) {
                        int year = syear + 1;
                        database = openOrCreateDatabase("test.db", MODE_PRIVATE, null);
                        try {
                            Cursor c1 = database.rawQuery("select 학정번호, 과목명, 이수,학점, 담당교수, 요일1,시간1,요일2,시간2 from 군사학 where 학정번호 like '_____" + year + "%'", null);
                            if (c1 != null) {
                                int num = c1.getCount();
                                for (int i = 0; i < num; i++) {
                                    c1.moveToNext();
                                    ClassSubject cs1 = new ClassSubject(c1.getString(1));
                                    ((major_select) major_select.mContext).timecal(cs1, c1);//분반 추가하고
                                    cs1.setTypecode(10);
                                    classlist.add(cs1);//과목이름을 가진 ..
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("", e.getMessage());
                        }
                    } else {//1,2학년은 rotc가 없죵
                        Toast.makeText(getApplicationContext(), "3,4학년만 선택 가능합니다", Toast.LENGTH_SHORT).show();
                        rotc.setChecked(false);
                    }
                } else {
                    if (classlist.size() != 0)
                        classlist.remove(classlist.size() - 1);//넣은거 뺀다
                }
            }
        });
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.학과, R.layout.spinner_item);
        spinnerMajor1.setAdapter(adapter1);
        spinnerMajor2.setAdapter(adapter1);
        chksubMajor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean arg1) {
                if (chksubMajor.isChecked())    // 나중에 애니메이션효과 추가1
                {
                    spinnerMajor1.setVisibility(View.VISIBLE);
                    subMajor = 0;
                    spinnerMajor1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            subMajor = position;//부전공
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else {
                    spinnerMajor1.setVisibility(View.INVISIBLE);
                    spinnerMajor1.setVisibility(View.GONE);
                    subMajor = -1;
                }
            }
        });

        chkdoubleMajor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkdoubleMajor.isChecked()) // 나중에 애니메이션효과 추가2
                {
                    spinnerMajor2.setVisibility(View.VISIBLE);
                    doubleMajor = 0;
                    spinnerMajor2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            doubleMajor = position;//복수전공
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    spinnerMajor2.setVisibility(View.INVISIBLE);
                    spinnerMajor2.setVisibility(View.GONE);
                    doubleMajor = -1;
                }
            }
        });
        mContext = this;
    }

    public boolean isTableExists(String tableName) {
        database = openOrCreateDatabase("test.db", MODE_PRIVATE, null);
        try {
            Cursor cursor = database.rawQuery("select * from " + tableName + "", null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.close();
                    return true;
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
            return false;
        }

        return false;
    }

    private void makedb() {
        database = openOrCreateDatabase("test.db", MODE_PRIVATE, null);

        if (!isTableExists("allsubject")) {//처음에만 만든다
            database.execSQL("create table allsubject as select * from 과학과기술");
            database.execSQL("alter table allsubject add column typecode integer default 1");

            database.execSQL("create table test1 as select * from 언어와표현");
            database.execSQL("alter table test1 add column typecode integer default 2");

            database.execSQL("create table test2 as select * from 인간과철학");
            database.execSQL("alter table test2 add column typecode integer default 3");

            database.execSQL("create table test3 as select * from 사회와경제");
            database.execSQL("alter table test3 add column typecode integer default 4");

            database.execSQL("create table test4 as select * from 글로벌");
            database.execSQL("alter table test4 add column typecode integer default 5");

            database.execSQL("create table test5 as select * from 예술과체육");
            database.execSQL("alter table test5 add column typecode integer default 6");

            database.execSQL("create table test6 as select * from e러닝");
            database.execSQL("alter table test6 add column typecode integer default 7");

            database.execSQL("create table test7 as select * from 실용영어");
            database.execSQL("alter table test7 add column typecode integer default 8");

            database.execSQL("create table test8 as select * from 필수교양");
            database.execSQL("alter table test8 add column typecode integer default 9");

            database.execSQL("create table test9 as select * from 군사학");
            database.execSQL("alter table test9 add column typecode integer default 10");

            database.execSQL("create table test10 as select * from 교직");
            database.execSQL("alter table test10 add column typecode integer default 11");

            for (int i = 1; i < 11; i++)
                database.execSQL("insert into allsubject select * from test" + i);//모두 넣는다

            for (int i = 1; i < 11; i++)
                database.execSQL("drop table test" + i);//모두 지운다
        }
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, major_select.class);

        intent.putExtra("studentId", sid);
        intent.putExtra("studentYear", syear);
        intent.putExtra("studentMajor", smajor);

        intent.putExtra("subMajor", subMajor);
        intent.putExtra("doubleMajor", doubleMajor);
        intent.putParcelableArrayListExtra("classlist", classlist);

        startActivity(intent);
    }
}
