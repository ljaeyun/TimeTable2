package com.example.myapplication;

import java.util.ArrayList;

public class ClassSubject {
    private String name;//과목이름
    private ArrayList<TimeArr> time = new ArrayList<>();//시간배열 변경변경 이름 바꿔야겠다

    public ClassSubject(String name) {
        this.name = name;
    }

    public void put(TimeArr a)
    {//arraylist에 추가하는 함수
        time.add(a);//변경변경{0,1,2},{4,5,6}이렇게 되게
    }

    public String getName()
    {//과목이름 가져오는 함수
        return name;
    }

    public int getTimeSize()
    {
        return time.size();
    }

    public TimeArr getTimearr(int i)
    {
        return time.get(i);
    }

    public void setTimearr(TimeArr arr)
    {
        this.time.clear();
        this.time.add(arr);
    }
}
