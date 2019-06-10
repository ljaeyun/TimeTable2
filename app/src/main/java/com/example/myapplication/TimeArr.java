package com.example.myapplication;

import java.util.ArrayList;

public class TimeArr {
    private ArrayList<Integer> timearray = new ArrayList<>();
    private String code;//학정번호

    public void put(int a)
    {
        timearray.add(a);
    }

    public int print(int i)
    {
        return timearray.get(i);
    }
    public String getCode()
    {
        return code;
    }
    public int size()
    {
        return timearray.size();
    }

    public void putCode(String code)
    {
        this.code = code;
    }
}
