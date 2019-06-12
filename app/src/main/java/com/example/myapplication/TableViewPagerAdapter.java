package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class TableViewPagerAdapter extends PagerAdapter {
    private Context mContext = null;

    public TableViewPagerAdapter()
    {

    }

    public TableViewPagerAdapter(Context context)
    {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View view = null;

        if(mContext != null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.table_one, container, false);

            TableLayout tableLayout = (TableLayout) view.findViewById(R.id.tableLayout);

            TextView textView = (TextView) view.findViewById(R.id.table);
            textView.setText("시간표" + position+1);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View)object);
    }

    @Override
    public int getCount(){
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
    {
        return (view == (View)object);
    }

}
