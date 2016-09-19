package com.example.lenovo.myfirstapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.lenovo.myfirstapp.R;
import com.example.lenovo.myfirstapp.activity.ImageActivity;
import com.example.lenovo.myfirstapp.activity.ImageChooseActivity;
import com.example.lenovo.myfirstapp.model.Pvc;
import com.example.lenovo.myfirstapp.model.PvcLab;
import com.example.lenovo.myfirstapp.model.UseDate;
import com.example.lenovo.myfirstapp.util.ImageLoader;

import java.util.UUID;

/**
 * Created by lenovo on 2016/8/26.
 */
public class ContentFragment extends VisibleFragment {
    public static final String EXTRA_CONTENT_ID = "com.example.lenovo.myfirstapp.contentfragment";

    private static final int RESQUEST_CONTNET_DATE = 0635;

    private static final int RESQUEST_CHOOSE_IMAGE = 3432;

    public static final String DIALOG_DATE = "date";



    private Pvc mPvc;

    private EditText mName, mStartMoney, mFinalMoney, mFactory,mExplain;
    private RadioGroup mType;
    private CheckBox isPay;
    private Button mDate;
    private ImageView mPvcImage;

    public static ContentFragment newInstance(UUID pvcid) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CONTENT_ID, pvcid);
        ContentFragment cf = new ContentFragment();
        cf.setArguments(args);
        return cf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID uuid = (UUID) getArguments().getSerializable(EXTRA_CONTENT_ID);
        mPvc = PvcLab.get(getActivity()).getPvc(uuid);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_content, container, false);
        initView(v);
        initEvent();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mPvc.getmHaveImage()) {
            ImageLoader.getmInstance(1, ImageLoader.Type.LIFO).loadImage(mPvc.getmImagePath(), mPvcImage);
        }
    }

    private void initEvent() {
        mName.setText(mPvc.getmName());
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mPvc.setmName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mStartMoney.setText( mPvc.getmStartMoney() +"");
        mStartMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    mPvc.setmStartMoney(Float.valueOf(charSequence.toString()));
                }catch (NumberFormatException e) {
                    mPvc.setmStartMoney(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mFinalMoney.setText(mPvc.getmFinalMoney()+"");
        mFinalMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        mPvc.setmFinalMoney(Float.parseFloat(charSequence.toString()));
                    }catch (NumberFormatException e) {
                        mPvc.setmFinalMoney(0);
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mFactory.setText(mPvc.getFactory());
        mFactory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPvc.setFactory(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mExplain.setText(mPvc.getmExplain());
        mExplain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mPvc.setmExplain(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mType.check(mPvc.getmType());
        mType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
               mPvc.setmType(i);
            }
        });

        isPay.setChecked(mPvc.ismIspay());
        isPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mPvc.setmIspay(b);
            }
        });

        mDate.setText("时间 :" + mPvc.getmDate().getYear() +"-" + mPvc.getmDate().getMonth());
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mPvc.getmDate());
                dialog.setTargetFragment(ContentFragment.this, RESQUEST_CONTNET_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });


        mPvcImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mPvc.getmHaveImage()) {
                    Intent i = new Intent(getActivity(), ImageActivity.class);
                    i.putExtra(ImageActivity.EXTRA_IMAGE_ACTIVITY, mPvc.getmId());
                    startActivity(i);
                } else {
                    Intent i = new Intent(getActivity(), ImageChooseActivity.class);
                    i.putExtra(ImageChooseFragment.EXTRA_IMAGEIME_ID, mPvc.getmId());
                    startActivityForResult(i, RESQUEST_CHOOSE_IMAGE);
                }
            }
        });
    }

    private void initView(View v) {
        mName = (EditText) v.findViewById(R.id.edit_name);
        mStartMoney = (EditText) v.findViewById(R.id.start_money);
        mFinalMoney = (EditText) v.findViewById(R.id.final_money);
        mFactory = (EditText) v.findViewById(R.id.edit_factory);
        mExplain = (EditText) v.findViewById(R.id.id_explain);

        mType = (RadioGroup) v.findViewById(R.id.id_type);

        isPay = (CheckBox) v.findViewById(R.id.is_buy);

        mDate = (Button) v.findViewById(R.id.date);

        mPvcImage = (ImageView) v.findViewById(R.id.id_image);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case DatePickerFragment.CONTENT_REQUEST :
                if (requestCode == RESQUEST_CONTNET_DATE) {
                    UseDate date = (UseDate) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                    mPvc.setmDate(date);
                    mDate.setText("时间 :" + mPvc.getmDate().getYear() + "-" + mPvc.getmDate().getMonth());
                }
                break;
            case ImageChooseFragment.IMAGECHOOSE_REQUEST :
                if(requestCode == RESQUEST_CHOOSE_IMAGE) {
                    UUID uuid = (UUID) data.getSerializableExtra(EXTRA_CONTENT_ID);
                    mPvc = PvcLab.get(getActivity()).getPvc(uuid);
                    String path = mPvc.getmImagePath();
                    ImageLoader.getmInstance(1, ImageLoader.Type.LIFO).loadImage(path, mPvcImage);
                }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        PvcLab.get(getActivity()).updatePvcLab(mPvc);
    }


}
