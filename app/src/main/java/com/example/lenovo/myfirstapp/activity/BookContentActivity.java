package com.example.lenovo.myfirstapp.activity;

import android.support.v4.app.Fragment;

import com.example.lenovo.myfirstapp.fragment.BookContentFragment;

import java.util.UUID;

/**
 * Created by lenovo on 2016/9/16.
 */
public class BookContentActivity extends SingleActivity{
    public static final int BOOK_TAG = 2222;

    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(BookContentFragment.EXTRA_BOOK_CONTENT_ID);
        return BookContentFragment.newInstance(uuid);
    }
}
