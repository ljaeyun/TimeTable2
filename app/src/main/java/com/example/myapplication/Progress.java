package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Progress extends AppCompatActivity {
    Integer sid, smajor;
    SQLiteDatabase database;
    String table = "business";
    Cursor c;
    TableRow tr[];
    TextView text[][];
    TableLayout.LayoutParams rowLayout;
    TableLayout t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);

        Intent intent1 = getIntent();
        sid = intent1.getIntExtra("studentId", 1);
        smajor = intent1.getIntExtra("studentMajor", 1);


        TextView textView1 = (TextView) findViewById(R.id.myid);
        TextView textView2 = (TextView) findViewById(R.id.mymajor);
        TextView textView3 = (TextView) findViewById(R.id.mycredit);
        TextView textView4 = (TextView) findViewById(R.id.majorcredit);
        TextView textView5 = (TextView) findViewById(R.id.myallcredit);
        TextView textView6 = (TextView) findViewById(R.id.allcredit);

        Button close_button = (Button) findViewById(R.id.button_close3);
        close_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String id = sid.toString().substring(2, 4);
        textView1.setText(id);

        String[] major_text = getResources().getStringArray(R.array.학과);
        String[] arr1 = major_text[smajor].split(" ");//단과대 빼고 학과만
        textView2.setText(arr1[1] + " ");

        choosetable(smajor);//db다시
        if (smajor >= 3 && smajor <= 5)
            table = "engineer";//공과대
        else if (smajor >= 6 && smajor <= 8)
            table = "sw";//소융대
        else if (smajor >= 21 && smajor <= 28)
            table = "ei";//전정공대
        else if (smajor == 12)
            table = "media";
        else if (smajor == 14)
            table = "eng";//예외처리

        if (id.equals("18"))
            id = "17";//17학번 18학번 기준같음
        if (id.equalsIgnoreCase("15"))
            id = "14";//14 15 기준 같음

        database.close();
        database = openOrCreateDatabase("credit.db", MODE_PRIVATE, null);

        try {
            c = database.rawQuery("select 전공, 졸업 from 졸업이수 where 학번 = '" + id + "' and 학과 = '" + table+"'", null);
            if (c != null) {
                int count = c.getCount();//개수
                if (count != 0) {
                    for (int i = 0; i < count; i++) {
                        c.moveToNext();
                        textView4.setText(c.getString(0));
                        textView6.setText(c.getString(1));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }
        database.close();

        textView3.setText("55");//나중에 받아온다
        textView5.setText("135");//나중에 받아온다

        choosetable(smajor);//db다시
        t = (TableLayout) findViewById(R.id.pilsu);
        rowLayout = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        try {
            c = database.rawQuery("select distinct 과목명, 이수, 학점 from " + table + " where 이수 like '%필'", null);
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

                            if (j == 0) {
                                text[i][j].setWidth(650);
                            }
                            if (j == 1) {
                                text[i][j].setWidth(150);
                            }
                            if (j == 2) {
                                text[i][j].setWidth(150);
                            }

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

    }
}
