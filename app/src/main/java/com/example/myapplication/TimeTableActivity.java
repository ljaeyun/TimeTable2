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

public class TimeTableActivity extends AppCompatActivity {
    Integer sid,syear,smajor,db;
    String spw;
    SQLiteDatabase database;
    TextView text1;
    int[] idArray =new int[25];

    TextView[] tvArray =  new TextView[25];

    private Integer getId()
    {
        return sid;
    }

    private void setId(int sid)
    {
        this.sid = sid;
    }

    private String getPw()
    {
        return spw;
    }

    private void setPw(String spw) { this.spw = spw; }

    private Integer getyear()
    {
        return syear;
    }

    private void setyear(int syear)
    {
        this.syear = syear;
    }

    private Integer getmajor()
    {
        return smajor;
    }

    private void setmajor(int smajor)
    {
        this.smajor = smajor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        text1 = (TextView) findViewById(R.id.text1);

        Intent intent1 = getIntent();
        db = intent1.getIntExtra("database",1);
        sid = intent1.getIntExtra("studentId",1);
        spw = intent1.getStringExtra("studentPw");
        syear = intent1.getIntExtra("studentYear",1);
        smajor = intent1.getIntExtra("studentMajor",1);
        setmajor(smajor);
        choosedb(db);

        for(int i = 0 ; i < idArray.length; i++){
            int jj=i/5;
            int jjj=i%5;
            idArray[i] = getResources().getIdentifier("lec"+jj+"_"+jjj,"id","com.example.myapplication");
        }

        for(int i = 0 ; i < tvArray.length; i++){
            tvArray[i] = (TextView)findViewById(idArray[i]);
        }

       // Toast.makeText(this,sid+" "+smajor+" "+syear,Toast.LENGTH_SHORT).show();
        Button button = (Button) findViewById(R.id.button);
        queryData(smajor);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SelectionActivity.class);
               startActivity(intent);
            }
        });
    }


    private void choosedb(int db){
        if(db>=0&&db<=1)
            database=openOrCreateDatabase("biz.db",MODE_PRIVATE,null);
        else if(db>=2&&db<=5)
            database=openOrCreateDatabase("engineer.db",MODE_PRIVATE,null);
        else if(db>=6&&db<=8)
            database=openOrCreateDatabase("sw.db",MODE_PRIVATE,null);
        else if(db>=9&&db<=15)
            database=openOrCreateDatabase("hss.db",MODE_PRIVATE,null);
        else if(db>=16&&db<=20)
            database=openOrCreateDatabase("natural.db",MODE_PRIVATE,null);
        else if(db>=21&&db<=28)
            database=openOrCreateDatabase("ei.db",MODE_PRIVATE,null);
        else
            database=openOrCreateDatabase("kwlaw.db",MODE_PRIVATE,null);
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
syear+=1;
        String sql1 = "select 학정번호, 과목명, 이수, 담당교수, 시간1 from ";
        String sql2 = " where 이수 like '%필' and 학정번호 like '_____"+syear+"%'";
        String sql = sql1+table+sql2;

        try{
            Cursor c = database.rawQuery(sql,null);

            if( c!=null)
            {
                int count = c.getCount();

                for(int i=0;i<count;i++)
                {
                    c.moveToNext();
                    {
                        for(int j=0;j<5;j++)
                        {
                            tvArray[i*5+j].setText(c.getString(j));
                        }
                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
            Log.e("",e.getMessage());
        }
    }
}
