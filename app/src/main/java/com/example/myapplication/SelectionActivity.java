package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectionActivity extends AppCompatActivity {
    SearchView searchview;
    SQLiteDatabase database;
    TableLayout tableLayout;
    Integer min, max, creditsum, minorNum;
    ArrayList<Integer> freeDay = new ArrayList<>();
    ArrayList<Integer> nowtimearr;
    ArrayList<Integer> subjectarr = new ArrayList<>();
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
        minorNum = intent1.getIntExtra("minorNum", 0);//교양개수

        nowtimearr = new ArrayList<>();
        if (classList.get(0).getName().equalsIgnoreCase("null")) {//빈거 받아왔으면
            classList.remove(0);//null은 지워줌
        } else {
            for (int i = 0; i < classList.size(); i++) {
                for (int j = 0; j < classList.get(i).getTimearr(0).size(); j++)
                    nowtimearr.add(classList.get(i).getTimearr(0).print(j));
            }
        }
        creditsum = creditSum(classList);//추가하기 전 현재 학점 합

        min = 17;
        max = 21;//일단 초기화

        max -= creditsum;
        min -= creditsum;//더 작으면 오류!

        final EditText name_text = (EditText) findViewById(R.id.name_text);
        final TableLayout nametable = (TableLayout) findViewById(R.id.nameTag);
        Button button_close = (Button) findViewById(R.id.button_close);
        Button button_apply = (Button) findViewById(R.id.button_apply);
        Button add_button = (Button) findViewById(R.id.add_button);
        searchview = (SearchView) findViewById(R.id.findclass);

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
                if (subjectarr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "교양영역을 1개이상 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (subjectarr.size() > 3) {
                    Toast.makeText(getApplicationContext(), "교양영역을 3개 이하로 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (minorNum != 0)
                    getTimeTable();

                Intent intent = new Intent();
                //intent.putExtra("Day", freeDay);//공강요일 넘김
                setResult(RESULT_OK, intent);
                finish();//닫기
            }
        });

        add_button.setOnClickListener(new Button.OnClickListener() {//제거할이름들 추가
            @Override
            public void onClick(View v) {
                //textView.setText(name_text.getText().toString());
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
            cs.setTypecode(list.get(j).getTypecode());

            ArrayList<ClassSubject> arr = (ArrayList<ClassSubject>) a.clone();//왜복사하는지 모르겠음

            arr.add(cs);//ClassSubject를 넣어야지
            if (checkOverlap(arr) && creditSum(arr) <= max && eliminate(arr.get(arr.size() - 1))) {//학점의 합이 최대 학점을 넘지 않고 시간이 겹치지 않는다면
                if (j == maxnum - 1) { //마지막과목이라면
                    arr.addAll(classList);
                    rrr.add(arr);//끝까지 다봤으면 rrr에 넣습니다.
                } else
                    make(list, rrr, arr, j + 1);//그다음 과목으로 넘어갑니다
            } else//겹친다면
                arr.remove(arr.size() - 1);//방금 넣은거 뺀다
        }
    }

    private boolean isavailable(ArrayList<ClassSubject> a, ClassSubject cs) {
        ArrayList<ClassSubject> arr = (ArrayList<ClassSubject>) a.clone();
        arr.addAll(classList);//이미 있는 과목이랑도 비교
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getTypecode().equals(cs.getTypecode()) && arr.get(i).getTimearr(0).getCode().substring(5, 6).equalsIgnoreCase(cs.getTimearr(0).getCode().substring(5, 6)))
                return false;//같은 영역 같은 난이도면 false
        }
        return true;
    }

    private boolean over3(ArrayList<ClassSubject> a) {
        int[] n = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int i = 0; i < a.size(); i++) {
            n[a.get(i).getTypecode()]++;
        }

        for (int i = 0; i < 8; i++) {
            if (n[i] > 3)
                return false;//3개 넘어가는거 있으면 false
        }
        return true;
    }

    private void combination(ArrayList<ClassSubject> list, ArrayList<ClassSubject> cs, int n, int r, int i) {
//n개 과목중에서 r개를 고르는 조합
        ArrayList<ClassSubject> a = (ArrayList<ClassSubject>) cs.clone();
        if (r == 0) {
            if (over3(a))
                arr.add(a);
            return;
        } else if (n == r) {
            for (int j = 0; j < n; j++)
                a.add(list.get(j + i));//모든 과목을 다 고르는 경우
            if (over3(a))
                arr.add(a);//넣고
            cs.clear();//초기화
        } else {
            if (isavailable(a, list.get(i))) {//a에 있는거 영역이랑 난이도 겹치는게 있는지
                a.add(list.get(i));
                combination(list, a, n - 1, r - 1, i + 1);

                if (a.size() != 0)
                    a.remove(a.size() - 1);
            }
            combination(list, a, n - 1, r, i + 1);
        }
    }

    private boolean isExists(ArrayList<ClassSubject> list, Cursor c) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equalsIgnoreCase(c.getString(1))) {
                ((major_select) major_select.mContext).timecal(list.get(i), c);//분반 추가하고
                return true;//이미 있다
            }
        }
        return false;//없다
    }

    private void getTimeTable() {
        ArrayList<ClassSubject> list = new ArrayList<>();

        int n = 0;
        for (int s = 0; s < subjectarr.size(); s++) {//교양영역에 맞는거
            try {
                Cursor c1 = database.rawQuery("select 학정번호, 과목명, 이수,학점, 담당교수, 요일1,시간1,요일2,시간2,typecode from allsubject where typecode like " + subjectarr.get(s), null);
                if (c1 != null) {
                    int num = c1.getCount();
                    n += num;
                    for (int i = 0; i < n; i++) {
                        c1.moveToNext();
                        if (!isExists(list, c1)) {//list에 존재하지 않으면
                            ClassSubject cs1 = new ClassSubject(c1.getString(1));
                            list.add(cs1);//과목이름을 가진 ..
                            ((major_select) major_select.mContext).timecal(list.get(list.size() - 1), c1);//분반 추가하고
                            list.get(list.size() - 1).setTypecode(c1.getInt(9));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("", e.getMessage());
            }
        }

        ArrayList<ClassSubject> cs = new ArrayList<>();
        arr = new ArrayList<>();//총 조합이 여기에 저장된다
        combination(list, cs, list.size(), minorNum, 0);//전체에서 r개 과목을 고른다 과 조합이 arr에 저장

        ArrayList<ClassSubject> a = new ArrayList<>();
        rrr = new ArrayList<>();//최종 조합 배열?
        for (int i = 0; i < arr.size(); i++)
            make(arr.get(i), rrr, a, 0);//i번째 과목 조합의... 가능한 분반 조합
    }

    private void printChecked(View view) {
        final CheckBox cb1 = (CheckBox) findViewById(R.id.mon);
        final CheckBox cb2 = (CheckBox) findViewById(R.id.tue);
        final CheckBox cb3 = (CheckBox) findViewById(R.id.wen);
        final CheckBox cb4 = (CheckBox) findViewById(R.id.thu);
        final CheckBox cb5 = (CheckBox) findViewById(R.id.fri);

        ArrayList<Integer> result = new ArrayList<>();
        if (cb1.isChecked()) {
            if (!freeAvailable(0)) {
                cb1.setChecked(false);
                Toast.makeText(getApplicationContext(), "공강X", Toast.LENGTH_SHORT).show();
            } else
                result.add(0);
        }
        if (cb2.isChecked()) {
            if (!freeAvailable(1)) {
                cb2.setChecked(false);
                Toast.makeText(getApplicationContext(), "공강X", Toast.LENGTH_SHORT).show();
            } else
                result.add(1);
        }
        if (cb3.isChecked()) {
            if (!freeAvailable(2)) {
                cb3.setChecked(false);
                Toast.makeText(getApplicationContext(), "공강X", Toast.LENGTH_SHORT).show();
            } else
                result.add(2);
        }
        if (cb4.isChecked()) {
            if (!freeAvailable(3)) {
                cb4.setChecked(false);
                Toast.makeText(getApplicationContext(), "공강X", Toast.LENGTH_SHORT).show();
            } else
                result.add(3);
        }
        if (cb5.isChecked()) {
            if (!freeAvailable(4)) {
                cb5.setChecked(false);
                Toast.makeText(getApplicationContext(), "공강X", Toast.LENGTH_SHORT).show();
            } else
                result.add(4);
        }

        freeDay.clear();
        freeDay = (ArrayList<Integer>) result.clone();
    }

    private void printSubjectChecked(View view) {
        final CheckBox cb1 = (CheckBox) findViewById(R.id.sci_tec);
        final CheckBox cb2 = (CheckBox) findViewById(R.id.expression);
        final CheckBox cb3 = (CheckBox) findViewById(R.id.human);
        final CheckBox cb4 = (CheckBox) findViewById(R.id.social);
        final CheckBox cb5 = (CheckBox) findViewById(R.id.global);
        final CheckBox cb6 = (CheckBox) findViewById(R.id.arts);
        final CheckBox cb7 = (CheckBox) findViewById(R.id.e_learning);
        final CheckBox cb8 = (CheckBox) findViewById(R.id.english);

        ArrayList<Integer> result = new ArrayList<>();
        if (cb1.isChecked()) result.add(1);
        if (cb2.isChecked()) result.add(2);
        if (cb3.isChecked()) result.add(3);
        if (cb4.isChecked()) result.add(4);
        if (cb5.isChecked()) result.add(5);
        if (cb6.isChecked()) result.add(6);
        if (cb7.isChecked()) result.add(7);
        if (cb8.isChecked()) result.add(8);

        if (result.size() > 3) {
            Toast.makeText(getApplicationContext(), "교양영역을 3개이하로 선택해주세요", Toast.LENGTH_SHORT).show();
        }

        subjectarr.clear();
        subjectarr = (ArrayList<Integer>) result.clone();
    }

    private boolean freeAvailable(int free) {
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i < classList.size(); i++) {
            for (int j = 0; j < classList.get(i).getTimearr(0).size(); j++) {
                arr.add(classList.get(i).getTimearr(0).print(j) / 10);//요일만 넣는다
            }
        }
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).equals(free))
                return false;
        }
        return true;
    }

    private boolean eliminate(ClassSubject cs) {
        for (int i = 0; i < freeDay.size(); i++) {
            for (int j = 0; j < cs.getTimearr(0).size(); j++) {
                if (freeDay.get(i) == cs.getTimearr(0).print(j) / 10)
                    return false;//제거할 시간arr과 겹치면 false
            }
        }//공강 제거

        //제외할 시간 제거

        return true;//안겹치면
    }
}
