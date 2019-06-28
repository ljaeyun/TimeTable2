package com.example.myapplication;

import java.util.ArrayList;

public class TimeArr {
    private ArrayList<Integer> timearray = new ArrayList<>();
    private String code;//학정번호
    private String prof;//교수명
    private String esu;//이수
    private String timestr;//문자열로

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

    public void setprof(String prof)
    {
        this.prof = prof;
    }

    public String getProf()
    {
        return prof;
    }

    public void setEsu(String esu)
    {
        this.esu = esu;
    }

    public String getEsu()
    {
        return esu;
    }

    public String getTimestr(){return timestr;}

    public void setTimestr(String timestr)
    {
        this.timestr = timestr;
    }
}
