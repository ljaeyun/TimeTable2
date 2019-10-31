package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

public class RemoveClass extends Activity {
    int index;//지울 index
    String str = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup);

        Intent intent1 = getIntent();
        index = intent1.getIntExtra("index", 0);
        str = intent1.getStringExtra("classname");

        Button b = (Button) findViewById(R.id.button_close2);
        b.setVisibility(View.GONE);//닫기버튼도 없애고
        ScrollView sc = (ScrollView) findViewById(R.id.scrollview);
        sc.setVisibility(View.GONE);//scrollview도 없애고

        LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout2);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(0, 20, 0, 20);
        param.gravity = Gravity.CENTER;
        TextView textView = new TextView(this);
        textView.setText(str + " 과목을 삭제하시겠습니까?");//나중에 수정
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        ll.addView(textView, param);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        ll.addView(linearLayout, param);

        Button button1 = new Button(this);
        button1.setText("확인");
        button1.setTextSize(15);

        Button button2 = new Button(this);
        button2.setText("취소");
        button2.setTextSize(15);

        param = new LinearLayout.LayoutParams(220, 120);
        param.setMargins(30, 0, 30, 0);
        linearLayout.addView(button1, param);
        linearLayout.addView(button2, param);

        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("yes", 0);
                intent.putExtra("index", index);
                setResult(RESULT_OK, intent);
                finish();//닫기
            }
        });

        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("yes", 1);
                setResult(RESULT_OK, intent);
                finish();//닫기
            }
        });
    }
}
