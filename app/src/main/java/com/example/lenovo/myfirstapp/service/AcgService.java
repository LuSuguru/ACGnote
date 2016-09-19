package com.example.lenovo.myfirstapp.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.lenovo.myfirstapp.R;
import com.example.lenovo.myfirstapp.activity.MainActivity;

/**
 * Created by lenovo on 2016/9/9.
 */
public class AcgService extends IntentService {

    public static final String EXTRA_PVCSERVICE_ID = "com.example.service.pvc";
    public static final String EXTRA_BOOKSERVICE_ID = "com,example.service.book" ;


    public static final String ACTION_SHOW_NOTIFICATION = "com.example.myfirstapp.SHOW_NOTIFICATION";
    public static final String PERM_PRIVATE = "com.example.lenovo.myfirstapp.PVC";

    private static final String TAG = "PollService";

    private static float allPvcMoney;
    private static float allBookMoney;

    private static boolean isStart;

    private static SharedPreferences sp;

    public AcgService() {
        super(TAG);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Notification notification;

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        allPvcMoney =  sp.getFloat("allPvcMoney", 0);
        allBookMoney = sp.getFloat("allBookMoney", 0);


        PendingIntent pi = PendingIntent.getActivity(this, 0 ,new Intent(this, MainActivity.class), 0);
        if(allPvcMoney != 0 || allBookMoney != 0) {
            notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("你本月还有剩余补款")
                    .setContentText("你本月还需补款手办" + allPvcMoney + "元")
                    .setSubText("你本月还需补款轻小说" + allBookMoney + "元")
                    .setContentTitle("剩余补款")
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(this).build();
        }


        Log.d("通知", allBookMoney+" "+allPvcMoney);
        showBackgroundNotification(0, notification);

    }

    /**
     *  启动时间定时器控制Service
     */
    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = new Intent(context, AcgService.class);
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        PendingIntent pi = PendingIntent.getService(context, 0 ,i ,0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(isOn) {
            alarmManager.setRepeating(AlarmManager.RTC , System.currentTimeMillis(), 1000 * 15, pi);
        }else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    void showBackgroundNotification(int requestCode, Notification notification) {
        Intent i1 = new Intent(ACTION_SHOW_NOTIFICATION);
        i1.putExtra("REQUEST_CODE", requestCode);
        i1.putExtra("NOTIFICATION", notification);

        sendOrderedBroadcast(i1, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }

    public void setPvcIntent(Intent i) {
        allPvcMoney = i.getFloatExtra(EXTRA_PVCSERVICE_ID, 0);
        sp.edit().putFloat("allPvcMoney", allPvcMoney).commit();
        Log.d("allPvcMoney", allPvcMoney +"");
    }

    public void setBookIntent(Intent i) {
        allBookMoney = i.getFloatExtra(EXTRA_BOOKSERVICE_ID, 0);
        sp.edit().putFloat("allBookMoney", allBookMoney).commit();
        Log.d("allBookMoney", allBookMoney +"");
    }


}
