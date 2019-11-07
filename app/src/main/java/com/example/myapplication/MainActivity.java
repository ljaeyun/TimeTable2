package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    Integer id, year, major;
    EditText editTextid;

    private Integer getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    private Integer getyear() {
        return year;
    }

    private void setyear(int year) {
        this.year = year;
    }

    private Integer getmajor() {
        return major;
    }

    private void setmajor(int major) {
        this.major = major;
    }

    public static final String PACKAGE_DIR = "/data/data/com.example.myapplication/databases";
    public static final String[] Database_name = new String[9];

    public static void initialize(Context ctx) {
// check
        Database_name[0] = "test.db";
        Database_name[1] = "credit.db";
        Database_name[2] = "natural.db";
        Database_name[3] = "biz.db";
        Database_name[4] = "sw.db";
        Database_name[5] = "ei.db";
        Database_name[6] = "engineer.db";
        Database_name[7] = "hss.db";
        Database_name[8] = "kwlaw.db";

        File folder = new File(PACKAGE_DIR);
        folder.mkdirs();

        for (int i = 0; i < 9; i++) {
            File outfile = new File(PACKAGE_DIR + "/" + Database_name[i]);
            if (outfile.length() <= 0) {
                AssetManager assetManager = ctx.getResources().getAssets();
                try {
                    InputStream is = assetManager.open(Database_name[i], AssetManager.ACCESS_BUFFER);
                    long filesize = is.available();
                    byte[] tempdata = new byte[(int) filesize];
                    is.read(tempdata);
                    is.close();
                    outfile.createNewFile();
                    FileOutputStream fo = new FileOutputStream(outfile);
                    fo.write(tempdata);
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize(getApplicationContext());

        editTextid = (EditText) findViewById(R.id.editTextId);

        Button button1 = (Button) findViewById(R.id.button1);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerMajor);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinnerYear);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.학과, R.layout.spinner_item);
        spinner.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.학년, R.layout.spinner_item);
        spinner2.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//데이터베이스 열기
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setmajor(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setyear(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //id = 2016000000;
                String hakbun = "";
                hakbun = editTextid.getText().toString();

                if (hakbun.equals(""))
                    Toast.makeText(getApplicationContext(), "학번을 입력하세요", Toast.LENGTH_SHORT).show();
                else {
                    setId(Integer.parseInt(hakbun));//int로 바꿔서 전달

                    Intent intent = new Intent(getApplicationContext(), FirstSelect.class);
                    intent.putExtra("studentId", id);
                    intent.putExtra("studentYear", year);
                    intent.putExtra("studentMajor", major);
                    startActivity(intent);
                }
            }
        });
    }
}
