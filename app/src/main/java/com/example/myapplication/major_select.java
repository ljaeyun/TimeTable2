package com.example.myapplication;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class major_select extends AppCompatActivity {
    public static Context mContext = null;

    SQLiteDatabase database;
    String table = "business";
    Integer sid, syear, smajor, subMajor, doubleMajor, isMajor = 0;
    String spw;
    Cursor c;
    ClassSubject cs;
    ArrayList<ClassSubject> classlist, ex_classlist;
    ArrayList<Integer> selected[] = new ArrayList[5];
    int num = 0, level = 0;
    TableRow tr[];
    TextView text[][];
    TableLayout.LayoutParams rowLayout;
    TableLayout t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.major_select);

        classlist = new ArrayList<>();
        selected[0] = new ArrayList<>();
        selected[1] = new ArrayList<>();
        selected[2] = new ArrayList<>();
        selected[3] = new ArrayList<>();
        selected[4] = new ArrayList<>();

        Intent intent1 = getIntent();
        isMajor = intent1.getIntExtra("isMajor", 0);//전공인지 부전공인지 복수전공인지
        sid = intent1.getIntExtra("studentId", 1);
        spw = intent1.getStringExtra("studentPw");
        syear = intent1.getIntExtra("studentYear", 1);
        smajor = intent1.getIntExtra("studentMajor", 1);//전공

        subMajor = intent1.getIntExtra("subMajor", -1);//부전공
        doubleMajor = intent1.getIntExtra("doubleMajor", -1);//복수전공
        classlist = intent1.getParcelableArrayListExtra("classlist");
        ex_classlist = new ArrayList<>();//들은거

        if (isMajor == 0)
            choosetable(smajor);//전공 db열고 table선택
        else if (isMajor == 1) {
            choosetable(subMajor);
            isMajor = 3;
        } else {
            choosetable(doubleMajor);
            isMajor = 3;
        }
        rowLayout = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        t = (TableLayout) findViewById(R.id.result_table);

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
        mContext = this;
    }

    private void changeLevel(int pos) {
        level = pos;
    }

    public void choosetable(int major) {
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

        TextView textView = (TextView) findViewById(R.id.major_text);
        String[] major_text = getResources().getStringArray(R.array.학과);
        String[] arr1 = major_text[major].split(" ");//단과대 빼고 학과만

        textView.setText(arr1[1] + " ");
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
                String[] arr = c.getString(8).split("\\n");//줄바꿈제거..
                timestr = c.getString(5) + c.getString(6) + ' ' + c.getString(7);
                for (int i = 0; i < arr.length; i++)
                    timestr += arr[i];
            } else
                timestr = c.getString(5) + c.getString(6);

            String[] arr1 = c.getString(6).split("\\.|,|\\n");//시간1 .이나,으로 구분하고

            if (c.getString(8) != null) {
                String[] arr2 = c.getString(8).split("\\.|,|\\n");//시간2 .이나,으로 구분하고 + 공백도 가끔있음
                for (int i = 0; i < arr2.length; i++) {
                    if (!arr2[i].equals(""))//공백으로 자르고나면 ""남음...
                        t.put(m + Integer.parseInt(arr2[i]));
                }
            }//null 예외처리!!!!

            //자른거
            for (int i = 0; i < arr1.length; i++) {
                try {
                    if (Integer.parseInt(arr1[i]) <= 9) //10교시부터는 안넣음
                        t.put(n + Integer.parseInt(arr1[i]));
                } catch (Exception e) {//이상한거 나오면
                    t.put(50);
                }
            }
        } else {//인강인 경우..
            n = 50;
            t.put(n);
        }
        t.putCode(c.getString(0));//학정번호도 넣어봅시다
        t.setEsu(c.getString(2));//이수
        t.setCredit(Integer.parseInt(c.getString(3)));//학점
        if (c.getString(4) != null)
            t.setprof(c.getString(4));//교수명도
        t.setTimestr(timestr);
        s.put(t);
    }

    protected void showList() {
        num = 0;
        t.removeAllViews();//싹 지우고 출력

        try {
            if (table.equalsIgnoreCase("software") || table.equalsIgnoreCase("cs")) {
                if (level != 0 && level != 4) {
                    database = openOrCreateDatabase("sw.db", MODE_PRIVATE, null);
                    c = database.rawQuery("select distinct 과목명, 이수, 학점 from software where 학정번호 like '_____" + level + "%'", null);
                }else if (level == 4){
                    database = openOrCreateDatabase("ei.db", MODE_PRIVATE, null);
                    c = database.rawQuery("select distinct 과목명, 이수, 학점 from cs where 학정번호 like '_____" + level + "%'", null);
                }
                else{
                    database = openOrCreateDatabase("ei.db", MODE_PRIVATE, null);
                    c = database.rawQuery("select distinct 과목명, 이수, 학점 from common", null);
                }
            } else {
                if (level != 0)
                    c = database.rawQuery("select distinct 과목명, 이수, 학점 from " + table + " where 학정번호 like '_____" + level + "%'", null);
                else
                    c = database.rawQuery("select distinct 과목명, 이수, 학점 from common", null);
            }

            if (c != null) {
                int count = c.getCount();//개수
                if (count != 0) {

                    TableRow tbrow0 = new TableRow(this);
                    TextView tv0 = new TextView(this);
                    tv0.setText(" 과목명 ");
                    tv0.setTextSize(20);
                    tv0.setTextColor(Color.WHITE);
                    //tv0.setBackgroundColor(124 - 224 - 134);
                    tv0.setGravity(Gravity.CENTER);
                    tv0.setBackgroundResource(R.drawable.rounded_corners);
                    tv0.setPadding(0,7,0,8);
                    GradientDrawable drawable = (GradientDrawable) tv0.getBackground();
                    drawable.setColor(Color.parseColor("#EA5446"));
                    tbrow0.addView(tv0);

                    TextView tv1 = new TextView(this);
                    tv1.setText(" 이 수 ");
                    tv1.setTextSize(20);
                    tv1.setTextColor(Color.WHITE);
                    tv1.setBackgroundColor(Color.parseColor("#EA5446"));
                    tv1.setGravity(Gravity.CENTER);
                    tv1.setBackgroundResource(R.drawable.rounded_corners3);
                    tv1.setPadding(0,7,0,8);
                    GradientDrawable drawable1 = (GradientDrawable) tv1.getBackground();
                    drawable1.setColor(Color.parseColor("#EA5446"));
                    tbrow0.addView(tv1);

                    TextView tv2 = new TextView(this);
                    tv2.setText(" 학 점 ");
                    tv2.setTextSize(20);
                    //tv2.setBackgroundColor(124 - 224 - 134);
                    tv2.setTextColor(Color.WHITE);
                    tv2.setGravity(Gravity.CENTER);
                    tv2.setBackgroundResource(R.drawable.rounded_corners2);
                    tv2.setPadding(0,7,0,8);
                    GradientDrawable drawable2 = (GradientDrawable) tv2.getBackground();
                    drawable2.setColor(Color.parseColor("#EA5446"));
                    tbrow0.addView(tv2);

                    t.addView(tbrow0);

                    TableRow.LayoutParams tparams1 = new TableRow.LayoutParams(570, ViewGroup.LayoutParams.WRAP_CONTENT);
                    TableRow.LayoutParams tparams2 = new TableRow.LayoutParams(120, ViewGroup.LayoutParams.WRAP_CONTENT);

                    tr = new TableRow[count];
                    text = new TextView[count][3];

                    for (int i = 0; i < count; i++) {
                        c.moveToNext();
                        tr[i] = new TableRow(this);
                        for (int j = 0; j < 3; j++) {
                            text[i][j] = new TextView(this);
                            text[i][j].setText(c.getString(j));

                            text[i][j].setTextSize(20);
                            text[i][j].setTextColor(Color.BLACK);
                            text[i][j].setGravity(Gravity.CENTER);
                            text[i][j].setBackgroundResource(R.drawable.cell);
                            text[i][j].setSingleLine(true);
                            text[i][j].setEllipsize(TextUtils.TruncateAt.MIDDLE);
                            text[i][j].setPadding(0,7,0,8);

                            for (int l = 0; l < selected[level].size(); l++) {
                                if (selected[level].get(l).equals(i))//선택된과목이면
                                    text[i][j].setBackgroundResource(R.drawable.select_cell);
                            }
                            text[i][j].setTag(i);

                            text[i][j].setOnClickListener(new Button.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    num = (Integer) v.getTag();//선택된 번호
                                    if (v.getBackground().getConstantState() == getResources().getDrawable(R.drawable.select_cell).getConstantState()) {//다시누르면
                                        for (int k = 0; k < 3; k++)
                                            text[num][k].setBackgroundResource(R.drawable.cell);//줄 선택 해제
                                        c.moveToPosition(num);
                                        for (int l = 0; l < selected[level].size(); l++) {
                                            if (selected[level].get(l).equals(num))
                                                selected[level].remove(l);
                                        }
                                        for (int l = 0; l < ex_classlist.size(); l++) {
                                            if (ex_classlist.get(l).getName().equals(c.getString(0)))
                                                ex_classlist.remove(l);//배열에서 찾아서 지운다
                                        }
                                    } else {
                                        c.moveToPosition(num);
                                        selected[level].add(num);
                                        cs = new ClassSubject(c.getString(0));//그 과목명으로
                                        TimeArr t = new TimeArr();
                                        t.setEsu(c.getString(1));//이수
                                        t.setCredit(Integer.parseInt(c.getString(2)));//학점
                                        cs.put(t);
                                        ex_classlist.add(cs); //배열에 넣는다

                                        for (int k = 0; k < 3; k++)
                                            text[num][k].setBackgroundResource(R.drawable.select_cell);//선택한 줄 색칠
                                    }
                                }
                            });
                            if (j == 0)
                                tr[i].addView(text[i][j], tparams1);//칸추가
                            else
                                tr[i].addView(text[i][j], tparams2);//칸추가
                        }
                        t.addView(tr[i], rowLayout);//행추가
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }

    }

    public boolean checkCredit(ArrayList<ClassSubject> list, ClassSubject cs) {
        int Max = 19;
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i).getTimearr(0).getCredit();
        }
        sum += cs.getTimearr(0).getCredit();//넣을 과목

        if (Integer.parseInt(sid.toString().substring(2, 4)) <= 16) {
            Max = 21;
        }

        if (sum > Max)//일단
            return false;//못넣게
        else
            return true;
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, Learned_Sub_Select.class);//전공선택하러
        if (subMajor != -1 && isMajor == 0) {
            intent = new Intent(this, major_select.class);//부전공과목고르러
            isMajor = 1;
        }
        if (doubleMajor != -1 && isMajor == 0) {
            intent = new Intent(this, major_select.class);//복수전공과목고르러
            isMajor = 2;
        }
        if (isMajor == 3)
            isMajor = 0;//이제 전공과목 들을거 선택하러

        intent.putExtra("isMajor", isMajor);
        intent.putExtra("studentId", sid);
        intent.putExtra("studentPw", spw);
        intent.putExtra("studentYear", syear);
        intent.putExtra("studentMajor", smajor);

        intent.putExtra("subMajor", subMajor);
        intent.putExtra("doubleMajor", doubleMajor);
        intent.putParcelableArrayListExtra("ex_classlist", ex_classlist);//들은거
        intent.putParcelableArrayListExtra("classlist", classlist);

        startActivity(intent);
    }
}

