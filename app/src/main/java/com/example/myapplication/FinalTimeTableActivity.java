package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FinalTimeTableActivity extends AppCompatActivity {
    Integer sid, syear, smajor, subMajor, doubleMajor, position = 0;
    private ViewPager viewPager;
    private TableViewPagerAdapter pagerAdpater;
    int[] idArray = new int[28];//강의 목록 출력
    TextView[] tvArray = new TextView[28];
    SearchView searchview;
    TableLayout tableLayout;
    ArrayList<ArrayList<ClassSubject>> rrr;//이름.. 나중에 수정..
    ArrayList<ClassSubject> pluscs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdpater = new TableViewPagerAdapter(this);
        viewPager.setAdapter(pagerAdpater);
        searchview = (SearchView) findViewById(R.id.find);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        Intent intent1 = getIntent();
        sid = intent1.getIntExtra("studentId", 1);
        smajor = intent1.getIntExtra("studentMajor", 1);
        rrr = (ArrayList<ArrayList<ClassSubject>>) intent1.getSerializableExtra("final_list");//모든 조합 받아와서

        for (int i = 0; i < idArray.length; i++) {
            int jj = i / 4;
            int jjj = i % 4;
            idArray[i] = getResources().getIdentifier("lec" + jj + "_" + jjj, "id", "com.example.myapplication");
        }

        for (int i = 0; i < tvArray.length; i++) {
            tvArray[i] = (TextView) findViewById(idArray[i]);
        }

        for (int i = 0; i < tvArray.length; i++) {
            tvArray[i].setTag(i);
            tvArray[i].setOnClickListener(new TextView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (Integer) v.getTag();
                    if (index < rrr.get(position).size() * 4) {//과목 선택하면
                        Intent intent = new Intent(getApplicationContext(), RemoveClass.class);
                        intent.putExtra("index", index / 4);//지울 과목
                        intent.putExtra("classname", rrr.get(position).get(index / 4).getName());//지울 과목 이름
                        startActivityForResult(intent, 300);
                    }
                }
            });
        }

        firstprint();

        findClass();

        Button button = (Button) findViewById(R.id.button);
        button.setVisibility(View.GONE);//추가버튼은 지우고

        Button prog_button = (Button) findViewById(R.id.progress_button);
        prog_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Progress.class);
                intent.putExtra("studentId", sid);
                intent.putExtra("studentMajor", smajor);
                startActivity(intent);
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
                    tvArray[j * 4 + 0].setText(rrr.get(i).get(j).getTimearr(0).getCode());
                    tvArray[j * 4 + 1].setText(rrr.get(i).get(j).getName());
                    tvArray[j * 4 + 2].setText(rrr.get(i).get(j).getTimearr(0).getEsu());//이수 출력
                    tvArray[i * 4 + 3].setText(rrr.get(i).get(i).getTimearr(0).getTimestr());//시간 출력
                }//i번째 조합 출력
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void firstprint() {
        if (rrr.size() != 0) {
            for (int i = 0; i < rrr.get(0).size(); i++) {//과목 개수만큼만 출력
                tvArray[i * 4 + 0].setText(rrr.get(0).get(i).getTimearr(0).getCode());
                tvArray[i * 4 + 1].setText(rrr.get(0).get(i).getName());
                tvArray[i * 4 + 2].setText(rrr.get(0).get(i).getTimearr(0).getEsu());//이수 출력
                tvArray[i * 4 + 3].setText(rrr.get(0).get(i).getTimearr(0).getTimestr());//시간 출력
            }//처음에 조합첫번째꺼 출력
        } else {//조합되는 시간표가 없을때
            rrr = new ArrayList<>();//최종 조합 배열?
            ClassSubject cs = new ClassSubject("null");
            ArrayList<ClassSubject> arr = new ArrayList<>();
            TimeArr timeArr = new TimeArr();
            cs.put(timeArr);
            arr.add(cs);
            rrr.add(arr);//빈거를 넣어준다
        }
        pagerAdpater.setRrr(rrr);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200://과목검색 searchview
                    pluscs = intent.getParcelableArrayListExtra("plus");
                    if (pluscs != null) {//받아온 과목이 있다
                        for (int i = 0; i < pluscs.size(); i++) {
                            if (((major_select) major_select.mContext).checkCredit(rrr.get(position), pluscs.get(i)) && ((TimeTableActivity) TimeTableActivity.mContext).samename(rrr.get(position), pluscs.get(i))) {//학점초과 안되면
                                rrr.get(position).add(pluscs.get(i));//현재 페이지에 추가
                                if (!((TimeTableActivity) TimeTableActivity.mContext).checkOverlap(rrr.get(position)))//겹치면
                                    rrr.get(position).remove(rrr.get(position).size() - 1);//추가하지 않는다
                            }
                            if (rrr.get(position).get(0).getName().equals("null"))
                                rrr.get(position).remove(0);//일단 아무과목이 없는경우 오류를 없애기 위해
                        }
                    }
                    //순서대로 추가 선택한 과목끼리 겹치면 앞에꺼만 나오게

                    pagerAdpater.setRrr(rrr);//시간표 업데이트

                    for (int i = 0; i < rrr.get(position).size(); i++) {//과목 개수만큼만 출력
                        tvArray[i * 4 + 0].setText(rrr.get(position).get(i).getTimearr(0).getCode());
                        tvArray[i * 4 + 1].setText(rrr.get(position).get(i).getName());
                        tvArray[i * 4 + 2].setText(rrr.get(position).get(i).getTimearr(0).getEsu());//이수 출력
                        tvArray[i * 4 + 3].setText(rrr.get(position).get(i).getTimearr(0).getTimestr());//시간 출력
                    }//다시 출력
                    break;
                case 300://과목 삭제
                    int yes = intent.getIntExtra("yes", 0);
                    int index = intent.getIntExtra("index", 0);
                    if (yes == 0) {
                        rrr.get(position).remove(index);
                    }
                    pagerAdpater.setRrr(rrr);//시간표 업데이트
                    clearText();
                    for (int i = 0; i < rrr.get(position).size(); i++) {//과목 개수만큼만 출력
                        tvArray[i * 4 + 0].setText(rrr.get(position).get(i).getTimearr(0).getCode());
                        tvArray[i * 4 + 1].setText(rrr.get(position).get(i).getName());
                        tvArray[i * 4 + 2].setText(rrr.get(position).get(i).getTimearr(0).getEsu());//이수 출력
                        tvArray[i * 4 + 3].setText(rrr.get(position).get(i).getTimearr(0).getTimestr());//시간 출력
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
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++)
                tvArray[i * 4 + j].setText("");
        }
    }
}
