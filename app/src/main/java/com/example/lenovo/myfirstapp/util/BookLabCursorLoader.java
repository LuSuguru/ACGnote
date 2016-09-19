package com.example.lenovo.myfirstapp.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.lenovo.myfirstapp.model.BookLab;

/**
 * Created by lenovo on 2016/9/16.
 */
public class BookLabCursorLoader extends AsyncTaskLoader<BookLab> {

    private BookLab mBookLab;
    private Context mContext;
    public BookLabCursorLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BookLab loadInBackground() {
        return BookLab.get(mContext);
    }

    @Override
    public void deliverResult(BookLab data) {
        mBookLab = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if(mBookLab != null) {
            deliverResult(mBookLab);
        } else {
            forceLoad();
        }
    }
}
