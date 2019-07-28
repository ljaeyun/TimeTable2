package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private Context mContext = null;

    Integer sid, syear, smajor, db, freeDay = 0,position=0;
    String spw;
    SQLiteDatabase database;
    SearchView searchview;
    TextView text1;
    int[] idArray = new int[30];//강의 목록 출력
    TextView[] tvArray = new TextView[30];
    ArrayList<ArrayList<ClassSubject>> rrr;//이름.. 나중에 수정..
    ClassSubject pluscs;
    private ViewPager viewPager;
    private TableViewPagerAdapter pagerAdpater;
    TableLayout tableLayout;

    private Integer getId() {
        return sid;
    }

    private void setId(int sid) {
        this.sid = sid;
    }

    private String getPw() {
        return spw;
    }

    private void setPw(String spw) {
        this.spw = spw;
    }

    private Integer getyear() {
        return syear;
    }

    private void setyear(int syear) {
        this.syear = syear;
    }

    private Integer getmajor() {
        return smajor;
    }

    private void setmajor(int smajor) {
        this.smajor = smajor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdpater = new TableViewPagerAdapter(this);
        viewPager.setAdapter(pagerAdpater);

        searchview = (SearchView) findViewById(R.id.find);
        text1 = (TextView) findViewById(R.id.inputSearch);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        Intent intent1 = getIntent();
        db = intent1.getIntExtra("database", 1);
        sid = intent1.getIntExtra("studentId", 1);
        spw = intent1.getStringExtra("studentPw");
        syear = intent1.getIntExtra("studentYear", 1);
        smajor = intent1.getIntExtra("studentMajor", 1);

        setmajor(smajor);
        choosedb(db);

        findClass();

        for (int i = 0; i < idArray.length; i++) {
            int jj = i / 5;
            int jjj = i % 5;
            idArray[i] = getResources().getIdentifier("lec" + jj + "_" + jjj, "id", "com.example.myapplication");
        }

        for (int i = 0; i < tvArray.length; i++) {
            tvArray[i] = (TextView) findViewById(idArray[i]);
        }

        Button button = (Button) findViewById(R.id.button);
        queryData(smajor);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectionActivity.class);
               startActivityForResult(intent,100);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                position=i;//현재페이지
                if (rrr.size() > i) {//조합개수 나중에 페이지개수 조절로..
                    for (int j = 0; j < rrr.get(i).size(); j++) {//과목 개수만큼만 출력
                        tvArray[j * 5 + 0].setText(rrr.get(i).get(j).getTimearr(0).getCode());
                        tvArray[j * 5 + 1].setText(rrr.get(i).get(j).getName());
                        tvArray[j * 5 + 2].setText(rrr.get(i).get(j).getTimearr(0).getEsu());//이수 출력
                        tvArray[j * 5 + 3].setText(rrr.get(i).get(j).getTimearr(0).getProf());//교수명 출력
                        tvArray[j * 5 + 4].setText(rrr.get(i).get(j).getTimearr(0).getTimestr());
                    }//i번째 조합 출력
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100://추가버튼
                    //freeDay = intent.getIntExtra("Day", 1);
                    Toast.makeText(getApplicationContext(), freeDay + "kk", Toast.LENGTH_SHORT).show();
                    break;
                    case 200://과목검색 searchview
                        pluscs = (ClassSubject) intent.getParcelableExtra("plus");//추가할 과목 받아옴
                        rrr.get(position).add(pluscs);//현재페이지에만 추가해야되는데
                        for (int i = 0; i < rrr.get(0).size(); i++) {//과목 개수만큼만 출력
                            tvArray[i * 5 + 0].setText(rrr.get(0).get(i).getTimearr(0).getCode());
                            tvArray[i * 5 + 1].setText(rrr.get(0).get(i).getName());
                            tvArray[i * 5 + 2].setText(rrr.get(0).get(i).getTimearr(0).getEsu());//이수 출력
                            tvArray[i * 5 + 3].setText(rrr.get(0).get(i).getTimearr(0).getProf());//교수명 출력
                            tvArray[i * 5 + 4].setText(rrr.get(0).get(i).getTimearr(0).getTimestr());
                        }//다시 출력
                        break;
                default:
                    break;
            }
        }
    }

    private void findClass() {
        //searchview.setSubmitButtonEnabled(true);//확인버튼 활성화
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                int num = 0;
                Intent intent = new Intent(getApplicationContext(), ClassListActivity.class);
                intent.putExtra("classname",s);//검색할 과목
                startActivityForResult(intent,200);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
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
            if (j == max - 1)
                rrr.add(arr);//끝까지 다봤으면 rrr에 넣습니다.
            else
                make(list, rrr, arr, j + 1);//그다음 과목으로 넘어갑니다
        }
    }

    private boolean find(ArrayList<Integer> chk, int k) {
        for (int ii = 0; ii < chk.size(); ii++) {
            if (k == chk.get(ii))
                return false;//순서대로 찾았는데 없다
        }
        return true;
    }

    private boolean checkOverlap(ArrayList<ClassSubject> rrr) {//중복검사
        ArrayList<Integer> chk = new ArrayList<>();//임시로 모든 시간을 저장한다 순서대로
        for (int i = 0; i < rrr.size(); i++) {
            for (int k = 0; k < rrr.get(i).getTimearr(0).size(); k++) {
                if (i == 0) {
                    chk.add(rrr.get(0).getTimearr(0).print(k));//첫번째 시간은 다 넣고
                } else {
                    if (find(chk, rrr.get(i).getTimearr(0).print(k)))
                        chk.add(rrr.get(i).getTimearr(0).print(k));//없으면 집어넣는다.
                    else
                        return false;//겹치는거 나오면
                }
            }
        }
        return true;
    }

    private void johab(ArrayList<ClassSubject> list) {
        ArrayList<ClassSubject> a = new ArrayList<>();
        rrr = new ArrayList<>();//최종 조합 배열?

        make(list, rrr, a, 0);//총 조합 rrr에 저장

        for (int i = 0; i < rrr.size(); i++) {//rrr i번째 조합 확인
            if (!checkOverlap(rrr.get(i))) {
                rrr.remove(i);//중복걸린거 지우기
                i--;//아 조합 오류
            }
        }

        for (int i = 0; i < rrr.get(0).size(); i++) {//과목 개수만큼만 출력
            tvArray[i * 5 + 0].setText(rrr.get(0).get(i).getTimearr(0).getCode());
            tvArray[i * 5 + 1].setText(rrr.get(0).get(i).getName());
            tvArray[i * 5 + 2].setText(rrr.get(0).get(i).getTimearr(0).getEsu());//이수 출력
            tvArray[i * 5 + 3].setText(rrr.get(0).get(i).getTimearr(0).getProf());//교수명 출력
            tvArray[i * 5 + 4].setText(rrr.get(0).get(i).getTimearr(0).getTimestr());
        }//처음에 조합첫번째꺼 출력

        pagerAdpater.setRrr(rrr);
    }

    private void choosedb(int db) {
        if (db >= 0 && db <= 1)
            database = openOrCreateDatabase("biz.db", MODE_PRIVATE, null);
        else if (db >= 2 && db <= 5)
            database = openOrCreateDatabase("engineer.db", MODE_PRIVATE, null);
        else if (db >= 6 && db <= 8)
            database = openOrCreateDatabase("sw.db", MODE_PRIVATE, null);
        else if (db >= 9 && db <= 15)
            database = openOrCreateDatabase("hss.db", MODE_PRIVATE, null);
        else if (db >= 16 && db <= 20)
            database = openOrCreateDatabase("natural.db", MODE_PRIVATE, null);
        else if (db >= 21 && db <= 28)
            database = openOrCreateDatabase("ei.db", MODE_PRIVATE, null);
        else
            database = openOrCreateDatabase("kwlaw.db", MODE_PRIVATE, null);
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


    private void queryData(int major) {
        text1.setText("");
        String table = "business";

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
        syear += 1;
        int num = 0;
        String sql1 = "select 학정번호, 과목명, 이수, 담당교수, 요일1,시간1,요일2,시간2 from ";
        String sql2 = " where 이수 like '%필' and 학정번호 like '_____" + syear + "%'";
        String sql = sql1 + table + sql2;
        ArrayList<ClassSubject> classlist = new ArrayList<>();//클래스타입의 arraylist.....

        try {
            Cursor c1 = database.rawQuery("select distinct 과목명 from " + table + " where 이수 like '%필' and 학정번호 like '_____" + syear + "%'", null);
            //과목이름 한번만
            if (c1 != null) {
                num = c1.getCount();
                for (int i = 0; i < num; i++) {
                    c1.moveToNext();
                    ClassSubject s1 = new ClassSubject(c1.getString(0));
                    classlist.add(s1);//필수과목이름을 가진 ..
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }

        try {
            Cursor c = database.rawQuery(sql, null);

            if (c != null) {
                int count = c.getCount();

                for (int i = 0; i < count; i++) {
                    c.moveToNext();
                    {
                        for (int k = 0; k < num; k++) {//과목개수
                            if (c.getString(1).equalsIgnoreCase(classlist.get(k).getName())) {//과목이름이 같은 arraylist에
                                timecal(classlist.get(k), c);//깔끔한 함수
                            }
                        }//classlist에 넣는건 모든과목 다
                    }
                }

                johab(classlist);//필수인 과목이 없을때도 함수를 실행해서 그런것 같아요
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }

    }
}
