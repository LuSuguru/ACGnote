package com.example.lenovo.myfirstapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.myfirstapp.R;
import com.example.lenovo.myfirstapp.model.UseDate;

/**
 * Created by lenovo on 2016/8/31.
 */
public class DatePickerFragment extends DialogFragment {
    private int mYear;
    private int mMonth;

    private EditText mYearText;
    private EditText mMonthText;

    public static final String EXTRA_DATE = "com.example.lenovo.myfirstapp.date";

    public static final int CONTENT_REQUEST = 523;
    public static final int LIST_REQUEST = 524;
    public static final int BOOK_LIST_REQUEST = 525;
    public static final int BOOK_CONTENT_REQUEST = 526;

    private UseDate mDate;



    public static DatePickerFragment newInstance(UseDate date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public DatePickerFragment () {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (UseDate) getArguments().getSerializable(EXTRA_DATE);
        mYear = mDate.getYear();
        mMonth = mDate.getMonth();
        View v = getActivity().getLayoutInflater().inflate(R.layout.item_mydatepicker, null);
        initView(v);
        initEvent();

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("日期选择")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       try{

                           // 当月份不准确时返回原年月
                           if(Integer.parseInt(mMonthText.getText().toString()) < 1 || Integer.parseInt(mMonthText.getText().toString()) > 12) {
                               Toast.makeText(getActivity(), "请输入正确的月份", Toast.LENGTH_SHORT).show();
                                mDate.setYear(mYear);
                                mDate.setMonth(mMonth);
                               return;
                           } else {
                               switch (getTag()) {
                                   case ContentFragment.DIALOG_DATE:
                                       sendResult(CONTENT_REQUEST);
                                       break;
                                   case PVCListFragment.DIALOGLIST_DATE:
                                       sendResult(LIST_REQUEST);
                                       break;
                                   case BookListFragment.DIALOGLIST_BOOK_DATE:
                                       sendResult(BOOK_LIST_REQUEST);
                                       break;
                                   case BookContentFragment.DIALOG_BOOK_DATE:
                                       sendResult(BOOK_CONTENT_REQUEST);
                               }
                           }
                       } catch (NumberFormatException e) {
                           return;
                       }
                    }
                })
                .create();
    }

    private void initEvent() {
        mYearText.setText(mYear + "");
        mYearText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    mDate.setYear(Integer.parseInt(charSequence.toString()));
                }catch (NumberFormatException e) {
                    mDate.setYear(1970);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mMonthText.setText(mMonth+ "");
        mMonthText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               try {
                   mDate.setMonth(Integer.parseInt(charSequence.toString()));
               }catch (NumberFormatException e){
                   mDate.setMonth(1);
               }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initView(View v) {
        mYearText = (EditText) v.findViewById(R.id.id_year);
        mMonthText = (EditText) v.findViewById(R.id.id_month);
    }

    private void sendResult(int resultCode) {
        if(getTargetFragment() == null) return;

        switch (resultCode) {

            case CONTENT_REQUEST :
                Intent i1 = new Intent();
                i1.putExtra(EXTRA_DATE, mDate);
                getTargetFragment().onActivityResult(getTargetRequestCode(), CONTENT_REQUEST, i1);
                break;

            case LIST_REQUEST :
                Intent i2 = new Intent();
                i2.putExtra(EXTRA_DATE, mDate);
                getTargetFragment().onActivityResult(getTargetRequestCode(), LIST_REQUEST, i2);
                break;

            case BOOK_LIST_REQUEST :
                Intent i3 = new Intent();
                i3.putExtra(EXTRA_DATE, mDate);
                getTargetFragment().onActivityResult(getTargetRequestCode(), BOOK_LIST_REQUEST, i3);
                break;

            case BOOK_CONTENT_REQUEST :
                Intent i4 = new Intent();
                i4.putExtra(EXTRA_DATE, mDate);
                getTargetFragment().onActivityResult(getTargetRequestCode(), BOOK_CONTENT_REQUEST, i4);
        }
    }
}
