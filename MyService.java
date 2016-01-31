package com.motherslove.stephnoutsa.mothersloveteam;


import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;

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

                    if(diff < 1) {
                        String contactphone = dbHandler.getPhone();
                        String smsBody = "It's been less than a week, but anything can. Don\'t forget your ANC next week";

                        String SMS_SENT = "SMS_SENT";
                        String SMS_DELIVERED = "SMS_DELIVERED";

                        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(SMS_SENT), 0);
                        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(SMS_DELIVERED), 0);

                        // When SMS has been sent
                        registerReceiver(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                switch (getResultCode()) {
                                    case Activity.RESULT_OK:
                                        Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();
                                        break;

                                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                        Toast.makeText(context, "Generic failure cause", Toast.LENGTH_SHORT).show();
                                        break;
                                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                                        Toast.makeText(context, "Service is currently unavailable", Toast.LENGTH_SHORT).show();
                                        break;
                                    case SmsManager.RESULT_ERROR_NULL_PDU:
                                        Toast.makeText(context, "No pdu provided", Toast.LENGTH_SHORT).show();
                                        break;
                                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                                        Toast.makeText(context, "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }, new IntentFilter(SMS_SENT));

                        // When SMS has been delivered
                        registerReceiver(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                switch (getResultCode()) {
                                    case Activity.RESULT_OK:
                                        Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                                        break;
                                    case Activity.RESULT_CANCELED:
                                        Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }, new IntentFilter(SMS_DELIVERED));

                        // Get default instance of SmsManager
                        SmsManager smsManager = SmsManager.getDefault();

                        // Send a text-based SMS
                        smsManager.sendTextMessage(contactphone, null, smsBody, sentPendingIntent, deliveredPendingIntent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread stephThread = new Thread(r);
        stephThread.start();

        return START_STICKY;
    }
}
