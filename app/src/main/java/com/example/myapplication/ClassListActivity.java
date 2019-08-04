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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import android.content.Context;

public class ClassListActivity extends Activity {
    SQLiteDatabase database;
    String classname;
    int count = 0, num = 0;
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
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,
                        100);
        //ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,100);
        LinearLayout ll = new LinearLayout(this);
        TableLayout.LayoutParams rowLayout = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TableLayout t = (TableLayout) findViewById(R.id.result_table);
        TableRow tableRow = (TableRow) findViewById(R.id.tablerow);

        try {
            c = database.rawQuery("select 학정번호, 과목명, 이수, 담당교수, 요일1,시간1,요일2,시간2 from allclass where 과목명 like '%" + classname + "%'", null);//일단 한 테이블에 모든 교양넣었음
            if (c != null) {
                count = c.getCount();//개수
                if (count != 0) {
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
                                public void onClick(View v) {//여러개 선택하면 어떡하징..
                                    num = (Integer) v.getTag();//선택된 번호
                                    for (int k = 0; k < 5; k++)
                                        text[num][k].setBackgroundResource(R.drawable.select_cell);//선택한 줄 색칠
                                    c.moveToPosition(num);
                                    cs = new ClassSubject(c.getString(1));
                                    ((TimeTableActivity) TimeTableActivity.mContext).timecal(cs, c);
                                    //finish();//바로 닫히면서 추가하게 할지 추가버튼을 만들지
                                }
                            });

                            tr[i].addView(text[i][j]);
                        }
                        t.addView(tr[i], rowLayout);
                    }
                } else {//검색한 과목이 없을때
                    tableRow.setVisibility(View.GONE);
                    TextView textView = new TextView(this);
                    textView.setText("없습니다");//나중에 수정
                    textView.setTextSize(20);
                    textView.setTextColor(Color.BLACK);
                    t.addView(textView);
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
                intent.putExtra("plus", cs);
                setResult(RESULT_OK, intent);
                finish();//닫기
            }
        });
    }
}
