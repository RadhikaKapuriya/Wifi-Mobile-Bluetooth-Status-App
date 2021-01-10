package com.example.wifi_blueetooth_status;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    MyReceiver myReceiver;
    MyReceiver_Blueetooth myReceiver_blueetooth;
    private Timer timer = null;
    private TimerTask task = null;
    boolean  isConnected = false , isConnectedblue = false;

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myReceiver, filter);

        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(myReceiver_blueetooth, filter1);

    }

    private void setNotification() {
        isConnected = MyReceiver.isConnected();
        String nwteork_status = "" , bluetooth_status = "";
        try {
            isConnectedblue = MyReceiver_Blueetooth.getBluetoothState();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(isConnectedblue)
        {
            bluetooth_status = " || Bluetooth ON";
        }
        else
        {
            bluetooth_status = " || Bluetooth OFF";
        }
        if(isConnected)
        {
            nwteork_status = "WiFi/Mobile-Data ON";
        }
        else
        {
            nwteork_status = "WiFi/Mobile-Data OFF";
        }
        String status = nwteork_status + bluetooth_status;
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("WiFi/Mobile & Bluetooth Status")
                .setContentText(status)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (timer == null) {
            timer = new Timer();
            task = new TimerTask() {

                @Override
                public void run() {
                    setNotification();
                }
            };
            timer.schedule(task, 100, 1000);
        }



        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}