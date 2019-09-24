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
    public static Context mContext = null;

    Integer sid, syear, smajor, minorNum, majorNum, subMajor, subMajorNum, doubleMajor, doubleMajorNum, freeDay = 0, position = 0;
    String spw;
    String table = "business";
    SQLiteDatabase database;
    SearchView searchview;
    int[] idArray = new int[30];//강의 목록 출력
    TextView[] tvArray = new TextView[30];
    ArrayList<ArrayList<ClassSubject>> rrr;//이름.. 나중에 수정..
    ArrayList<ArrayList<ClassSubject>> arr;
    ArrayList<ClassSubject> classlist;
    ArrayList<ClassSubject> pluscs = new ArrayList<>();
    private ViewPager viewPager;
    private TableViewPagerAdapter pagerAdpater;
    TableLayout tableLayout;
    long nStart = 0;
    long nEnd = 0;


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
        majorNum = intent1.getIntExtra("majorNum", 0);
        minorNum = intent1.getIntExtra("minorNum", 0);
        subMajor = intent1.getIntExtra("subMajor", 0);
        subMajorNum = intent1.getIntExtra("subMajorNum", 0);
        doubleMajor = intent1.getIntExtra("doubleMajor", 0);
        doubleMajorNum = intent1.getIntExtra("doubleMajorNum", 0);

        for (int i = 0; i < idArray.length; i++) {
            int jj = i / 5;
            int jjj = i % 5;
            idArray[i] = getResources().getIdentifier("lec" + jj + "_" + jjj, "id", "com.example.myapplication");
        }

        for (int i = 0; i < tvArray.length; i++) {
            tvArray[i] = (TextView) findViewById(idArray[i]);
        }

        setmajor(smajor);
        choosetable(smajor);
        queryData(majorNum);

        if (subMajorNum != 0) {
            choosetable(subMajor);
            //queryData(subMajorNum);//나중에 수정
        }
        if (doubleMajorNum != 0) {
            choosetable(doubleMajor);
            //queryData(doubleMajorNum);//나중에 수정
        }

        findClass();

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectionActivity.class);
                intent.putParcelableArrayListExtra("now_list", rrr.get(position));
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
        mContext = this;
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
                    pluscs = intent.getParcelableArrayListExtra("plus");
                    if (pluscs != null) {
                        for (int i = 0; i < pluscs.size(); i++) {
                            rrr.get(position).add(pluscs.get(i));//현재 페이지에 추가
                            if (!checkOverlap(rrr.get(position)))
                                rrr.get(position).remove(rrr.get(position).size() - 1);//추가하지 않는다
                        }
                        if (rrr.get(position).get(0).getName().equals("null"))
                            rrr.get(position).remove(0);//일단 필수과목이 없는경우 오류를 없애기 위해
                    }

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
                int num = 0;
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

        nEnd = System.nanoTime();
        System.out.println("total elapsed time = " + (nEnd - nStart));
    }

    private void choosetable(int major) {
        if (major >= 0 && major <= 1)
            database = openOrCreateDatabase("biz.db", MODE_PRIVATE, null);
        else if (major >= 2 && major <= 5)
            database = openOrCreateDatabase("engineer.db", MODE_PRIVATE, null);
        else if (major >= 6 && major <= 8)
            database = openOrCreateDatabase("sw.db", MODE_PRIVATE, null);
        else if (major >= 9 && major <= 15)
            database = openOrCreateDatabase("hss.db", MODE_PRIVATE, null);
        else if (major >= 16 && major <= 20)
            database = openOrCreateDatabase("natural.db", MODE_PRIVATE, null);
        else if (major >= 21 && major <= 28)
            database = openOrCreateDatabase("ei.db", MODE_PRIVATE, null);
        else
            database = openOrCreateDatabase("kwlaw.db", MODE_PRIVATE, null);

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
    }

    public void timecal(ClassSubject s, Cursor c) {
        int n = 0, m = 0;// 월: 0~9
        String timestr = null;//시간 문자열
        TimeArr t = new TimeArr();

        if (c.getString(5) != null) {
            if (c.getString(5).equals("화")) {//화:10~19
                n = 10;
            } else if (c.getString(5).equals("수")) {
                n = 20;
            } else if (c.getString(5).equals("목")) {
                n = 30;
            } else if (c.getString(5).equals("금")) {
                n = 40;
            } else if (c.getString(5).equals("토")) {
                n = 50;
            }
            if (c.getString(7) != null) {
                if (c.getString(7).equals("화")) {
                    m = 10;
                } else if (c.getString(7).equals("수")) {
                    m = 20;
                } else if (c.getString(7).equals("목")) {
                    m = 30;
                } else if (c.getString(7).equals("금")) {
                    m = 40;
                }
                timestr = c.getString(5) + c.getString(6) + ' ' + c.getString(7) + c.getString(8);
            } else
                timestr = c.getString(5) + c.getString(6);


            String[] arr1 = c.getString(6).split("\\.|,|\\n");//시간1 .이나,으로 구분하고

            if (c.getString(8) != null) {
                String[] arr2 = c.getString(8).split("\\.|,|\\n");//시간2 .이나,으로 구분하고 + 공백도 가끔있음
                for (int i = 0; i < arr2.length; i++) {
                    if (arr2[i].equals("") == false)//공백으로 자르고나면 ""남음...
                        t.put(m + Integer.parseInt(arr2[i]));
                }
            }//null 예외처리!!!!

            //자른거
            for (int i = 0; i < arr1.length; i++)
                t.put(n + Integer.parseInt(arr1[i]));
        } else {//인강인 경우..
            n = 50;
            t.put(n);
        }
        t.putCode(c.getString(0));//학정번호도 넣어봅시다
        t.setEsu(c.getString(2));//이수
        t.setCredit(Integer.parseInt(c.getString(3)));//학점
        t.setprof(c.getString(4));//교수명도
        t.setTimestr(timestr);
        s.put(t);
    }

    private void queryData(int major) {
        syear += 1;
        int num = 0;
        String sql1 = "select 학정번호, 과목명, 이수,학점, 담당교수, 요일1,시간1,요일2,시간2 from ";
        String sql2 = " where 이수 like '%필' and 학정번호 like '_____" + syear + "%'";
        String sql = sql1 + table + sql2;
        classlist = new ArrayList<>();//클래스타입의 arraylist.....

        try {
            Cursor c1 = database.rawQuery("select 학정번호, 과목명, 이수,학점, 담당교수, 요일1,시간1,요일2,시간2 from " + table + " where 이수 like '%필' and 학정번호 like '_____" + syear + "%'", null);
            //과목이름 한번만
            if (c1 != null) {
                num = c1.getCount();
                for (int i = 0; i < num; i++) {
                    c1.moveToNext();
                    if (!isExists(classlist, c1)) {//list에 존재하지 않으면
                        ClassSubject cs1 = new ClassSubject(c1.getString(1));
                        classlist.add(cs1);//과목이름을 가진 ..
                        timecal(classlist.get(classlist.size() - 1), c1);//분반 추가하고
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }

        if (major == 0) {
            rrr = new ArrayList<>();//최종 조합 배열?
            ClassSubject cs = new ClassSubject("null");
            ArrayList<ClassSubject> arr = new ArrayList<>();
            TimeArr timeArr = new TimeArr();
            cs.put(timeArr);
            arr.add(cs);
            rrr.add(arr);//빈거를 넣어준다

        } else if (classlist.size() <= major) { //필수전공의 수가 고른 전공보다 적다면
            int num2 = major - classlist.size();

            arr = new ArrayList<>();//총 조합이 여기에 저장된다
            ArrayList<ClassSubject> list = new ArrayList<>();

            try {
                Cursor c1 = database.rawQuery("select 학정번호, 과목명, 이수,학점, 담당교수, 요일1,시간1,요일2,시간2 from " + table + " where 이수 not like '%필' and 학정번호 like '_____" + syear + "%'", null);
                if (c1 != null) {
                    num = c1.getCount();
                    for (int i = 0; i < num; i++) {
                        c1.moveToNext();
                        if (!isExists(list, c1)) {//list에 존재하지 않으면
                            ClassSubject cs1 = new ClassSubject(c1.getString(1));
                            list.add(cs1);//과목이름을 가진 ..
                            timecal(list.get(list.size() - 1), c1);//분반 추가하고
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("", e.getMessage());
            }

            combination(list, classlist, list.size(), num2, 0);//list에서 num2개 조합을 arr에 저장한다.

            ArrayList<ClassSubject> a = new ArrayList<>();
            rrr = new ArrayList<>();//최종 조합 배열?
            for (int i = 0; i < arr.size(); i++)
                make(arr.get(i), rrr, a, 0);//i번째 과목 조합의... 가능한 분반 조합

            johab();
        } else {//필수전공수보다 적게고른다면...
            ArrayList<ClassSubject> cs = new ArrayList<>();
            arr = new ArrayList<>();//총 조합이 여기에 저장된다

            combination(classlist, cs, classlist.size(), major, 0);//classlist에서 조합을 arr에 저장한다.

            ArrayList<ClassSubject> a = new ArrayList<>();
            rrr = new ArrayList<>();//최종 조합 배열?
            for (int i = 0; i < arr.size(); i++)
                make(arr.get(i), rrr, a, 0);//i번째 과목 조합의... 가능한 분반 조합

            johab();
        }
        pagerAdpater.setRrr(rrr);
    }

    private boolean isExists(ArrayList<ClassSubject> list, Cursor c) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equalsIgnoreCase(c.getString(1))) {
                timecal(list.get(i), c);//분반 추가하고
                return true;//이미 있다
            }
        }
        return false;//없다
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

}

