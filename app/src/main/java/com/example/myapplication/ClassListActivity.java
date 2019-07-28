package com.example.myapplication;

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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ClassListActivity extends Activity {
    SQLiteDatabase database;
    String classname;
    int count = 0,num=0;
    ClassSubject cs;
    Cursor c;
    TableRow tr[];
    TextView text[][];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup);

        Intent intent1 = getIntent();
        classname = intent1.getStringExtra("classname");
        database = openOrCreateDatabase("test.db", MODE_PRIVATE, null);

        TableLayout.LayoutParams rowLayout = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TableLayout t = (TableLayout) findViewById(R.id.result_table);

        try {
            c = database.rawQuery("select 학정번호, 과목명, 이수, 담당교수, 요일1,시간1,요일2,시간2 from allclass where 과목명 like '%" + classname + "%'", null);//일단 한 테이블에 모든 교양넣었음
            if (c != null) {
                count = c.getCount();//개수
                tr = new TableRow[count];
                text = new TextView[count][5];

                for (int i = 0; i < count; i++) {
                    c.moveToNext();
                    tr[i] = new TableRow(this);
                    for (int j = 0; j < 5; j++) {
                        text[i][j] = new TextView(this);
                        if (j == 4) {
                            if (c.getString(6) != null)
                                text[i][j].setText(c.getString(4) + c.getString(5) + c.getString(6) + c.getString(7));
                            else
                                text[i][j].setText(c.getString(4) + c.getString(5));
                        } else
                            text[i][j].setText(c.getString(j));
                        text[i][j].setTextSize(15);
                        text[i][j].setTextColor(Color.BLACK);
                        text[i][j].setGravity(Gravity.CENTER);
                        text[i][j].setBackgroundResource(R.drawable.cell_shape);
                        text[i][j].setTag(i);
                        text[i][j].setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v.setBackgroundResource(R.drawable.select_cell);//선택한 행 색바뀌게 수정해야함
                                num = (Integer)v.getTag();//선택된 번호
                                cs = new ClassSubject(c.getString(1));
                                c.moveToPosition(num);
                                timecal(cs,c);
                                //finish();//바로 닫히면서 추가하게 할지 추가버튼을 만들지
                            }
                        });

                        tr[i].addView(text[i][j]);
                    }
                    t.addView(tr[i], rowLayout);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }

        Button button_close = (Button) findViewById(R.id.button_close2);
        button_close.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("plus",cs);
                setResult(RESULT_OK, intent);
                finish();//닫기
            }
        });
    }

    public void timecal(ClassSubject s, Cursor c) {
        int n = 0, m = 0;// 월: 0~9
        String timestr = null;//시간 문자열
        if (c.getString(4).equals("화")) {//화:10~19
            n = 10;
        } else if (c.getString(4).equals("수")) {
            n = 20;
        } else if (c.getString(4).equals("목")) {
            n = 30;
        } else if (c.getString(4).equals("금")) {
            n = 40;
        }
        if (c.getString(6) != null) {
            if (c.getString(6).equals("화")) {
                m = 10;
            } else if (c.getString(6).equals("수")) {
                m = 20;
            } else if (c.getString(6).equals("목")) {
                m = 30;
            } else if (c.getString(6).equals("금")) {
                m = 40;
            }
            timestr = c.getString(4) + c.getString(5) + ' ' + c.getString(6) + c.getString(7);
        } else
            timestr = c.getString(4) + c.getString(5);


        TimeArr t = new TimeArr();

        String[] arr1 = c.getString(5).split("\\.|,|\\n");//시간1 .이나,으로 구분하고

        if (c.getString(7) != null) {
            String[] arr2 = c.getString(7).split("\\.|,|\\n");//시간2 .이나,으로 구분하고 + 공백도 가끔있음
            for (int i = 0; i < arr2.length; i++) {
                if (arr2[i].equals("") == false)//공백으로 자르고나면 ""남음...
                    t.put(m + Integer.parseInt(arr2[i]));
            }
        }//null 예외처리!!!!

        //자른거
        for (int i = 0; i < arr1.length; i++)
            t.put(n + Integer.parseInt(arr1[i]));

        t.putCode(c.getString(0));//학정번호도 넣어봅시다
        t.setEsu(c.getString(2));//이수
        t.setprof(c.getString(3));//교수명도
        t.setTimestr(timestr);
        s.put(t);
    }
}
