package com.example.lenovo.myfirstapp.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.lenovo.myfirstapp.service.AcgService;


public class VisibleFragment extends Fragment{

       public BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
           @Override
           public void onReceive(Context context, Intent intent) {
               setResultCode(Activity.RESULT_CANCELED);
               Log.d("onShowNotificaion",2312 +"");
           }
       };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(AcgService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mOnShowNotification, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mOnShowNotification);
    }

    public static VisibleFragment newInstance(String title) {
        switch (title) {
            case "预定手办":
                return new PVCListFragment();
            case "预定轻小说":
                return new BookListFragment();
            case "其他周边":
                return new OtherListFragment();
        }
        return new PVCListFragment();
    }
}
