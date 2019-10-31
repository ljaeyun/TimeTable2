package com.example.myapplication;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class Learned_Sub_Select extends AppCompatActivity {
    public static Context mContext = null;

    SQLiteDatabase database;
    String table = "business";
    Integer sid, syear, smajor, minorNum, subMajor, doubleMajor, isMajor = 0;
    String spw;
    Cursor c;
    ClassSubject cs;
    ArrayList<ClassSubject> classlist,ex_classlist;
    ArrayList<Integer> selected[] = new ArrayList[5];
    int num = 0, level = 0;
    TableRow tr[];
    TextView text[][];
    TableLayout.LayoutParams rowLayout;
    TableLayout t;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.major_select);

        classlist = new ArrayList<>();
        ex_classlist = new ArrayList<>();
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

        minorNum = intent1.getIntExtra("minorNum", 0);
        subMajor = intent1.getIntExtra("subMajor", -1);//부전공
        doubleMajor = intent1.getIntExtra("doubleMajor", -1);//복수전공
        classlist = intent1.getParcelableArrayListExtra("classlist");
        ex_classlist = intent1.getParcelableArrayListExtra("ex_classlist");//들은거

        if (isMajor == 0)
            choosetable(smajor);//전공 db열고 table선택
        else if (isMajor == 1)
            choosetable(subMajor);
        else
            choosetable(doubleMajor);

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

        TextView textView = (TextView) findViewById(R.id.major_text);
        TextView textView2 = (TextView) findViewById(R.id.select_page_text);
        String[] major_text = getResources().getStringArray(R.array.학과);
        String[] arr1 = major_text[major].split(" ");//단과대 빼고 학과만

        textView.setText(arr1[1] + " ");
        textView2.setText("전공 선택 페이지");
    }

    private void findClasses(String classname) {
        Cursor c1;
        try {
            if (level != 0)
                c1 = database.rawQuery("select 학정번호, 과목명, 이수,학점, 담당교수, 요일1,시간1,요일2,시간2 from " + table + " where 학정번호 like '_____" + level + "%'", null);
            else
                c1 = database.rawQuery("select 학정번호, 과목명, 이수,학점, 담당교수, 요일1,시간1,요일2,시간2 from common", null);
            if (c1 != null) {
                int num = c1.getCount();//개수
                if (num != 0) {
                    for (int i = 0; i < num; i++) {
                        c1.moveToNext();
                        if (c1.getString(1).equalsIgnoreCase(classname)) {//이름같은거 찾아서
                            ((major_select)major_select.mContext).timecal(cs, c1);//분반을 저장한다
                            cs.setTypecode(0);
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

        try {
            if (level != 0)
                c = database.rawQuery("select distinct 과목명, 이수, 학점 from " + table + " where 학정번호 like '_____" + level + "%'", null);
            else
                c = database.rawQuery("select distinct 과목명, 이수, 학점 from common", null);
            if (c != null) {
                int count = c.getCount();//개수
                if (count != 0) {

                    TableRow tbrow0 = new TableRow(this);
                    TextView tv0 = new TextView(this);
                    tv0.setText(" 과목명 ");
                    tv0.setTextSize(25);
                    tv0.setTextColor(Color.BLACK);
                    tv0.setBackgroundResource(android.R.color.holo_green_light);
                    tv0.setGravity(Gravity.CENTER);
                    tbrow0.addView(tv0);
                    TextView tv1 = new TextView(this);
                    tv1.setText(" 이 수 ");
                    tv1.setTextSize(25);
                    tv1.setTextColor(Color.BLACK);
                    tv1.setBackgroundResource(android.R.color.holo_green_light);
                    tv1.setGravity(Gravity.CENTER);
                    tbrow0.addView(tv1);
                    TextView tv2 = new TextView(this);
                    tv2.setText(" 학 점 ");
                    tv2.setTextSize(25);
                    tv2.setBackgroundResource(android.R.color.holo_green_light);
                    tv2.setTextColor(Color.BLACK);
                    tv2.setGravity(Gravity.CENTER);
                    tbrow0.addView(tv2);
                    t.addView(tbrow0);

                    tr = new TableRow[count];
                    text = new TextView[count][3];


                    for (int i = 0; i < count; i++) {
                        c.moveToNext();
                        tr[i] = new TableRow(this);
                        if (checkSelect(ex_classlist, c.getString(0))) { //선택한 과목 제외 출력
                            for (int j = 0; j < 3; j++) {
                                text[i][j] = new TextView(this);
                                text[i][j].setText(c.getString(j));

                                text[i][j].setTextSize(20);
                                text[i][j].setTextColor(Color.BLACK);
                                text[i][j].setGravity(Gravity.CENTER);
                                text[i][j].setBackgroundResource(R.drawable.cell_shape);

                                if (j == 0) {
                                    text[i][j].setWidth(640);
                                }
                                if (j == 1) {
                                    text[i][j].setWidth(150);
                                }
                                if (j == 2) {
                                    text[i][j].setWidth(150);
                                }

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
                                                text[num][k].setBackgroundResource(R.drawable.cell_shape);//줄 선택 해제
                                            c.moveToPosition(num);
                                            for (int l = 0; l < selected[level].size(); l++) {
                                                if (selected[level].get(l).equals(num))
                                                    selected[level].remove(l);
                                            }
                                            for (int l = 0; l < classlist.size(); l++) {
                                                if (classlist.get(l).getName().equals(c.getString(0)))
                                                    classlist.remove(l);//배열에서 찾아서 지운다
                                            }
                                        } else {//처음 누르면
                                            c.moveToPosition(num);
                                            selected[level].add(num);
                                            cs = new ClassSubject(c.getString(0));//그 과목명으로
                                            findClasses(c.getString(0));//분반저장
                                            if (((major_select)major_select.mContext).checkCredit(classlist, cs)) {
                                                classlist.add(cs); //배열에 넣는다
                                                for (int k = 0; k < 3; k++)
                                                    text[num][k].setBackgroundResource(R.drawable.select_cell);//선택한 줄 색칠
                                            }
                                        }
                                    }
                                });
                                tr[i].addView(text[i][j]);
                            }
                            t.addView(tr[i], rowLayout);
                        }
                    }
                    if( t.getChildCount()==1) //출력할 과목이 없다면
                        t.removeView(tbrow0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }

    }

    public boolean checkSelect(ArrayList<ClassSubject> classlist, String s) {
        for (int i = 0; i < classlist.size(); i++) {
            if ( classlist.get(i).getName().equalsIgnoreCase(s) ) // 선택한 과목이랑 이름이 겹치면
                return false;
        }
        return true;  // 안겹치면
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, TimeTableActivity.class);//시간표 생성
        if (subMajor != -1 && isMajor == 0) {
            intent = new Intent(this, Learned_Sub_Select.class);//부전공과목고르러
            isMajor = 1;
        }
        if (doubleMajor != -1 && isMajor == 0) {
            intent = new Intent(this, Learned_Sub_Select.class);//복수전공과목고르러
            isMajor = 2;
        }
        intent.putExtra("isMajor", isMajor);
        intent.putExtra("studentId", sid);
        intent.putExtra("studentPw", spw);
        intent.putExtra("studentYear", syear);
        intent.putExtra("studentMajor", smajor);

        intent.putExtra("minorNum", minorNum);
        intent.putExtra("subMajor", subMajor);
        intent.putExtra("doubleMajor", doubleMajor);
        intent.putParcelableArrayListExtra("ex_classlist", ex_classlist);
        intent.putParcelableArrayListExtra("classlist", classlist);

        startActivity(intent);
    }
}
