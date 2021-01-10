package com.example.wifi_blueetooth_status;

import android.app.Application;

import com.example.wifi_blueetooth_status.MyReceiver;

public class AppController extends Application {
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(MyReceiver.ConnectivityReceiverListener listener) {
        MyReceiver.connectivityReceiverListener = listener;
    }

    public void setConnectivityListenerBlue(MyReceiver_Blueetooth.BluetoothConnectivityReceiverListener listener) {
        MyReceiver_Blueetooth.connectivityReceiverListener = listener;
    }
}
