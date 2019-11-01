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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextid = (EditText) findViewById(R.id.editTextId);

        Button button1 = (Button) findViewById(R.id.button1);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerMajor);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinnerYear);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.학과,R.layout.spinner_item);
        spinner.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.학년,R.layout.spinner_item);
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
                Integer n = getmajor();
                //gethakbun();
                id = 2016000000;
                Intent intent = new Intent(getApplicationContext(), FirstSelect.class);
                intent.putExtra("studentId", id);
                intent.putExtra("studentYear", year);
                intent.putExtra("studentMajor", major);
                startActivity(intent);
            }
        });
    }

    private void gethakbun() {
        String hakbun = "";
        hakbun = editTextid.getText().toString();
        setId(Integer.parseInt(hakbun));//int로 바꿔서 전달
    }

}
