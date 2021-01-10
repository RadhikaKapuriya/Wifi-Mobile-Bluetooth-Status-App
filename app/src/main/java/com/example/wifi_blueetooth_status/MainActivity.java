package com.example.wifi_blueetooth_status;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        MyReceiver.ConnectivityReceiverListener  , MyReceiver_Blueetooth.BluetoothConnectivityReceiverListener{
    /*Views declaration*/
    Button btnExplicitBroadcast;
    private boolean isConnected;
    private LinearLayout linearConnectionAvailable , linear_connection_available_bluetooth;
    private Button errorPageRetry;
    ImageView ivnetwork,ivbluenetwork;
    TextView tvbluetext , tvnetwork;
    Switch sbluetooth;
    /*My Receiver declaration*/
    MyReceiver myReceiver;
    MyReceiver_Blueetooth myReceiver_blueetooth;
    BluetoothAdapter  bluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        myReceiver = new MyReceiver();
        myReceiver_blueetooth = new MyReceiver_Blueetooth();
        checkConnection();
        checkBluetoothConnection();
        startService();
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*initial */
    private void initView() {
//        linearNoConnectionAvailable = findViewById(R.id.linear_no_connection_available);
        errorPageRetry = findViewById(R.id.error_page_retry);
        linearConnectionAvailable = findViewById(R.id.linear_connection_available);
        linear_connection_available_bluetooth = findViewById(R.id.linear_connection_available_bluetooth);
        ivnetwork = findViewById(R.id.ivnetwork);
        ivbluenetwork = findViewById(R.id.ivbluenetwork);
        tvbluetext = findViewById(R.id.tvbluetext);
        tvnetwork = findViewById(R.id.tvnetwork);
        sbluetooth = findViewById(R.id.sbluetooth);
        errorPageRetry.setOnClickListener(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        sbluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.disable();
                    Toast.makeText(getApplicationContext(), " Turned off", Toast.LENGTH_LONG).show();

                } else if(bluetoothAdapter.disable()) {
                    bluetoothAdapter.enable();
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, 0);
                    Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /*method for check connection*/
    private void checkConnection() {
        isConnected = MyReceiver.isConnected();
        if (isConnected) {
//            linearConnectionAvailable.setVisibility(View.VISIBLE);
//            linearNoConnectionAvailable.setVisibility(View.GONE);
            tvnetwork.setText(R.string.connection_avail);
            ivnetwork.setImageResource(R.drawable.icon_connection_availalable);
//            Toast.makeText(this, "WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
        } else {
//            linearConnectionAvailable.setVisibility(View.GONE);
//            linearNoConnectionAvailable.setVisibility(View.VISIBLE);
            tvnetwork.setText(R.string.internet_not_connect_tex);
            ivnetwork.setImageResource(R.drawable.icon_no_connection_available);
//            Toast.makeText(this, "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();

        }
    }


    private void checkBluetoothConnection() {
        try {
            isConnected = MyReceiver_Blueetooth.getBluetoothState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sbluetooth.setChecked(isConnected);
        if (isConnected) {
//            linear_connection_available_bluetooth.setVisibility(View.VISIBLE);
//            linearNoConnectionAvailable.setVisibility(View.GONE);
            tvbluetext.setText(R.string.connection_avail_blue);
            ivbluenetwork.setImageResource(R.drawable.icon_connection_availalable);

//            Toast.makeText(this, "Bluetooth Connected!", Toast.LENGTH_SHORT).show();
        } else {
//            linear_connection_available_bluetooth.setVisibility(View.GONE);
//            linearNoConnectionAvailable.setVisibility(View.VISIBLE);
            tvbluetext.setText(R.string.connection_avail_blue_not);
            ivbluenetwork.setImageResource(R.drawable.icon_no_connection_available);
//            Toast.makeText(this, "No Bluetooth Connected!", Toast.LENGTH_SHORT).show();

        }
    }

    /*on Resume Method*/
    @Override
    protected void onResume() {
        super.onResume();
        /*for registering to broadcast receiver*/
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myReceiver, filter);

        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(myReceiver_blueetooth, filter1);

        AppController.getInstance().setConnectivityListener(this);
        AppController.getInstance().setConnectivityListenerBlue(this);
    }

    /*on stop method  to unregister the broadcast Receiver*/
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myReceiver);
        unregisterReceiver(myReceiver_blueetooth);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.error_page_retry:
                checkConnection();
                break;
        }
    }

    /*Override method of MyReciver Broadcast reciver*/
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        checkConnection();
    }


    @Override
    public void onBlueToothStatus(boolean isConnected) {
        checkBluetoothConnection();
    }
}