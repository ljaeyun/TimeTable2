package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class ClassSubject implements Parcelable {
    private String name;//과목이름
    private ArrayList<TimeArr> time = new ArrayList<>();//시간배열 변경변경 이름 바꿔야겠다

    public ClassSubject(String name) {
        this.name = name;
    }

    protected ClassSubject(Parcel in) {
        name = in.readString();
        time = (ArrayList<TimeArr>) in.readSerializable();
    }

    public static final Creator<ClassSubject> CREATOR = new Creator<ClassSubject>() {
        @Override
        public ClassSubject createFromParcel(Parcel in) {
            return new ClassSubject(in);
        }

        @Override
        public ClassSubject[] newArray(int size) {
            return new ClassSubject[size];
        }
    };

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
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeSerializable(time);
    }
}
