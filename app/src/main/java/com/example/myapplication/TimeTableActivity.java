package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import javax.security.auth.Subject;

public class TimeTableActivity extends AppCompatActivity {
    Integer sid, syear, smajor, db;
    String spw;
    SQLiteDatabase database;
    TextView text1;
    int[] idArray = new int[30];
    TextView[] tvArray = new TextView[30];

    int[] timearr = new int[50];//시간표에 쓸.. 번호


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

        for (int i = 0; i < 50; i++)
            timearr[i] = i;//월:0~9 화:10~19

        text1 = (TextView) findViewById(R.id.inputSearch);

        Intent intent1 = getIntent();
        db = intent1.getIntExtra("database", 1);
        sid = intent1.getIntExtra("studentId", 1);
        spw = intent1.getStringExtra("studentPw");
        syear = intent1.getIntExtra("studentYear", 1);
        smajor = intent1.getIntExtra("studentMajor", 1);
        setmajor(smajor);
        choosedb(db);

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
                startActivity(intent);
            }
        });
    }

    private void make(ArrayList<ClassSubject> list, ArrayList<ArrayList<TimeArr>> rrr, ArrayList<TimeArr> a,int j)
    {
        int max = list.size();//전체 과목 수?

        for(int i=0, length = list.get(j).getTimeSize();i<length;i++)//j번째 과목의 분반개수
        {
            ArrayList<TimeArr> arr = (ArrayList<TimeArr>)a.clone();//왜복사해야되는지는 모르겠음

            arr.add(list.get(j).getTimearr(i));
            if(j==max-1)
                rrr.add(arr);//끝까지 다봤으면 rrr에 넣습니다.
            else
                make(list,rrr,arr,j+1);//그다음 과목으로 넘어갑니다
        }
    }

    private void johab(ArrayList<ClassSubject> list)
    {
        ArrayList<TimeArr> a=new ArrayList<>();
        ArrayList<ArrayList<TimeArr>> rrr = new ArrayList<>();//최종 조합 배열?

        make(list,rrr,a,0);

        text1.setText(rrr.size()+"개 조합");

    }

    private void timecal(ClassSubject s, String 요일1, String 시간1, String 요일2, String 시간2) {
        int n = 0, m = 0;// 월: 0~9
        if (요일1.equals("화")) {//화:10~19
            n = 10;
        } else if (요일1.equals("수")) {
            n = 20;
        } else if (요일1.equals("목")) {
            n = 30;
        } else if (요일1.equals("금")) {
            n = 40;
        }
        if (요일2 != null) {
            if (요일2.equals("화")) {
                m = 10;
            } else if (요일2.equals("수")) {
                m = 20;
            } else if (요일2.equals("목")) {
                m = 30;
            } else if (요일2.equals("금")) {
                m = 40;
            }
        }
        TimeArr t = new TimeArr();

        String[] arr1 = 시간1.split("\\.|,");//시간1 .이나,으로 구분하고

        if (시간2 != null) {
            String[] arr2 = 시간2.split("\\.|,");//시간2 .이나,으로 구분하고
            for (int i = 0; i < arr2.length; i++)
                t.put(m + Integer.parseInt(arr2[i]));
        }//null 예외처리!!!!

        //배열 시간에{0,1,2}이런식으로 넣게 변경해야됨
        //자른거
        for (int i = 0; i < arr1.length; i++)
            t.put(n + Integer.parseInt(arr1[i]));

        s.put(t);
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
                    {
                        ClassSubject s1 = new ClassSubject(c1.getString(0));
                        classlist.add(s1);//필수과목이름을 가진 ..
                    }
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
                        for (int j = 0; j < 4; j++) {
                            tvArray[i * 5 + j].setText(c.getString(j));
                        }

                        //출력은 나중에 조합된 과목만 출력하는걸로 바꿀 예정
                        if (c.getString(6) != null)
                            tvArray[i * 5 + 4].setText(c.getString(4) + c.getString(5) + c.getString(6) + c.getString(7));
                        else
                            tvArray[i * 5 + 4].setText(c.getString(4) + c.getString(5));
                        //마지막 칸 화1목2로 출력 null일때 예외처리

                        for (int k = 0; k < num; k++) {
                            if (c.getString(1).equalsIgnoreCase(classlist.get(k).getName())) {//학정번호가 같은 arraylist에
                                timecal(classlist.get(k), c.getString(4), c.getString(5), c.getString(6), c.getString(7));//과목명
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", e.getMessage());
        }

        johab(classlist);
    }
}
