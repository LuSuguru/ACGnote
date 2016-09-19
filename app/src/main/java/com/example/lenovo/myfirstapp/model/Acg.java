package com.example.lenovo.myfirstapp.model;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public abstract class Acg {

    private Date mDate = new Date();
    private UseDate date;
    private UUID mId;

    private String mImageChoosePath;
    private String imagePath;

    private String mName;
    private String Factory;

    private float mStartMoney;
    private float mFinalMoney;

    private int mType;

    private boolean mIspay;
    private boolean mHaveImage;





    public Acg() {
        mId = UUID.randomUUID();
    }

    public void setmId(UUID id) {
        mId = id;
    }

    public UUID getmId() {
        return mId;
    }

    public String getmImageCHoosePath() {
        return mImageChoosePath;
    }

    public void setmImageCHoosePath(String mImageCHoosePath) {
        this.mImageChoosePath = mImageCHoosePath;
    }

    public boolean getmHaveImage() {
        return mHaveImage;
    }

    public void setmHaveImage(boolean mHaveImage) {
        this.mHaveImage = mHaveImage;
    }

    public UseDate getmDate() {
        if (date == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mDate);
            date = new UseDate();
            date.setMonth(calendar.get(Calendar.MONTH) + 1);
            date.setYear(calendar.get(Calendar.YEAR));
            return date;
        }else {
            return date;
        }
    }

    public void setmDate(UseDate date) {
        this.date = date;
    }

    public String getmImagePath() {
        return imagePath;
    }

    public void setmImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public float getmStartMoney() {
        return mStartMoney;
    }

    public void setmStartMoney(float mStartMoney) {
        this.mStartMoney = mStartMoney;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getFactory() {
        return Factory;
    }

    public void setFactory(String factory) {
        Factory = factory;
    }

    public float getmFinalMoney() {
        return mFinalMoney;
    }

    public void setmFinalMoney(float mFinalMoney) {
        this.mFinalMoney = mFinalMoney;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public boolean ismIspay() {
        return mIspay;
    }

    public void setmIspay(boolean mIspay) {
        this.mIspay = mIspay;
    }

    public abstract String getTag();
}
