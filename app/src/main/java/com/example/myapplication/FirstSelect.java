package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;


public class FirstSelect extends AppCompatActivity{
    Integer sid, syear, smajor, db, freeDay = 0, position = 0;
    String spw;

    CheckBox subMajor, doubleMajor;
    Spinner spinnerMajor1, spinnerMajor2, selMajorCount ,selDoubleCount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        Intent intent1 = getIntent();   // 시간표에 값 전달??

        subMajor = (CheckBox)findViewById(R.id.subMajor);
        doubleMajor = (CheckBox)findViewById(R.id.doubleMajor);

        spinnerMajor1 = (Spinner)findViewById(R.id.spinnerMajor1);
        spinnerMajor1.setVisibility(View.INVISIBLE);

        spinnerMajor2 = (Spinner)findViewById(R.id.spinnerMajor2);
        spinnerMajor2.setVisibility(View.INVISIBLE);

        selMajorCount = (Spinner)findViewById(R.id.selMajorCount);
        selMajorCount.setVisibility(View.INVISIBLE);

        selDoubleCount = (Spinner)findViewById(R.id.selDoubleCount);
        selDoubleCount.setVisibility(View.INVISIBLE);

        subMajor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean arg1) {
                if(subMajor.isChecked() == true)    // 나중에 애니메이션효과 추가1
                {
                    spinnerMajor1.setVisibility(View.VISIBLE);
                    selMajorCount.setVisibility(View.VISIBLE);
                }
                else
                {
                    spinnerMajor1.setVisibility(View.INVISIBLE);
                    selMajorCount.setVisibility(View.INVISIBLE);
                }
            }
        });

        doubleMajor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(doubleMajor.isChecked() == true) // 나중에 애니메이션효과 추가2
                {
                    spinnerMajor2.setVisibility(View.VISIBLE);
                    selDoubleCount.setVisibility(View.VISIBLE);
                }
                else
                {
                    spinnerMajor2.setVisibility(View.INVISIBLE);
                    selDoubleCount.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void onClick(View view)
    {
        Intent intent = new Intent(this, TimeTableActivity.class);

        startActivity(intent);
    }
}
