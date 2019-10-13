package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import java.util.ArrayList;

import javax.security.auth.Subject;

public class TimeTableActivity extends AppCompatActivity {
    public static Context mContext = null;

    Integer sid, syear, smajor, minorNum, subMajor, doubleMajor, position = 0;
    String spw;
    SearchView searchview;
    int[] idArray = new int[30];//강의 목록 출력
    TextView[] tvArray = new TextView[30];
    ArrayList<ArrayList<ClassSubject>> rrr;//이름.. 나중에 수정..
    ArrayList<ClassSubject> classlist;
    ArrayList<ClassSubject> pluscs = new ArrayList<>();
    private ViewPager viewPager;
    private TableViewPagerAdapter pagerAdpater;
    TableLayout tableLayout;
    long nStart = 0;
    long nEnd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nStart = System.nanoTime();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdpater = new TableViewPagerAdapter(this);
        viewPager.setAdapter(pagerAdpater);

        searchview = (SearchView) findViewById(R.id.find);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        Intent intent1 = getIntent();
        sid = intent1.getIntExtra("studentId", 1);
        spw = intent1.getStringExtra("studentPw");
        syear = intent1.getIntExtra("studentYear", 1);
        smajor = intent1.getIntExtra("studentMajor", 1);
        minorNum = intent1.getIntExtra("minorNum", 0);
        subMajor = intent1.getIntExtra("subMajor", 0);
        doubleMajor = intent1.getIntExtra("doubleMajor", 0);
        classlist = intent1.getParcelableArrayListExtra("classlist");

        for (int i = 0; i < idArray.length; i++) {
            int jj = i / 5;
            int jjj = i % 5;
            idArray[i] = getResources().getIdentifier("lec" + jj + "_" + jjj, "id", "com.example.myapplication");
        }

        for (int i = 0; i < tvArray.length; i++) {
            tvArray[i] = (TextView) findViewById(idArray[i]);
        }

        queryData(classlist);

