package com.example.lenovo.myfirstapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.myfirstapp.R;

/**
 * Created by lenovo on 2016/9/13.
 */
public class DateChooseView extends LinearLayout{

    private TextView mDateText, mAllMoney;
    private RelativeLayout linearLayout_biaoti;
    private OnDateChangeListener mOnDateChangeListener;

    public DateChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View v = LayoutInflater.from(context).inflate(R.layout.date_choose_view, this);
        initView(v);
    }

    private void initView(View v) {
        mDateText = (TextView) v.findViewById(R.id.tv_time);
        mAllMoney = (TextView) v.findViewById(R.id.id_allMoney);
        linearLayout_biaoti = (RelativeLayout) v.findViewById(R.id.layout_date);
        linearLayout_biaoti.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnDateChangeListener.onDateChange();
            }
        });
    }
    public void setText(int year, int month) {
        String time = year + "-" + month;
        mDateText.setText(time);
    }

    public TextView getMallMoney() {
        return mAllMoney;
    }

    public void setDateChangeListener(OnDateChangeListener listener) {
        mOnDateChangeListener = listener;
    }

   public interface OnDateChangeListener {
        void onDateChange();
    }
}
