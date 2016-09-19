package com.example.lenovo.myfirstapp.activity;

import android.support.v4.app.Fragment;

import com.example.lenovo.myfirstapp.fragment.ImageChooseFragment;

import java.util.UUID;

/**
 * Created by lenovo on 2016/9/5.
 */
public class ImageChooseActivity extends SingleActivity {
    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(ImageChooseFragment.EXTRA_IMAGEIME_ID);
        return ImageChooseFragment.newInstance(uuid);
    }
}
