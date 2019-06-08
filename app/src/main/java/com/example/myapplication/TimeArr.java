package com.example.myapplication;

import java.util.ArrayList;

public class TimeArr {
    private ArrayList<Integer> timearray = new ArrayList<>();

    public void put(int a)
    {
        timearray.add(a);
    }

    public int print(int i)
    {
        return timearray.get(i);
    }
    public int size()
    {
        return timearray.size();
    }
}
