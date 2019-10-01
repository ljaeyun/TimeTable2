package com.example.myapplication;

import android.app.ListActivity;
import android.database.sqlite.SQLiteException;
import android.support.design.widget.TabLayout;
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

    SQLiteDatabase database;
    String table = "business";
    Integer sid, syear, smajor, minorNum, subMajor, doubleMajor;
    String spw;
    Cursor c;
    ClassSubject cs;
    ArrayList<ClassSubject> classlist;
    int num = 0, level = 1;
    TableRow tr[];
    TextView text[][];
    TableLayout.LayoutParams rowLayout;
    TableLayout t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.major_select);

        Intent intent1 = getIntent();
        sid = intent1.getIntExtra("studentId", 1);
        spw = intent1.getStringExtra("studentPw");
        syear = intent1.getIntExtra("studentYear", 1);
        smajor = intent1.getIntExtra("studentMajor", 1);//전공

        minorNum = intent1.getIntExtra("minorNum", 0);
        subMajor = intent1.getIntExtra("subMajor", 0);//부전공
        doubleMajor = intent1.getIntExtra("doubleMajor", 0);//복수전공

        choosetable(smajor);//전공 db열고 table선택

        classlist = new ArrayList<>();

        rowLayout = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        t = (TableLayout) findViewById(R.id.result_table);
        TableRow tableRow = (TableRow) findViewById(R.id.tablerow);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeLevel(pos);
                showList();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        showList();
