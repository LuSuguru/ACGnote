package com.example.lenovo.myfirstapp.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.lenovo.myfirstapp.model.PvcLab;

/**
 * Created by lenovo on 2016/9/4.
 */
public class PvcLabCursorLoader extends AsyncTaskLoader<PvcLab> {

    private PvcLab mPvcLab;
    private Context mContext;
    public PvcLabCursorLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public PvcLab loadInBackground() {
        return PvcLab.get(mContext);
    }

    @Override
    public void deliverResult(PvcLab data) {
        mPvcLab = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
       if(mPvcLab != null) {
           deliverResult(mPvcLab);
       } else {
           forceLoad();
       }
    }
}
