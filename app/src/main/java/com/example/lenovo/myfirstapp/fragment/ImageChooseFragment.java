package com.example.lenovo.myfirstapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lenovo.myfirstapp.R;
import com.example.lenovo.myfirstapp.activity.BookContentActivity;
import com.example.lenovo.myfirstapp.activity.ImageActivity;
import com.example.lenovo.myfirstapp.model.Acg;
import com.example.lenovo.myfirstapp.model.BookLab;
import com.example.lenovo.myfirstapp.model.PvcLab;
import com.example.lenovo.myfirstapp.util.ImageLoader;

import java.util.UUID;

/**
 * Created by lenovo on 2016/9/5.
 */
public class ImageChooseFragment extends VisibleFragment implements View.OnClickListener{
    public static final String EXTRA_IMAGEIME_ID = "com.example.lenovo.myfirstapp.imagechoosefragment";

    public static final int REQUEST_IMAGE_DATE = 2765;

    public static final int IMAGECHOOSE_REQUEST = 2435;

    private Acg mAcg;
    private ImageView ImageChoose;
    private Button mSure;
    private Button mReturn;

    // 需要存储的图片路径
    private String mImagePath;

    //初始化的图片路径
    private String mInitPath;

    private String mShowPath;


    public static ImageChooseFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGEIME_ID, id);
        ImageChooseFragment cf = new ImageChooseFragment();
        cf.setArguments(args);
        return cf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID uuid = (UUID) getArguments().getSerializable(EXTRA_IMAGEIME_ID);

        mAcg = PvcLab.get(getActivity()).getPvc(uuid);
        if(mAcg == null) {
            mAcg = BookLab.get(getActivity()).getBook(uuid);
        }
        if(mAcg.getmImageCHoosePath() != null) {
            mImagePath = mAcg.getmImageCHoosePath();
        } else {
            mImagePath = mAcg.getmImagePath();
        }
        mShowPath = mImagePath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_imagechoose, container, false);
        initView(v);
        initEvent();
        return v;
    }

    private void initEvent() {

        ImageLoader.getmInstance(1, ImageLoader.Type.LIFO).loadImage(mShowPath, ImageChoose);
        ImageChoose.setOnClickListener(this);
        mSure.setOnClickListener(this);
        mReturn.setOnClickListener(this);
    }

    private void initView(View v) {
        ImageChoose = (ImageView) v.findViewById(R.id.id_imagechoose);
        mSure = (Button) v.findViewById(R.id.id_queren);
        mReturn = (Button) v.findViewById(R.id.id_quxiao);
        mInitPath = mAcg.getmImagePath();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_imagechoose :
                Intent i = new Intent(getActivity(), ImageActivity.class);
                i.putExtra(ImageActivity.EXTRA_IMAGE_ACTIVITY, mAcg.getmId());
                startActivityForResult(i, REQUEST_IMAGE_DATE );
                break;
            case R.id.id_queren :
                if(!mAcg.getmHaveImage()) {
                    Intent i1 = new Intent(getActivity(), BookContentActivity.class);
                    mAcg.setmHaveImage(true);
                    mAcg.setmImagePath(mImagePath);
                    i1.putExtra(BookContentFragment.EXTRA_BOOK_CONTENT_ID, mAcg.getmId());
                    startActivity(i1);
                    getActivity().finish();
                } else {
                    Intent i2 = new Intent();
                    i2.putExtra(BookContentFragment.EXTRA_BOOK_CONTENT_ID, mAcg.getmId());
                    getActivity().setResult(IMAGECHOOSE_REQUEST, i2);
                    mAcg.setmImagePath(mImagePath);
                    getActivity().finish();
                }
                break;
            case R.id.id_quxiao :
                mShowPath = mInitPath;
                mAcg.setmImagePath(mInitPath);
                getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_OK :
                if(requestCode == REQUEST_IMAGE_DATE) {
                    mImagePath = data.getStringExtra(ImageActivity.EXTRA_IMAGE_ACTIVITY);
                    mShowPath = mImagePath;
                    ImageLoader.getmInstance(1, ImageLoader.Type.LIFO).loadImage(mImagePath, ImageChoose);
                }
                break;
        }
    }
}
