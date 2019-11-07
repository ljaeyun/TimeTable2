package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static Context mContext = null;
    Integer sid, syear, smajor, subMajor = -1, doubleMajor = -1;
    EditText editTextid;
    ArrayList<ClassSubject> classlist,ex_classlist;

    SQLiteDatabase database;

    CheckBox chksubMajor, chkdoubleMajor, rotc;
    Spinner spinnerM2;

    private Integer getId() {
        return sid;
    }

    private void setId(int id) {
        this.sid = id;
    }

    private Integer getyear() {
        return syear;
    }

    private void setyear(int year) {
        this.syear = year;
    }

    private Integer getmajor() {
        return smajor;
    }

    private void setmajor(int major) {
        this.smajor = major;
    }

    public static final String PACKAGE_DIR = "/data/data/com.example.myapplication/databases";
    public static final String[] Database_name = new String[9];

    public static void initialize(Context ctx) {
// check
        Database_name[0] = "test.db";
        Database_name[1] = "credit.db";
        Database_name[2] = "natural.db";
        Database_name[3] = "biz.db";
        Database_name[4] = "sw.db";
        Database_name[5] = "ei.db";
        Database_name[6] = "engineer.db";
        Database_name[7] = "hss.db";
        Database_name[8] = "kwlaw.db";

        File folder = new File(PACKAGE_DIR);
        folder.mkdirs();

        for (int i = 0; i < 9; i++) {
            File outfile = new File(PACKAGE_DIR + "/" + Database_name[i]);
            if (outfile.length() <= 0) {
                AssetManager assetManager = ctx.getResources().getAssets();
                try {
                    InputStream is = assetManager.open(Database_name[i], AssetManager.ACCESS_BUFFER);
                    long filesize = is.available();
                    byte[] tempdata = new byte[(int) filesize];
                    is.read(tempdata);
                    is.close();
                    outfile.createNewFile();
                    FileOutputStream fo = new FileOutputStream(outfile);
                    fo.write(tempdata);
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize(getApplicationContext());

        editTextid = (EditText) findViewById(R.id.editTextId);

        Button button1 = (Button) findViewById(R.id.button1);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerMajor);
        spinnerM2 = (Spinner) findViewById(R.id.spinnerMajor3);
        Spinner spinner3 = (Spinner) findViewById(R.id.spinnerYear);
        makedb();//교양 db생성
        classlist = new ArrayList<>();
        ex_classlist = new ArrayList<>();

        chksubMajor = (CheckBox) findViewById(R.id.subMajor);
        chkdoubleMajor = (CheckBox) findViewById(R.id.doubleMajor);
        spinnerM2.setVisibility(View.GONE);


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.학과, R.layout.spinner_item);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.학년, R.layout.spinner_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//데이터베이스 열기
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setmajor(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chksubMajor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean arg1) {
                if (chksubMajor.isChecked())    // 나중에 애니메이션효과 추가1
                {
                    chkdoubleMajor.setChecked(false); // 부전공 복수전공 둘중 하나만 선택가능
                    spinnerM2.setVisibility(View.VISIBLE);
                    subMajor = 0;
                    spinnerM2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            subMajor = position;//부전공
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else {
                    spinnerM2.setVisibility(View.INVISIBLE);
                    spinnerM2.setVisibility(View.GONE);
                    subMajor = -1;
                }
            }
        });

        chkdoubleMajor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkdoubleMajor.isChecked()) // 나중에 애니메이션효과 추가2
                {
                    chksubMajor.setChecked(false); // 부전공 복수전공 둘중 하나만 선택가능
                    spinnerM2.setVisibility(View.VISIBLE);
                    doubleMajor = 0;
                    spinnerM2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            doubleMajor = position;//복수전공
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    spinnerM2.setVisibility(View.INVISIBLE);
                    spinnerM2.setVisibility(View.GONE);
                    doubleMajor = -1;
                }
            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setyear(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mContext = this;

        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //id = 2016000000;
                String hakbun = "";
                hakbun = editTextid.getText().toString();

                if (hakbun.equals(""))
                    Toast.makeText(getApplicationContext(), "학번을 입력하세요", Toast.LENGTH_SHORT).show();
                else {
                    setId(Integer.parseInt(hakbun));//int로 바꿔서 전달

                    Intent intent = new Intent(getApplicationContext(), major_select.class);
                    intent.putExtra("studentId", sid);
                    intent.putExtra("studentYear", syear);
                    intent.putExtra("studentMajor", smajor);

                    intent.putExtra("subMajor", subMajor);
                    intent.putExtra("doubleMajor", doubleMajor);
                    intent.putParcelableArrayListExtra("classlist", classlist);
                    intent.putParcelableArrayListExtra("ex_classlist", ex_classlist);
                    startActivity(intent);
                }
            }
        });
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
}
