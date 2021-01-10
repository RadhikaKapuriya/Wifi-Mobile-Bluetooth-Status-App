package com.example.wifi_blueetooth_status;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/*This class is MyReceiver Broadcast receiver */
public class MyReceiver_Blueetooth extends BroadcastReceiver {
static Context mcontext;
    public static BluetoothConnectivityReceiverListener connectivityReceiverListener;

    public MyReceiver_Blueetooth() {
        super();
    }




    public static boolean getBluetoothState() throws Exception {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        boolean status = false;
        if (bluetoothAdapter == null) {
            throw new Exception("bluetooth device not found!");
        } else {
            int state =  bluetoothAdapter.getState();

            switch(state) {
                case BluetoothAdapter.STATE_OFF:
                    status = false;
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    status = false;
                    break;
                case BluetoothAdapter.STATE_ON:
                    status = true;
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    status = true;
                    break;
            }

        }
        return status;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext=context;

        /*get action name from activity which is trigger the broadcast receiver*/
        String action = intent.getAction();

        /*check action name*/
        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            if (connectivityReceiverListener != null) {

            }
            switch(state) {
                case BluetoothAdapter.STATE_OFF:
                    connectivityReceiverListener.onBlueToothStatus(false);
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    connectivityReceiverListener.onBlueToothStatus(false);
                    break;
                case BluetoothAdapter.STATE_ON:
                    connectivityReceiverListener.onBlueToothStatus(true);
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    connectivityReceiverListener.onBlueToothStatus(true);
                    break;
            }
        }

    }





    public interface BluetoothConnectivityReceiverListener {
        void onBlueToothStatus(boolean isConnected);
    }
}
