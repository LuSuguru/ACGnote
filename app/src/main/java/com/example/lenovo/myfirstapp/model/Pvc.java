package com.example.lenovo.myfirstapp.model;


/**
 * Created by lenovo on 2016/8/24.
 */
public class Pvc extends Acg{

    private String mExplain;

    public String getmExplain() {
        return mExplain;
    }

    public void setmExplain(String mExplain) {
        this.mExplain = mExplain;
    }


    @Override
    public String getTag() {
        return "Pvc";
    }
}
