package com.motherslove.stephnoutsa.mothersloveteam;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.support.annotation.Nullable;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MyService extends Service {

    NotificationCompat.Builder notification;
    SimpleDateFormat sdf;
    MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
    String lmp;
    Date lmpDate;
    Date today;
    Context context = this;

    public MyService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        lmp = dbHandler.getLMP();
        String expectedPattern = "MMM d, yyyy";
        sdf = new SimpleDateFormat(expectedPattern);

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true); // Delete notification from screen after user has viewed it.

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    lmpDate = sdf.parse(lmp);
                    today = new Date();
                    long diffInMs = today.getTime() - lmpDate.getTime();
                    long diff = diffInMs / (1000 * 60 * 60 * 24 * 7);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        return START_STICKY;
    }
}
