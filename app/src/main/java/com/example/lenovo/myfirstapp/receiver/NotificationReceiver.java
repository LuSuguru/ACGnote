package com.example.lenovo.myfirstapp.receiver;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("测试", "NotificationReceiver 已启动");
        if(getResultCode() != Activity.RESULT_OK) {
            return;
        }
        int requestCode = intent.getIntExtra("REQUEST_CODE", 0);

        Notification notification = intent.getParcelableExtra("NOTIFICATION");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, notification);

        }

}

