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
import android.text.TextUtils;
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

import java.util.ArrayList;

public class ClassListActivity extends Activity {
    SQLiteDatabase database;
    String classname;
    int count = 0, num = 0;
    ArrayList<ClassSubject> arr;
    ClassSubject cs;
    Cursor c;
    TableRow tr[];
    TextView text[][];
    TableLayout t;
    TableLayout.LayoutParams rowLayout;
    TableRow tableRow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup);

        Intent intent1 = getIntent();
        classname = intent1.getStringExtra("classname");
        database = openOrCreateDatabase("test.db", MODE_PRIVATE, null);




        rowLayout = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        t = (TableLayout) findViewById(R.id.result_table);
        tableRow = (TableRow) findViewById(R.id.tablerow);
        arr = new ArrayList<>();

        makedb();

        try {
            c = database.rawQuery("select 학정번호, 과목명, 이수, 학점, 담당교수, 요일1,시간1,요일2,시간2,typecode from allclass where 과목명 like '%" + classname + "%'", null);
            if (c != null) {
                count = c.getCount();//개수
                if (count != 0) {
                    tr = new TableRow[count];
                    text = new TextView[count][5];


                    TableRow tbrow0 = new TableRow(this);
                    TextView tv0 = new TextView(this);
                    tv0.setText("학정번호");
                    tv0.setTextSize(15);
                    tv0.setTextColor(Color.BLACK);
                    tv0.setBackgroundColor(124 - 224 - 134);
                    tv0.setGravity(Gravity.CENTER);
                    tbrow0.addView(tv0);

                    TextView tv1 = new TextView(this);
                    tv1.setText("과목명");
                    tv1.setTextSize(15);
                    tv1.setTextColor(Color.BLACK);
                    tv1.setBackgroundColor(124 - 224 - 134);
                    tv1.setGravity(Gravity.CENTER);
                    tbrow0.addView(tv1);

                    TextView tv2 = new TextView(this);
                    tv2.setText("이수");
                    tv2.setTextSize(15);
                    tv2.setBackgroundColor(124 - 224 - 134);
                    tv2.setTextColor(Color.BLACK);
                    tv2.setGravity(Gravity.CENTER);
                    tbrow0.addView(tv2);

                    TextView tv3 = new TextView(this);
                    tv3.setText("교수");
                    tv3.setTextSize(15);
                    tv3.setBackgroundColor(124 - 224 - 134);
                    tv3.setTextColor(Color.BLACK);
                    tv3.setGravity(Gravity.CENTER);
                    tbrow0.addView(tv3);

                    TextView tv4 = new TextView(this);
                    tv4.setText("시간");
                    tv4.setTextSize(15);
                    tv4.setBackgroundColor(124 - 224 - 134);
                    tv4.setTextColor(Color.BLACK);
                    tv4.setGravity(Gravity.CENTER);
                    tbrow0.addView(tv4);

                    t.addView(tbrow0);



                    for (int i = 0; i < count; i++) {
                        c.moveToNext();
                        tr[i] = new TableRow(this);
                        for (int j = 0; j < 5; j++) {
                            text[i][j] = new TextView(this);
                            if (j == 4) {//시간 출력
                                if (c.getString(5) != null) {
                                    if (c.getString(7) != null) {
                                        text[i][j].setText(c.getString(5) + c.getString(6) + c.getString(7) + c.getString(8));
                                    }
                                    else
                                        text[i][j].setText(c.getString(5) + c.getString(6));
                                } else
                                    text[i][j].setText(" ");
                            } else if (j == 3)
                                text[i][j].setText(c.getString(4));
                            else
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
                                        for (int k = 0; k < 5; k++)
                                            text[num][k].setBackgroundResource(R.drawable.cell_shape);//줄 선택 해제
                                        c.moveToPosition(num);
                                        for (int l = 0; l < arr.size(); l++) {
                                            if (arr.get(l).getName().equals(c.getString(1)))
                                                arr.remove(l);//배열에서 찾아서 지운다
                                        }
                                    } else {//처음 누르면
                                        for (int k = 0; k < 5; k++)
                                            text[num][k].setBackgroundResource(R.drawable.select_cell);//선택한 줄 색칠
                                        c.moveToPosition(num);
                                        cs = new ClassSubject(c.getString(1));
                                        ((major_select) major_select.mContext).timecal(cs, c);
                                        cs.setTypecode(c.getInt(9));
                                        arr.add(cs); //배열에 넣는다
                                    }
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
                intent.putParcelableArrayListExtra("plus", arr);
                setResult(RESULT_OK, intent);
                finish();//닫기
            }
        });
    }

    private void makedb() {

        if (!((FirstSelect) FirstSelect.mContext).isTableExists("allclass")) {//모든 전공+교양 allclass
            database.execSQL("create table allclass as select * from allsubject");

            database = openOrCreateDatabase("biz.db", MODE_PRIVATE, null);

            database.execSQL("create table test1 as select * from business");
            database.execSQL("create table test2 as select * from itrade");
            database.execSQL("create table test3 as select * from common");
            query(1, 3, "biz");

            database = openOrCreateDatabase("engineer.db", MODE_PRIVATE, null);

            database.execSQL("create table test4 as select * from archi");
            database.execSQL("create table test5 as select * from arching");
            database.execSQL("create table test6 as select * from chemng");
            database.execSQL("create table test7 as select * from env");
            database.execSQL("create table test8 as select * from common");
            query(4, 8, "engineer");

            database = openOrCreateDatabase("sw.db", MODE_PRIVATE, null);

            database.execSQL("create table test9 as select * from cie");
            database.execSQL("create table test10 as select * from software");
            database.execSQL("create table test11 as select * from ic");
            database.execSQL("create table test12 as select * from common");
            query(9, 12, "sw");

            database = openOrCreateDatabase("hss.db", MODE_PRIVATE, null);

            database.execSQL("create table test13 as select * from kor");
            database.execSQL("create table test14 as select * from ci");
            database.execSQL("create table test15 as select * from media");
            database.execSQL("create table test16 as select * from mediacomm");
            database.execSQL("create table test17 as select * from psy");
            database.execSQL("create table test18 as select * from engind");
            database.execSQL("create table test19 as select * from eng");
            database.execSQL("create table test20 as select * from common");
            query(13, 20, "hss");

            database = openOrCreateDatabase("natural.db", MODE_PRIVATE, null);

            database.execSQL("create table test21 as select * from sports");
            database.execSQL("create table test22 as select * from math");
            database.execSQL("create table test23 as select * from ep");
            database.execSQL("create table test24 as select * from infocontents");
            database.execSQL("create table test25 as select * from chemi");
            database.execSQL("create table test26 as select * from common");
            query(21, 26, "natural");

            database = openOrCreateDatabase("ei.db", MODE_PRIVATE, null);

            database.execSQL("create table test27 as select * from robot");
            database.execSQL("create table test28 as select * from electric");
            database.execSQL("create table test29 as select * from ee");
            database.execSQL("create table test30 as select * from radiowave");
            database.execSQL("create table test31 as select * from snme");
            database.execSQL("create table test32 as select * from elcomm");
            database.execSQL("create table test33 as select * from ce");
            database.execSQL("create table test34 as select * from cs");
            database.execSQL("create table test35 as select * from common");
            query(27, 35, "ei");

            database = openOrCreateDatabase("kwlaw.db", MODE_PRIVATE, null);

            database.execSQL("create table test36 as select * from intern");
            database.execSQL("create table test37 as select * from law");
            database.execSQL("create table test38 as select * from asset");
            database.execSQL("create table test39 as select * from pa");
            database.execSQL("create table test40 as select * from common");
            query(36, 40, "kwlaw");
        }
    }

    private void query(int a, int b, String dbname) {
        for (int i = a; i <= b; i++)
            database.execSQL("alter table test" + i + " add column typecode integer default 0");

        database = openOrCreateDatabase("test.db", MODE_PRIVATE, null);
        database.execSQL("ATTACH DATABASE '/data/data/com.example.myapplication/databases/" + dbname + ".db' as dbdb");

        for (int i = a; i <= b; i++)
            database.execSQL("insert into allclass select * from dbdb.test" + i);//모두 넣는다

        for (int i = a; i <= b; i++)
            database.execSQL("drop table test" + i);//모두 지운다
    }
}
