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

import java.util.ArrayList;

public class SelectionActivity extends AppCompatActivity {
    SearchView searchview;
    SQLiteDatabase database;
    TableLayout tableLayout;
    EditText credits_min, credits_max;
    TextView text2;
    TextView t1, t2;
    String className = "";
    Integer freeDay;
    ArrayList<ArrayList<ClassSubject>> rrr;//이름.. 나중에 수정..
    ArrayList<ArrayList<ClassSubject>> arr;
//    ArrayList<ClassSubject> cs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        database = openOrCreateDatabase("test.db", MODE_PRIVATE, null);

        Intent intent1 = getIntent();
        ArrayList<ClassSubject> classList = intent1.getParcelableArrayListExtra("now_list");//전달받은 과목 리스트

        ArrayList<ClassSubject> cs = new ArrayList<>();
        arr = new ArrayList<>();

        combination(classList, cs, classList.size(), 2, 0);

        credits_max = (EditText) findViewById(R.id.credits_max);
        credits_min = (EditText) findViewById(R.id.credits_min);

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

        findViewById(R.id.mon).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                printChecked(view);
            }
        });
        findViewById(R.id.tue).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                printChecked(view);
            }
        });
        findViewById(R.id.wen).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                printChecked(view);
            }
        });
        findViewById(R.id.thu).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                printChecked(view);
            }
        });
        findViewById(R.id.fri).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                printChecked(view);
            }
        });

        text2.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), 1 + "kk", Toast.LENGTH_SHORT).show();

            }
        });

        getClassName();
        //  getTimeTable();
        button_close.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//닫기
            }
        });

        button_apply.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {//적용버튼 추가
                // Intent intent = new Intent(getApplicationContext(), SelectionActivity.class);//다른 화면을 열어줄 계획
                // startActivityForResult(intent, 100);
                Intent intent = new Intent();
                intent.putExtra("Day", freeDay);//공강요일 넘김
                setResult(RESULT_OK, intent);
                finish();//닫기
            }
        });
    }

    private void make(ArrayList<ClassSubject> list, ArrayList<ArrayList<ClassSubject>> rrr, ArrayList<ClassSubject> a, int j) {//모든 조합 구해서 rrr에
        int max = list.size();//전체 과목 수?

        for (int i = 0, length = list.get(j).getTimeSize(); i < length; i++)//j번째 과목의 분반개수
        {
            ClassSubject cs = new ClassSubject(list.get(j).getName());
            cs.setTimearr(list.get(j).getTimearr(i));//i번째 분반 시간표만 갖는 ClassSubject

            ArrayList<ClassSubject> arr = (ArrayList<ClassSubject>) a.clone();//왜복사하는지 모르겠음

            arr.add(cs);//ClassSubject를 넣어야지
            if (((TimeTableActivity) TimeTableActivity.mContext).checkOverlap(arr))//겹치지않는다면
            {
                if (j == max - 1) //마지막과목이라면
                    rrr.add(arr);//끝까지 다봤으면 rrr에 넣습니다.
                else
                    make(list, rrr, arr, j + 1);//그다음 과목으로 넘어갑니다
            } else//겹친다면
                arr.remove(arr.size() - 1);//방금 넣은거 뺀다
        }
    }

    private void combination(ArrayList<ClassSubject> list, ArrayList<ClassSubject> cs, int n, int r, int i) {
//n개 과목중에서 r개를 고르는 조합
        ArrayList<ClassSubject> a = (ArrayList<ClassSubject>) cs.clone();
        if (r == 0) {
            arr.add(a);
            return;
        } else if (n == r) {
            for (int j = 0; j < n; j++)
                a.add(list.get(j + i));//모든 과목을 다 고르는 경우
            arr.add(a);//넣고
            cs.clear();//초기화
        } else {
            a.add(list.get(i));
            combination(list, a, n - 1, r - 1, i + 1);

            if (a.size() != 0)
                a.remove(a.size() - 1);
            combination(list, a, n - 1, r, i + 1);
        }
    }

    private void getTimeTable() {
        int min_num = Integer.parseInt(credits_min.getText().toString()) / 3 + 1;//최소 학점/3 +1 = 최소 과목 수

        ArrayList<ClassSubject> list = new ArrayList<>();//이거말구 넣을과목....ㅅㅂ 존나많은데
        ArrayList<ClassSubject> a = new ArrayList<>();
        rrr = new ArrayList<>();//최종 조합 배열?

        make(list, rrr, a, 0);//총 조합 rrr에 저장

    }

    public void printChecked(View view) {
        final CheckBox cb1 = (CheckBox) findViewById(R.id.mon);
        final CheckBox cb2 = (CheckBox) findViewById(R.id.tue);
        final CheckBox cb3 = (CheckBox) findViewById(R.id.wen);
        final CheckBox cb4 = (CheckBox) findViewById(R.id.thu);
        final CheckBox cb5 = (CheckBox) findViewById(R.id.fri);
        Integer result = 0;//정수말고 다른걸로
        if (cb1.isChecked() == true) result = 1;
        if (cb2.isChecked() == true) result = 2;
        if (cb3.isChecked() == true) result = 3;
        if (cb4.isChecked() == true) result = 4;
        if (cb5.isChecked() == true) result = 5;
        freeDay = result;
    }

    private void getClassName() {

        //searchview.setSubmitButtonEnabled(true);//확인버튼 활성화

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                int num = 0;

                try {
                    Cursor c1 = database.rawQuery("select 과목명, 학정번호 from allclass where 과목명 like '%" + s + "%'", null);//일단 한 테이블에 모든 교양넣었음
                    if (c1 != null) {
                        num = c1.getCount();
                        c1.moveToNext();
                        text2.setText("d" + num);//일단 개수출력

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
