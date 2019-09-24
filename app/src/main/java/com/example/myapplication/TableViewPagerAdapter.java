package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TableViewPagerAdapter extends PagerAdapter {
    public static Context mContext = null;
    private int position;
    private int page = 1;
    int[] tableArray = new int[35];//시간표에 출력
    TextView[] tvArray2 = new TextView[35];
    ArrayList<ArrayList<ClassSubject>> rrr;

    public TableViewPagerAdapter() {

    }

    public TableViewPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;

        if (mContext != null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.table_one, container, false);

            TableLayout tableLayout = (TableLayout) view.findViewById(R.id.tableLayout);
            TextView textView = (TextView) view.findViewById(R.id.table);

            int tableNum = position + 1;
            this.position = position;
            textView.setText("시간표" + tableNum);
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                if (i == 0)
                    tableArray[i * 7 + j] = view.getResources().getIdentifier("monday" + j, "id", "com.example.myapplication");
                else if (i == 1)
                    tableArray[i * 7 + j] = view.getResources().getIdentifier("tuesday" + j, "id", "com.example.myapplication");
                else if (i == 2)
                    tableArray[i * 7 + j] = view.getResources().getIdentifier("wednesday" + j, "id", "com.example.myapplication");
                else if (i == 3)
                    tableArray[i * 7 + j] = view.getResources().getIdentifier("thursday" + j, "id", "com.example.myapplication");
                else if (i == 4)
                    tableArray[i * 7 + j] = view.getResources().getIdentifier("friday" + j, "id", "com.example.myapplication");
            }
        }
        for (int i = 0; i < tvArray2.length; i++) {
            tvArray2[i] = (TextView) view.findViewById(tableArray[i]);
        }

        if (rrr != null) {//필수과목이 있는 경우에만
            drawTable();
        }

        container.addView(view);
        return view;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return page;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }

    public int getPosition() {
        return this.position;
    }

    public void setRrr(ArrayList<ArrayList<ClassSubject>> rrr) {
        this.rrr = rrr;
        setPage(rrr.size());//페이지 수
        notifyDataSetChanged();
    }

    public int changeint(int t) {//아직 6교시칸밖에 없어서 변환
        int n = 0;
        n = t / 10;
        n = n * 7 + (t % 10);
        return n;
    }

    public void drawTable() {
        if (rrr.size() > position) {//rrr이 null일때 오류
            for (int j = 0; j < rrr.get(position).size(); j++) {
                for (int i = 0; i < rrr.get(position).get(j).getTimearr(0).size(); i++) {
                    if ((rrr.get(position).get(j).getTimearr(0).print(i) % 10) < 7 && rrr.get(position).get(j).getTimearr(0).print(i) < 50)//6교시까지만 표시, 금요일까지만 표시
                    {
                        int t = changeint(rrr.get(position).get(j).getTimearr(0).print(i));
                        tvArray2[t].setText(rrr.get(position).get(j).getTimearr(0).getProf());
                        tvArray2[t].append("\n" + rrr.get(position).get(j).getName());
                    }
                }
            }
        }
    }
}

