package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
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
    //int[] colors = new int[]{Color.rgb(241, 154, 150), Color.rgb(255, 184, 121), Color.rgb(255, 232, 167), Color.argb(66, 159, 215, 149), Color.argb(66, 105, 182, 227), Color.argb(66, 0, 74, 139), Color.argb(66, 139, 108, 79)};

    TextView tvArray2[] = new TextView[35];
    ArrayList<ArrayList<ClassSubject>> rrr;

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
            drawTable(rrr.get(position));
        }

        container.addView(view);

        return view;
    }

    @Override

    public int getItemPosition(Object object) {

        return POSITION_NONE;

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

    public void drawTable(ArrayList<ClassSubject> classlist) {
        for (int i = 0; i < 5; i++)
            tvArray2[i*7].setText("      ");
        for (int j = 0; j < classlist.size(); j++) {
            for (int i = 0; i < classlist.get(j).getTimearr(0).size(); i++) {
                if ((classlist.get(j).getTimearr(0).print(i) % 10) < 7 && classlist.get(j).getTimearr(0).print(i) < 50)//6교시까지만 표시, 금요일까지만 표시
                {
                    int t = changeint(classlist.get(j).getTimearr(0).print(i));
                    tvArray2[t].setText(classlist.get(j).getTimearr(0).getProf());
                    tvArray2[t].append("\n" + classlist.get(j).getName());
                    tvArray2[t].setBackgroundResource(R.drawable.cell);
                    isSeries(classlist.get(j), t, j);
                }
            }
        }

    }

    public boolean contains(ClassSubject cs, int t) {
        for (int i = 0; i < cs.getTimearr(0).size(); i++) {
            if (changeint(cs.getTimearr(0).print(i)) == t)
                return true;//있다!
        }
        return false;
    }

    public void isSeries(ClassSubject cs, int t, int j) {
        if (cs.getTimearr(0).size() < 3) {
            if (contains(cs, t - 1)) { //위에 연강존재
                tvArray2[t].setBackgroundResource(R.drawable.cell_top);
                tvArray2[t].setText("");
            } else if (contains(cs, t + 1)) {//아래에
                tvArray2[t].setBackgroundResource(R.drawable.cell_bottom);
                tvArray2[t].setText(cs.getTimearr(0).getProf());
                tvArray2[t].append("\n" + cs.getName());
            } else {//연강 안존재함
                tvArray2[t].setBackgroundResource(R.drawable.cell);
                tvArray2[t].setText(cs.getTimearr(0).getProf());
                tvArray2[t].append("\n" + cs.getName());
            }
        } else {
            if (contains(cs, t - 1) && contains(cs, t + 1)) {//둘다!
                tvArray2[t].setBackgroundResource(R.drawable.cell_middle);
                tvArray2[t].setText("");
            } else {
                if (contains(cs, t - 1)) {//위에 연강존재
                    tvArray2[t].setBackgroundResource(R.drawable.cell_top);
                    tvArray2[t].setText("");
                }
                if (contains(cs, t + 1)) {//아래에
                    tvArray2[t].setBackgroundResource(R.drawable.cell_bottom);
                    tvArray2[t].setText(cs.getTimearr(0).getProf());
                    tvArray2[t].append("\n" + cs.getName());
                }
            }

        }


    }
}

