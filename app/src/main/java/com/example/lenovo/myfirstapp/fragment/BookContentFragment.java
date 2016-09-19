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
import com.example.lenovo.myfirstapp.model.Book;
import com.example.lenovo.myfirstapp.model.BookLab;
import com.example.lenovo.myfirstapp.model.UseDate;
import com.example.lenovo.myfirstapp.util.ImageLoader;

import java.util.UUID;

public class BookContentFragment extends VisibleFragment {
    public static final String EXTRA_BOOK_CONTENT_ID = "com.example.lenovo.myfirstapp.bookcontentfragment";

    private static final int RESQUEST_BOOK_CONTNET_DATE = 635;

    private static final int RESQUEST_BOOK_CHOOSE_IMAGE = 636;

    public static final String DIALOG_BOOK_DATE = "date.book";

    private Book mBook;

    private EditText mName, mStartMoney, mFinalMoney, mFactory;
    private RadioGroup mType;
    private CheckBox isPay;
    private Button mDate;
    private ImageView mBookImage;

    public static BookContentFragment newInstance(UUID bookid) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_BOOK_CONTENT_ID, bookid);
        BookContentFragment cf = new BookContentFragment();
        cf.setArguments(args);
        return cf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID uuid = (UUID) getArguments().getSerializable(EXTRA_BOOK_CONTENT_ID);
        mBook = BookLab.get(getActivity()).getBook(uuid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_book_content, container, false);
        initView(v);
        initEvent();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mBook.getmHaveImage()) {
            ImageLoader.getmInstance(1, ImageLoader.Type.LIFO).loadImage(mBook.getmImagePath(), mBookImage);
        }
    }

    private void initEvent() {
        mName.setText(mBook.getmName());
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mBook.setmName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mStartMoney.setText( mBook.getmStartMoney() +"");
        mStartMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    mBook.setmStartMoney(Float.valueOf(charSequence.toString()));
                }catch (NumberFormatException e) {
                    mBook.setmStartMoney(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mFinalMoney.setText(mBook.getmFinalMoney()+"");
        mFinalMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    mBook.setmFinalMoney(Float.parseFloat(charSequence.toString()));
                }catch (NumberFormatException e) {
                    mBook.setmFinalMoney(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mFactory.setText(mBook.getFactory());
        mFactory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mBook.setFactory(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mType.check(mBook.getmType());
        mType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mBook.setmType(i);
            }
        });

        isPay.setChecked(mBook.ismIspay());
        isPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mBook.setmIspay(b);
            }
        });

        mDate.setText("时间 :" + mBook.getmDate().getYear() +"-" + mBook.getmDate().getMonth());
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mBook.getmDate());
                dialog.setTargetFragment(BookContentFragment.this, RESQUEST_BOOK_CONTNET_DATE);
                dialog.show(fm, DIALOG_BOOK_DATE);
            }
        });


        mBookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBook.getmHaveImage()) {
                    Intent i = new Intent(getActivity(), ImageActivity.class);
                    i.putExtra(ImageActivity.EXTRA_IMAGE_ACTIVITY, mBook.getmId());
                    startActivity(i);
                } else {
                    Intent i = new Intent(getActivity(), ImageChooseActivity.class);
                    i.putExtra(ImageChooseFragment.EXTRA_IMAGEIME_ID, mBook.getmId());
                    startActivityForResult(i, RESQUEST_BOOK_CHOOSE_IMAGE);
                }
            }
        });
    }

    private void initView(View v) {
        mName = (EditText) v.findViewById(R.id.id_boook_name);
        mStartMoney = (EditText) v.findViewById(R.id.book_start_money);
        mFinalMoney = (EditText) v.findViewById(R.id.book_final_money);
        mFactory = (EditText) v.findViewById(R.id.book_edit_factory);

        mType = (RadioGroup) v.findViewById(R.id.id_book_type);

        isPay = (CheckBox) v.findViewById(R.id.book_is_buy);

        mDate = (Button) v.findViewById(R.id.book_date);

        mBookImage = (ImageView) v.findViewById(R.id.id_book_image);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case DatePickerFragment.BOOK_CONTENT_REQUEST :
                if (requestCode == RESQUEST_BOOK_CONTNET_DATE) {
                    UseDate date = (UseDate) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                    mBook.setmDate(date);
                    mDate.setText("时间 :" + mBook.getmDate().getYear() + "-" + mBook.getmDate().getMonth());
                }
                break;
            case ImageChooseFragment.IMAGECHOOSE_REQUEST :
                if(requestCode == RESQUEST_BOOK_CHOOSE_IMAGE) {
                    UUID uuid = (UUID) data.getSerializableExtra(EXTRA_BOOK_CONTENT_ID);
                    mBook = BookLab.get(getActivity()).getBook(uuid);
                    String path = mBook.getmImagePath();
                    ImageLoader.getmInstance(1, ImageLoader.Type.LIFO).loadImage(path, mBookImage);
                }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        BookLab.get(getActivity()).updateBookLab(mBook);
    }

}

