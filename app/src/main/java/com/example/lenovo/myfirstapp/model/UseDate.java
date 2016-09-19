package com.example.lenovo.myfirstapp.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/8/31.
 */
public class UseDate implements Serializable {
    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    private int year;

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    private int month;

    public UseDate() {

    }

}
