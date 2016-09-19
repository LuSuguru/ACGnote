package com.example.lenovo.myfirstapp.activity;

import android.support.v4.app.Fragment;

import com.example.lenovo.myfirstapp.fragment.ContentFragment;

import java.util.UUID;

public class ContentActivity extends SingleActivity {
    public static final int PVC_TAG = 1111;
    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(ContentFragment.EXTRA_CONTENT_ID);
        return ContentFragment.newInstance(uuid);
    }
}
