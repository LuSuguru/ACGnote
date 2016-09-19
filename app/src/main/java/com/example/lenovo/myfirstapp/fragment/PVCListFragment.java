package com.example.lenovo.myfirstapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.lenovo.myfirstapp.R;
import com.example.lenovo.myfirstapp.activity.ContentActivity;
import com.example.lenovo.myfirstapp.adapter.PvcAdapter;
import com.example.lenovo.myfirstapp.inter.CallBacks;
import com.example.lenovo.myfirstapp.model.Pvc;
import com.example.lenovo.myfirstapp.model.PvcLab;
import com.example.lenovo.myfirstapp.model.UseDate;
import com.example.lenovo.myfirstapp.service.AcgService;
import com.example.lenovo.myfirstapp.util.Helper;
import com.example.lenovo.myfirstapp.util.PvcLabCursorLoader;
import com.example.lenovo.myfirstapp.view.DateChooseView;
import com.example.lenovo.myfirstapp.view.Mylistview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PVCListFragment extends VisibleFragment implements LoaderManager.LoaderCallbacks<PvcLab>{

    private Calendar mCalendar;
    private Mylistview mMylistview;
    private DateChooseView mDateChooseView;

    private int initYear;
    private int initMonth;
    private int initDay;
    private float mAllMoey;

    private boolean isInit = true;

    private Helper mHelper;
    private Date mDate;
    private UseDate mUseDate;
    private ArrayList<Pvc> mPvcs;
    private PvcAdapter mAdapter;
    private CallBacks callBacks;
    private AcgService mService;

    private static final int REQUEST_CONLIST_DATE = 123;

    public static final String DIALOGLIST_DATE = "listdate";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callBacks = (CallBacks) getActivity();

        mCalendar = Calendar.getInstance();
        mDate = new Date();
        mCalendar.setTime(mDate);

        // 初始化日期
        initYear = mCalendar.get(Calendar.YEAR);
        initMonth = mCalendar.get(Calendar.MONTH) + 1;
        initDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        //将日期存储到时间存储器中
        mUseDate = new UseDate();
        mUseDate.setYear(initYear);
        mUseDate.setMonth(initMonth);
        callBacks.onPvcDateselected(mUseDate);

        LoaderManager lm = getLoaderManager();
        lm.initLoader(0, null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View tv = inflater.inflate(R.layout.item_pvc_content, container, false);
        mMylistview= (Mylistview) tv.findViewById(R.id.my_listview);

        mMylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pvc c = (Pvc) mAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra(ContentFragment.EXTRA_CONTENT_ID, c.getmId());
                startActivity(intent);
            }
        });
        mMylistview.setRemoveListener(new Mylistview.RemoveListener() {
            @Override
            public void removeItem(Mylistview.RemoveDirection direction, int position) {
                Pvc c = (Pvc) mAdapter.getItem(position);
                PvcLab.get(getActivity()).deletePvcLab(c);
                mAdapter.getFilter().filter(null);
            }
        });

        mDateChooseView = (DateChooseView) tv.findViewById(R.id.id_datechoose);
        mDateChooseView.setText(initYear, initMonth);
        mDateChooseView.setDateChangeListener(new DateChooseView.OnDateChangeListener() {
            @Override
            public void onDateChange() {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mUseDate);
                dialog.setTargetFragment(PVCListFragment.this, REQUEST_CONLIST_DATE );
                dialog.show(fm,DIALOGLIST_DATE);
            }
        });
        return tv;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isInit == true) {           // 第一次加载不用筛选
            isInit = false;
            return;
        } else {
            mAdapter.updateDate(mUseDate);
            mAdapter.getFilter().filter(null);
        }
    }

    /**
     *  在List不可见时计算总价格，并传递给service
     */
    @Override
    public void onPause() {
        super.onPause();

        mHelper = new Helper();
        mAllMoey = mHelper.getAllMoney(mPvcs, mUseDate, Helper.PVC);
        Intent i = new Intent();
        i.putExtra(AcgService.EXTRA_PVCSERVICE_ID, mAllMoey);
        mService.setPvcIntent(i);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode !=DatePickerFragment.LIST_REQUEST) return;

        if(requestCode == REQUEST_CONLIST_DATE) {
            UseDate date = (UseDate) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            callBacks.onPvcDateselected(date);
            int updateYear = date.getYear();
            int updateMonth = date.getMonth();
            mDateChooseView.setText(updateYear, updateMonth);
            mAdapter.updateDate(date);
            mAdapter.getFilter().filter(null);
        }
    }

    @Override
    public Loader<PvcLab> onCreateLoader(int i, Bundle bundle) {
        return new PvcLabCursorLoader(getActivity());
    }

    /**
     *  第一次new 单例时要从数据库加载数据，这里通过异步加载
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<PvcLab> loader, PvcLab data) {
        mPvcs = data.getmPvcs();
        mAdapter = new PvcAdapter(getActivity(), mPvcs, mUseDate, mDateChooseView.getMallMoney());
        mMylistview.setAdapter(mAdapter);

        mHelper = new Helper();
        mAllMoey = mHelper.getAllMoney(mPvcs, mUseDate, Helper.PVC);
        Intent i = new Intent();
        i.putExtra(AcgService.EXTRA_PVCSERVICE_ID, mAllMoey);
        mService = new AcgService();
        AcgService.setServiceAlarm(getActivity(), true);
        mService.setPvcIntent(i);
    }

    @Override
    public void onLoaderReset(Loader<PvcLab> loader) {

    }

}