//        db = openOrCreateDatabase("test.db" , MODE_PRIVATE, null);   // 있으면 열고 없으면 DB를 생성
//
//        list = (ListView)findViewById(R.id.listview);
//        classList = new ArrayList<HashMap<String, String>>();
//
//
//        try
//        {
//            db = this.openOrCreateDatabase("test.db", MODE_PRIVATE, null);
//
//
//                db.execSQL("create table allsubject as select * from 과학과기술");
//                db.execSQL("alter table allsubject add column typecode integer default 1");
//
//                db.execSQL("create table test1 as select * from 언어와표현");
//                db.execSQL("alter table test1 add column typecode integer default 2");
//
//                db.execSQL("create table test2 as select * from 인간과철학");
//                db.execSQL("alter table test2 add column typecode integer default 3");
//
//                db.execSQL("create table test3 as select * from 사회와경제");
//                db.execSQL("alter table test3 add column typecode integer default 4");
//
//                db.execSQL("create table test4 as select * from 글로벌");
//                db.execSQL("alter table test4 add column typecode integer default 5");
//
//                db.execSQL("create table test5 as select * from 예술과체육");
//                db.execSQL("alter table test5 add column typecode integer default 6");
//
//                db.execSQL("create table test6 as select * from e러닝");
//                db.execSQL("alter table test6 add column typecode integer default 7");
//
//                db.execSQL("create table test7 as select * from 실용영어");
//                db.execSQL("alter table test7 add column typecode integer default 8");
//
//                for (int i = 1; i < 8; i++)
//                    db.execSQL("insert into allsubject select * from test" + i);//모두 넣는다
//
//                for (int i = 1; i < 8; i++)
//                    db.execSQL("drop table test" + i);//모두 지운다
//
//            db.close();
//        }
//        catch (SQLiteException se)
//        {
//            Toast.makeText(getApplicationContext(), se.getMessage(), Toast.LENGTH_LONG).show();
//            Log.e("", se.getMessage());
//        }
//        showList();
    }

    private void changeLevel(int pos) {
        level = pos + 1;
    }

    private void choosetable(int major) {
        if (major >= 0 && major <= 1)
            database = openOrCreateDatabase("biz.db", MODE_PRIVATE, null);
        else if (major >= 2 && major <= 5)
            database = openOrCreateDatabase("engineer.db", MODE_PRIVATE, null);
        else if (major >= 6 && major <= 8)
            database = openOrCreateDatabase("sw.db", MODE_PRIVATE, null);
        else if (major >= 9 && major <= 15)
            database = openOrCreateDatabase("hss.db", MODE_PRIVATE, null);
        else if (major >= 16 && major <= 20)
            database = openOrCreateDatabase("natural.db", MODE_PRIVATE, null);
        else if (major >= 21 && major <= 28)
            database = openOrCreateDatabase("ei.db", MODE_PRIVATE, null);
        else
            database = openOrCreateDatabase("kwlaw.db", MODE_PRIVATE, null);

        switch (major) {
            case 0:
                table = "business";
                break;
            case 1:
                table = "itrade";
                break;
            case 2:
                table = "archi";
                break;
            case 3:
                table = "arching";
                break;
            case 4:
                table = "chemng";
                break;
            case 5:
                table = "env";
                break;
            case 6:
                table = "cie";
                break;
            case 7:
                table = "software";
                break;
            case 8:
                table = "ic";
                break;
            case 9:
                table = "kor";
                break;
            case 10:
                table = "ci";
                break;
            case 11:
                table = "media";
                break;
            case 12:
                table = "mediacomm";
                break;
            case 13:
                table = "psy";
                break;
            case 14:
                table = "engind";
                break;
            case 15:
                table = "eng";
                break;
            case 16:
                table = "sports";
                break;
            case 17:
                table = "math";
                break;
            case 18:
                table = "ep";
                break;
            case 19:
                table = "infocontents";
                break;
            case 20:
                table = "chemi";
                break;
            case 21:
                table = "robot";
                break;
            case 22:
                table = "electric";
                break;
            case 23:
                table = "ee";
                break;
            case 24:
                table = "radiowave";
                break;
            case 25:
                table = "snme";
                break;
            case 26:
                table = "elcomm";
                break;
            case 27:
                table = "ce";
                break;
            case 28:
                table = "cs";
                break;
            case 29:
                table = "intern";
                break;
            case 30:
                table = "law";
                break;
            case 31:
                table = "asset";
                break;
            case 32:
                table = "pa";
                break;
            default:
                table = "";
                break;
        }
    }

    public void timecal(ClassSubject s, Cursor c) {
        int n = 0, m = 0;// 월: 0~9
        String timestr = null;//시간 문자열
        TimeArr t = new TimeArr();

        if (c.getString(5) != null) {
            if (c.getString(5).equals("화")) {//화:10~19
                n = 10;
            } else if (c.getString(5).equals("수")) {
                n = 20;
            } else if (c.getString(5).equals("목")) {
                n = 30;
            } else if (c.getString(5).equals("금")) {
                n = 40;
            } else if (c.getString(5).equals("토")) {
                n = 50;
            }
            if (c.getString(7) != null) {
                if (c.getString(7).equals("화")) {
                    m = 10;
                } else if (c.getString(7).equals("수")) {
                    m = 20;
                } else if (c.getString(7).equals("목")) {
                    m = 30;
                } else if (c.getString(7).equals("금")) {
                    m = 40;
                }
                timestr = c.getString(5) + c.getString(6) + ' ' + c.getString(7) + c.getString(8);
            } else
                timestr = c.getString(5) + c.getString(6);


            String[] arr1 = c.getString(6).split("\\.|,|\\n");//시간1 .이나,으로 구분하고

            if (c.getString(8) != null) {
                String[] arr2 = c.getString(8).split("\\.|,|\\n");//시간2 .이나,으로 구분하고 + 공백도 가끔있음
                for (int i = 0; i < arr2.length; i++) {
                    if (arr2[i].equals("") == false)//공백으로 자르고나면 ""남음...
                        t.put(m + Integer.parseInt(arr2[i]));
                }
            }//null 예외처리!!!!

            //자른거
            for (int i = 0; i < arr1.length; i++)
                t.put(n + Integer.parseInt(arr1[i]));
        } else {//인강인 경우..
            n = 50;
            t.put(n);
        }
        t.putCode(c.getString(0));//학정번호도 넣어봅시다
        t.setEsu(c.getString(2));//이수
        t.setCredit(Integer.parseInt(c.getString(3)));//학점
        t.setprof(c.getString(4));//교수명도
        t.setTimestr(timestr);
        s.put(t);
    }

    private void findClasses(String classname) {
        try {
            Cursor c1 = database.rawQuery("select 학정번호, 과목명, 이수,학점, 담당교수, 요일1,시간1,요일2,시간2 from " + table + " where 학정번호 like '_____" + level + "%'", null);
            if (c1 != null) {
                int num = c1.getCount();//개수
                if (num != 0) {
                    for (int i = 0; i < num; i++) {
                        c1.moveToNext();
                        if (c1.getString(1).equalsIgnoreCase(classname)) {
                            timecal(cs, c1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }
    }

    protected void showList() {
        num = 0;
        t.removeAllViews();//싹 지우고 출력
        //탭을 다시 선택하면 눌렀던게 없어진다

        try {
            c = database.rawQuery("select distinct 과목명, 이수, 학점 from " + table + " where 학정번호 like '_____" + level + "%'", null);
            if (c != null) {
                int count = c.getCount();//개수
                if (count != 0) {
                    tr = new TableRow[count];
                    text = new TextView[count][3];

                    for (int i = 0; i < count; i++) {
                        c.moveToNext();
                        tr[i] = new TableRow(this);
                        for (int j = 0; j < 3; j++) {
                            text[i][j] = new TextView(this);
                            text[i][j].setText(c.getString(j));

                            text[i][j].setTextSize(15);
                            text[i][j].setTextColor(Color.BLACK);
                            text[i][j].setGravity(Gravity.CENTER);
                            text[i][j].setBackgroundResource(R.drawable.cell_shape);
                            text[i][j].setTag(i);

                            text[i][j].setOnClickListener(new Button.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    num = (Integer) v.getTag();//선택된 번호
                                    if (v.getBackground().getConstantState() == getResources().getDrawable(R.drawable.select_cell).getConstantState()) {//다시누르면
                                        for (int k = 0; k < 3; k++)
                                            text[num][k].setBackgroundResource(R.drawable.cell_shape);//줄 선택 해제
                                        c.moveToPosition(num);
                                        for (int l = 0; l < classlist.size(); l++) {
                                            if (classlist.get(l).getName().equals(c.getString(0)))
                                                classlist.remove(l);//배열에서 찾아서 지운다
                                        }
                                    } else {//처음 누르면
                                        for (int k = 0; k < 3; k++)
                                            text[num][k].setBackgroundResource(R.drawable.select_cell);//선택한 줄 색칠
                                        c.moveToPosition(num);
                                        cs = new ClassSubject(c.getString(0));//그 과목명으로
                                        findClasses(c.getString(0));//분반저장
                                        classlist.add(cs); //배열에 넣는다
                                    }
                                }
                            });
                            tr[i].addView(text[i][j]);
                        }
                        t.addView(tr[i], rowLayout);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }

//        try
//        {
//            SQLiteDatabase ReadDB = this.openOrCreateDatabase("test.db", MODE_PRIVATE, null);
//
//            Cursor c = ReadDB.rawQuery("SELECT * FROM " + tableName + "", null);
//
//            if(c!=null)
//            {
//                if(c.moveToFirst())
//                {
//                    do{
//                        String number = c.getString((c.getColumnIndex("학정번호")));
//                        String name = c.getString((c.getColumnIndex("과목명")));
//
//                        HashMap<String, String> classes = new HashMap<String, String>();
//
//                        classes.put(TAG_lecNum,number);
//                        classes.put(TAG_lecName,name);
//
//                        classList.add(classes);
//                    }while (c.moveToNext());
//                }
//            }
//            ReadDB.close();
//
//            adapter = new SimpleAdapter(
//                    this, classList, R.layout.class_listview,
//                    new String[]{TAG_lecNum, TAG_lecName},
//                    new int[]{R.id.lecNumber, R.id.lecName}
//            );
//
//            list.setAdapter(adapter);
//        }catch (SQLiteException se) {
//            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
//            Log.e("",  se.getMessage());
//        }
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, TimeTableActivity.class);

        intent.putExtra("studentId", sid);
        intent.putExtra("studentPw", spw);
        intent.putExtra("studentYear", syear);
        intent.putExtra("studentMajor", smajor);

        intent.putExtra("minorNum", minorNum);
        intent.putExtra("subMajor", subMajor);
        intent.putExtra("doubleMajor", doubleMajor);
        intent.putParcelableArrayListExtra("classlist", classlist);

        startActivity(intent);
    }
}