        findClass();

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectionActivity.class);
                intent.putParcelableArrayListExtra("now_list", rrr.get(position));//null일때 예외처리필요함
                intent.putExtra("minorNum", minorNum);//교양개수 넘김
                startActivityForResult(intent, 100);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                clearText();//이전꺼 지우고
                position = i;//현재페이지
                for (int j = 0; j < rrr.get(i).size(); j++) {//과목 개수만큼만 출력
                    tvArray[j * 5 + 0].setText(rrr.get(i).get(j).getTimearr(0).getCode());
                    tvArray[j * 5 + 1].setText(rrr.get(i).get(j).getName());
                    tvArray[j * 5 + 2].setText(rrr.get(i).get(j).getTimearr(0).getEsu());//이수 출력
                    tvArray[j * 5 + 3].setText(rrr.get(i).get(j).getTimearr(0).getProf());//교수명 출력
                    tvArray[j * 5 + 4].setText(rrr.get(i).get(j).getTimearr(0).getTimestr());
                }//i번째 조합 출력
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mContext = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100://추가버튼
                    //freeDay = intent.getIntExtra("Day", 1);
                    Toast.makeText(getApplicationContext(), "kk", Toast.LENGTH_SHORT).show();
                    break;
                case 200://과목검색 searchview
                    pluscs = intent.getParcelableArrayListExtra("plus");
                    if (pluscs != null) {
                        for (int i = 0; i < pluscs.size(); i++) {
                            rrr.get(position).add(pluscs.get(i));//현재 페이지에 추가
                            if (!checkOverlap(rrr.get(position)))//겹치면
                                rrr.get(position).remove(rrr.get(position).size() - 1);//추가하지 않는다
                        }
                        if (rrr.get(position).get(0).getName().equals("null"))
                            rrr.get(position).remove(0);//일단 필수과목이 없는경우 오류를 없애기 위해
                    }
                    //순서대로 추가 선택한 과목끼리 겹치면 앞에꺼만 나

                    pagerAdpater.setRrr(rrr);//시간표 업데이트

                    for (int i = 0; i < rrr.get(position).size(); i++) {//과목 개수만큼만 출력
                        tvArray[i * 5 + 0].setText(rrr.get(position).get(i).getTimearr(0).getCode());
                        tvArray[i * 5 + 1].setText(rrr.get(position).get(i).getName());
                        tvArray[i * 5 + 2].setText(rrr.get(position).get(i).getTimearr(0).getEsu());//이수 출력
                        tvArray[i * 5 + 3].setText(rrr.get(position).get(i).getTimearr(0).getProf());//교수명 출력
                        tvArray[i * 5 + 4].setText(rrr.get(position).get(i).getTimearr(0).getTimestr());
                    }//다시 출력
                    break;
                default:
                    break;
            }
        }
    }

    private void findClass() {//과목검색함수
        //searchview.setSubmitButtonEnabled(true);//확인버튼 활성화
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(getApplicationContext(), ClassListActivity.class);
                intent.putExtra("classname", s);//검색할 과목
                startActivityForResult(intent, 200);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void clearText() {//과목 목록 초기화
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++)
                tvArray[i * 5 + j].setText("");
        }
    }

    private void make(ArrayList<ClassSubject> list, ArrayList<ArrayList<ClassSubject>> rrr, ArrayList<ClassSubject> a, int j) {//모든 조합 구해서 rrr에
        int max = list.size();//전체 과목 수?

        for (int i = 0, length = list.get(j).getTimeSize(); i < length; i++)//j번째 과목의 분반개수
        {
            ClassSubject cs = new ClassSubject(list.get(j).getName());
            cs.setTimearr(list.get(j).getTimearr(i));//i번째 분반 시간표만 갖는 ClassSubject

            ArrayList<ClassSubject> arr = (ArrayList<ClassSubject>) a.clone();//왜복사하는지 모르겠음

            arr.add(cs);//ClassSubject를 넣어야지
            if (checkOverlap(arr))//겹치지않는다면
            {
                if (j == max - 1) //마지막과목이라면
                    rrr.add(arr);//끝까지 다봤으면 rrr에 넣습니다.
                else
                    make(list, rrr, arr, j + 1);//그다음 과목으로 넘어갑니다
            } else//겹친다면
                arr.remove(arr.size() - 1);//방금 넣은거 뺀다
        }
    }

    public boolean find(ArrayList<Integer> chk, int k) {
        for (int ii = 0; ii < chk.size(); ii++) {
            if (k == chk.get(ii))
                return false;//순서대로 찾았는데 없다
        }
        return true;
    }

    public boolean checkOverlap(ArrayList<ClassSubject> rrr) {//중복검사
        ArrayList<Integer> chk = new ArrayList<>();//임시로 모든 시간을 저장한다 순서대로
        for (int i = 0; i < rrr.size(); i++) {
            for (int k = 0; k < rrr.get(i).getTimearr(0).size(); k++) {//i번째 과목의 시간
                if (find(chk, rrr.get(i).getTimearr(0).print(k)))//겹치는지 확인하고
                    chk.add(rrr.get(i).getTimearr(0).print(k));//없으면 집어넣는다.
                else
                    return false;//겹치는거 나오면
            }
        }
        return true;
    }

    private void johab() {
        for (int i = 0; i < rrr.get(0).size(); i++) {//과목 개수만큼만 출력
            tvArray[i * 5 + 0].setText(rrr.get(0).get(i).getTimearr(0).getCode());
            tvArray[i * 5 + 1].setText(rrr.get(0).get(i).getName());
            tvArray[i * 5 + 2].setText(rrr.get(0).get(i).getTimearr(0).getEsu());//이수 출력
            tvArray[i * 5 + 3].setText(rrr.get(0).get(i).getTimearr(0).getProf());//교수명 출력
            tvArray[i * 5 + 4].setText(rrr.get(0).get(i).getTimearr(0).getTimestr());
        }//처음에 조합첫번째꺼 출력
    }

    private void queryData(ArrayList<ClassSubject> classlist) {
        rrr = new ArrayList<>();//최종 조합 배열?

        if (classlist.size() != 0) {
            ArrayList<ClassSubject> a = new ArrayList<>();
            make(classlist, rrr, a, 0);
            if (rrr.size() != 0)
                johab();
            else {//조합되는 시간표가 없을때
                rrr = new ArrayList<>();//최종 조합 배열?
                ClassSubject cs = new ClassSubject("null");
                ArrayList<ClassSubject> arr = new ArrayList<>();
                TimeArr timeArr = new TimeArr();
                cs.put(timeArr);
                arr.add(cs);
                rrr.add(arr);//빈거를 넣어준다
            }
        } else {//전공선택을 하나도 안했을때
            rrr = new ArrayList<>();//최종 조합 배열?
            ClassSubject cs = new ClassSubject("null");
            ArrayList<ClassSubject> arr = new ArrayList<>();
            TimeArr timeArr = new TimeArr();
            cs.put(timeArr);
            arr.add(cs);
            rrr.add(arr);//빈거를 넣어준다
        }

        pagerAdpater.setRrr(rrr);

        nEnd = System.nanoTime();
        System.out.println("total elapsed time = " + (nEnd - nStart));
    }
}

