package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;


public class FirstSelect extends AppCompatActivity {
    Integer sid, syear, smajor, majorNum, minorNum, subMajorNum = 0, doubleMajorNum = 0, subMajor = 0, doubleMajor = 0;
    String spw;

    CheckBox chksubMajor, chkdoubleMajor;
    Spinner spinnerMajor1, spinnerMajor2, selMajorCount, selDoubleCount, selMajorSpinner, selMinorSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        Intent intent1 = getIntent();   // 시간표에 값 전달??

        sid = intent1.getIntExtra("studentId", 1);
        spw = intent1.getStringExtra("studentPw");
        syear = intent1.getIntExtra("studentYear", 1);
        smajor = intent1.getIntExtra("studentMajor", 1);

        selMajorSpinner = (Spinner) findViewById(R.id.MajorNum);
        selMinorSpinner = (Spinner) findViewById(R.id.MinorNum);

        selMajorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                majorNum = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selMinorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                minorNum = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chksubMajor = (CheckBox) findViewById(R.id.subMajor);
        chkdoubleMajor = (CheckBox) findViewById(R.id.doubleMajor);

        spinnerMajor1 = (Spinner) findViewById(R.id.spinnerMajor1);
        spinnerMajor1.setVisibility(View.INVISIBLE);

        spinnerMajor2 = (Spinner) findViewById(R.id.spinnerMajor2);
        spinnerMajor2.setVisibility(View.INVISIBLE);

        selMajorCount = (Spinner) findViewById(R.id.subMajorNum);
        selMajorCount.setVisibility(View.INVISIBLE);

        selDoubleCount = (Spinner) findViewById(R.id.doubleMajorNum);
        selDoubleCount.setVisibility(View.INVISIBLE);

        chksubMajor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean arg1) {
                if (chksubMajor.isChecked() == true)    // 나중에 애니메이션효과 추가1
                {
                    spinnerMajor1.setVisibility(View.VISIBLE);
                    selMajorCount.setVisibility(View.VISIBLE);
                } else {
                    spinnerMajor1.setVisibility(View.INVISIBLE);
                    selMajorCount.setVisibility(View.INVISIBLE);
                }
            }
        });

        chkdoubleMajor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkdoubleMajor.isChecked() == true) // 나중에 애니메이션효과 추가2
                {
                    spinnerMajor2.setVisibility(View.VISIBLE);
                    selDoubleCount.setVisibility(View.VISIBLE);
                } else {
                    spinnerMajor2.setVisibility(View.INVISIBLE);
                    selDoubleCount.setVisibility(View.INVISIBLE);
                }
            }
        });

        spinnerMajor1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subMajor = position;//부전공
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMajor2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doubleMajor = position;//복수전공
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selMajorCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subMajorNum = position;//부전공개수
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selDoubleCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doubleMajorNum = position;//복수전공개수
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, TimeTableActivity.class);
        intent.putExtra("studentId", sid);
        intent.putExtra("studentPw", spw);
        intent.putExtra("studentYear", syear);
        intent.putExtra("studentMajor", smajor);

        intent.putExtra("majorNum", majorNum);
        intent.putExtra("minorNum", minorNum);
        intent.putExtra("subMajor", subMajor);
        intent.putExtra("subMajorNum", subMajorNum);
        intent.putExtra("doubleMajor", doubleMajor);
        intent.putExtra("doubleMajorNum", doubleMajorNum);

        startActivity(intent);
    }
}
