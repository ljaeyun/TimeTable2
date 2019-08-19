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
    Integer min, max, creditsum;
    TextView text2;
    TextView t1, t2;
    String className = "";
    Integer freeDay;
    ArrayList<Integer> nowtimearr;
    ArrayList<ClassSubject> classList;
    ArrayList<ArrayList<ClassSubject>> rrr;//이름.. 나중에 수정..
    ArrayList<ArrayList<ClassSubject>> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        database = openOrCreateDatabase("test.db", MODE_PRIVATE, null);

        Intent intent1 = getIntent();
        classList = intent1.getParcelableArrayListExtra("now_list");//전달받은 과목 리스트
        nowtimearr = new ArrayList<>();
        for (int i = 0; i < classList.size(); i++) {
            for (int j = 0; j < classList.get(i).getTimearr(0).size(); j++)
                nowtimearr.add(classList.get(i).getTimearr(0).print(j));
        }
        creditsum = creditSum(classList);//추가하기 전 현재 학점 합

        credits_max = (EditText) findViewById(R.id.credits_max);
        credits_min = (EditText) findViewById(R.id.credits_min);
        max = Integer.parseInt(credits_max.getText().toString());//최대 학점
        min = Integer.parseInt(credits_min.getText().toString());//최소 학점

        min = 17;
        max = 21;//일단 초기화

        max -= creditsum;
        min -= creditsum;//더 작으면 오류!

        Button button_close = (Button) findViewById(R.id.button_close);
        Button button_apply = (Button) findViewById(R.id.button_apply);
        searchview = (SearchView) findViewById(R.id.findclass);
        text2 = (TextView) findViewById(R.id.cn1);
        t1 = (TextView) findViewById(R.id.select1);
        t2 = (TextView) findViewById(R.id.select2);


        getTimeTable();

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

        findViewById(R.id.sci_tec).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                printSubjectChecked(view);
            }
        });
        findViewById(R.id.expression).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                printSubjectChecked(view);
            }
        });
        findViewById(R.id.human).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                printSubjectChecked(view);
            }
        });
        findViewById(R.id.social).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                printSubjectChecked(view);
            }
        });
        findViewById(R.id.global).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                printSubjectChecked(view);
            }
        });
        findViewById(R.id.arts).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                printSubjectChecked(view);
            }
        });
        findViewById(R.id.e_learning).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                printSubjectChecked(view);
            }
        });
        findViewById(R.id.english).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                printSubjectChecked(view);
            }
        });

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

    private int creditSum(ArrayList<ClassSubject> arr) {
        int sum = 0;
        for (int i = 0; i < arr.size(); i++)
            sum += arr.get(i).getTimearr(0).getCredit();
        return sum;//학점의 합 반환
    }

    public boolean checkOverlap(ArrayList<ClassSubject> rrr) {//중복검사
        ArrayList<Integer> chk = (ArrayList<Integer>) nowtimearr.clone();//임시로 모든 시간을 저장한다 순서대로
        for (int i = 0; i < rrr.size(); i++) {
            for (int k = 0; k < rrr.get(i).getTimearr(0).size(); k++) {//i번째 과목의 시간
                if (((TimeTableActivity) TimeTableActivity.mContext).find(chk, rrr.get(i).getTimearr(0).print(k)))//겹치는지 확인하고
                    chk.add(rrr.get(i).getTimearr(0).print(k));//없으면 집어넣는다.
                else
                    return false;//겹치는거 나오면
            }
        }
        return true;
    }

    private void make(ArrayList<ClassSubject> list, ArrayList<ArrayList<ClassSubject>> rrr, ArrayList<ClassSubject> a, int j) {//모든 조합 구해서 rrr에
        int maxnum = list.size();//전체 과목 수?

        for (int i = 0, length = list.get(j).getTimeSize(); i < length; i++)//j번째 과목의 분반개수
        {
            ClassSubject cs = new ClassSubject(list.get(j).getName());
            cs.setTimearr(list.get(j).getTimearr(i));//i번째 분반 시간표만 갖는 ClassSubject

            ArrayList<ClassSubject> arr = (ArrayList<ClassSubject>) a.clone();//왜복사하는지 모르겠음

            arr.add(cs);//ClassSubject를 넣어야지
            if (checkOverlap(arr) && creditSum(arr) <= max)//학점의 합이 최대 학점을 넘지 않고 시간이 겹치지 않는다면
            {
                if (j == maxnum - 1) //마지막과목이라면
                {    //if (creditSum(arr) >= min)//최소학접모다 크면
                    arr.addAll(classList);
                    rrr.add(arr);//끝까지 다봤으면 rrr에 넣습니다.
                }   else
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
        int min_num = min / 3 + 1;//최소 학점/3 +1 = 최소 과목 수
        int num = 0;
        ArrayList<ClassSubject> list = new ArrayList<>();//모든과목을 이름으로 넣는다..일단

        try {
            Cursor c1 = database.rawQuery("select distinct 과목명 from allclass", null);
            //과목이름 한번만
            if (c1 != null) {
                num = c1.getCount();
                for (int i = 0; i < num; i++) {
                    c1.moveToNext();
                    ClassSubject s1 = new ClassSubject(c1.getString(0));
                    list.add(s1);//과목이름을 가진 ..
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }

        int credit1 = 0, credit2 = 0, credit3 = 0;//1학점 과목 개수 2학점 과목 개수
        try {
            Cursor c = database.rawQuery("select distinct 과목명 from allclass where 학점 like '1'", null);
            if (c != null) {
                credit1 = c.getCount();
                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }
        try {
            Cursor c = database.rawQuery("select distinct 과목명 from allclass where 학점 like '2'", null);
            if (c != null) {
                credit2 = c.getCount();
                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }//이걸 이렇게 여러번 검색해야하나!!!
        credit3 = num - credit1 - credit2;
        int n1 = 0, n2 = 0;
        if (max <= credit1) {
            n1 = max;
        } else {
            if ((max - credit1) % 2 == 0) {
                n1 = credit1;
                n2 = (max - n1) / 2;
            } else {
                n1 = credit1 - 1;
                n2 = (max - n1 + 1) / 2;
            }
        }

        int max_num = n1 + n2;
        try {
            Cursor c = database.rawQuery("select 학정번호, 과목명, 이수,학점, 담당교수, 요일1,시간1,요일2,시간2 from allclass", null);
            if (c != null) {
                int count = c.getCount();

                for (int i = 0; i < count; i++) {
                    c.moveToNext();
                    for (int k = 0; k < num; k++) {
                        if (c.getString(1).equalsIgnoreCase(list.get(k).getName())) {//과목이름이 같은 arraylist에
                            ((TimeTableActivity) TimeTableActivity.mContext).timecal(list.get(k), c);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }

        ArrayList<ClassSubject> cs = new ArrayList<>();
      // for (int r = min_num; r < max_num; r++) {
            arr = new ArrayList<>();//총 조합이 여기에 저장된다
            combination(list, cs, list.size(), 2, 0);//전체에서 r개 과목을 고른다 과 조합이 arr에 저장
        //일단 2개 고르는걸로.. 연산이 너무 크다

            ArrayList<ClassSubject> a = new ArrayList<>();
            rrr = new ArrayList<>();//최종 조합 배열?
            for (int i = 0; i < arr.size(); i++)
                make(arr.get(i), rrr, a, 0);//i번째 과목 조합의... 가능한 분반 조합

      //  }

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

    public void printSubjectChecked(View view) {
        final CheckBox cb1 = (CheckBox) findViewById(R.id.sci_tec);
        final CheckBox cb2 = (CheckBox) findViewById(R.id.expression);
        final CheckBox cb3 = (CheckBox) findViewById(R.id.human);
        final CheckBox cb4 = (CheckBox) findViewById(R.id.social);
        final CheckBox cb5 = (CheckBox) findViewById(R.id.global);
        final CheckBox cb6 = (CheckBox) findViewById(R.id.arts);
        final CheckBox cb7 = (CheckBox) findViewById(R.id.e_learning);
        final CheckBox cb8 = (CheckBox) findViewById(R.id.english);
        ArrayList<Integer> result = new ArrayList<>();//정수말고 다른걸로
        if (cb1.isChecked()) result.add(1);
        if (cb2.isChecked()) result.add(2);
        if (cb3.isChecked()) result.add(3);
        if (cb4.isChecked()) result.add(4);
        if (cb5.isChecked()) result.add(5);
        if (cb6.isChecked()) result.add(6);
        if (cb7.isChecked()) result.add(7);
        if (cb8.isChecked()) result.add(8);
        //freeDay = result;
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
