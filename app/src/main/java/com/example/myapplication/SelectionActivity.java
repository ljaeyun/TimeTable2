package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectionActivity extends AppCompatActivity {
    SearchView searchview;
    SQLiteDatabase database;
    TableLayout tableLayout;
    TextView text2;
    TextView t1,t2;
    String className="";
    Integer freeDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        database = openOrCreateDatabase("test.db", MODE_PRIVATE, null);

        Button button_close = (Button) findViewById(R.id.button_close);
        Button button_apply = (Button) findViewById(R.id.button_apply);
        searchview = (SearchView) findViewById(R.id.findclass);
        text2 = (TextView) findViewById(R.id.cn1);
        t1 = (TextView) findViewById(R.id.select1);
        t2 = (TextView) findViewById(R.id.select2);

//        tableLayout = (TableLayout) findViewById(R.id.classTable);
//        tableLayout.setOnClickListener(new Button.OnClickListener(){//넣을과목 검색하고 추가할 과목 고름
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()){
//                    case R.id.select1:
//                        Toast.makeText(getApplicationContext(), 1 + "kk", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.select2:
//                        Toast.makeText(getApplicationContext(), 2 + "kk", Toast.LENGTH_SHORT).show();
//                        break;
//                        default:
//                            Toast.makeText(getApplicationContext(), 3 + "kk", Toast.LENGTH_SHORT).show();
//break;
//                }
//            }
//        });
//

        findViewById(R.id.mon).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                printChecked(view);
            }
        });
        findViewById(R.id.tue).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                printChecked(view);
            }
        });
        findViewById(R.id.wen).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                printChecked(view);
            }
        });
        findViewById(R.id.thu).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                printChecked(view);
            }
        });
        findViewById(R.id.fri).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                printChecked(view);
            }
        });

        text2.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), 1 + "kk", Toast.LENGTH_SHORT).show();

            }
        });

        getClassName();
        button_close.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//닫기
            }
        });

        button_apply.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {//적용버튼 추가
                //필수랑 공강요일이랑 겹치면 팝업창뜨게 할까 https://ghj1001020.tistory.com/9
                Intent intent = new Intent();
                intent.putExtra("Day",freeDay);//공강요일 넘김
                setResult(RESULT_OK,intent);
                finish();//닫기
            }
        });
    }

    public void printChecked(View view)
    {
        final CheckBox cb1 = (CheckBox)findViewById(R.id.mon);
        final CheckBox cb2 = (CheckBox)findViewById(R.id.tue);
        final CheckBox cb3 = (CheckBox)findViewById(R.id.wen);
        final CheckBox cb4 = (CheckBox)findViewById(R.id.thu);
        final CheckBox cb5 = (CheckBox)findViewById(R.id.fri);
        Integer result = 0 ;//정수말고 다른걸로
        if(cb1.isChecked() == true) result = 1;
        if(cb2.isChecked() == true) result = 2;
        if(cb3.isChecked() == true) result = 3;
        if(cb4.isChecked() == true) result = 4;
        if(cb5.isChecked() == true) result = 5;
        freeDay=result;
    }

    private void getClassName() {

        //searchview.setSubmitButtonEnabled(true);//확인버튼 활성화

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                int num = 0;

                try {
                    Cursor c1 = database.rawQuery("select 과목명, 학정번호 from allclass where 과목명 like '%"+s+"%'", null);//일단 한 테이블에 모든 교양넣었음
                    if (c1 != null) {
                        num = c1.getCount();
                        c1.moveToNext();
                        text2.setText("d"+num);//일단 개수출력

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("", e.getMessage());
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}
