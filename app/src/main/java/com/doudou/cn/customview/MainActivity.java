package com.doudou.cn.customview;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.doudou.cn.customview.blueutils.AbstractBluetoothListener;
import com.doudou.cn.customview.blueutils.BluetoothReceiver;
import com.doudou.cn.customview.blueutils.BluetoothScanner;
import com.doudou.cn.customview.blueutils.DefaultBluetoothScanner;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private int REQUEST_OPEN_BT_CODE = 100;
    private Button startBtn, autoStartBlue;


    private BluetoothDevice connectDevice;
    private BluetoothAdapter adapter;
    private BluetoothScanner bluetoothScanner;
    private Set<BluetoothDevice> bondedDevices;
    private Set<BluetoothDevice> conectedDevice = new HashSet<>();

    private BluetoothReceiver bluetoothReceiver;
    private String deviceName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBtn = (Button) findViewById(R.id.statBtn);
        startBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent, REQUEST_OPEN_BT_CODE);
                    }
                }
        );

        adapter = BluetoothAdapter.getDefaultAdapter();
        /**
         * 自动的开启蓝牙的设别的界面
         */
        autoStartBlue = (Button) findViewById(R.id.autoStartBlue);
        autoStartBlue.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        connectDevice();
                        initReciverBrocast();
                    }
                }
        );
    }

    /**
     * 初始化广播的事件
     */
    private void initReciverBrocast(){

        bluetoothReceiver = new BluetoothReceiver();
        bluetoothReceiver.register(MainActivity.this);
        bluetoothReceiver.setListener(
                new AbstractBluetoothListener() {
                    @Override
                    public void onFound(BluetoothDevice remoteDevice, String remoteName, short rssi, BluetoothClass bluetoothClass) {

                        Log.i(TAG,"-------onFound--------");
                    }

                    @Override
                    public void onConnect(BluetoothDevice remoteDevice) {
                        Log.i(TAG,"-------onConnect--------");
                    }

                    @Override
                    public void onDisconnect(BluetoothDevice remoteDevice) {
                        Log.i(TAG,"-------onDisConnect--------");
                    }

                    @Override
                    public void onStateOn() {
                        Log.i(TAG,"-------onStateOn--------");
                    }

                    @Override
                    public void onStateOFF() {
                        Log.i(TAG,"-------onStateOff--------");
                    }

                    @Override
                    public void onRequestBluetoothEnable() {
                        Log.i(TAG,"-------onRequestBluetoothEnable--------");
                    }

                    @Override
                    public void onDiscoveryStarted() {
                        Log.i(TAG,"-------onDiscoveryStarted--------");
                    }

                    @Override
                    public void onDiscoveryFinished() {
                        Log.i(TAG,"-------onDiscoveryFinished--------");
                    }

                    @Override
                    public void onPairingRequest(BluetoothDevice remoteDevice, String remoteName, String pairingKey, int pairingVariant) {
                        Log.i(TAG,"-------onPairingRequest--------");
                    }

                    @Override
                    public void onActionAclDisConnected() {
                        Log.i(TAG,"-------onActionAclDisConnected--------");
                    }
                }
        );


    }
    private void connectDevice() {

        adapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothScanner = new DefaultBluetoothScanner(adapter);
        if (!bluetoothScanner.isEnable()) {
            Log.d(TAG, "蓝牙处于未开启状态,正在打开");
            Toast.makeText(
                    MainActivity.this
                    , "正在打开蓝牙", Toast.LENGTH_SHORT
            ).show();
           // bluetoothScanner.enable();
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_OPEN_BT_CODE);
        } else {
            Log.d(TAG, "蓝牙处于已开启状态");
            connectBondedDeviceOrSearch();
        }

    }

    /**
     * 蓝牙连接的方法
     */

    private String getDeviceName() {
        return deviceName;
    }

    public void connectBondedDeviceOrSearch() {
        bondedDevices = adapter.getBondedDevices();
        Log.d(TAG, "已绑定的设备数量" + bondedDevices.size());
        if (bondedDevices != null && bondedDevices.size() > 0) {
            for (BluetoothDevice device : bondedDevices) {
                Log.d(TAG, "已绑定的设备名称: " + device.getName());
                if (getDeviceName().equalsIgnoreCase(device.getName())) {
                    Log.d(TAG, "找到匹配的设备,开始连接:" + device.getName());
                    connectDevice = device;
                    //连接蓝牙
//                    connectBluetoothDevice(connectDevice);
                    return;
                }
            }
            Log.d(TAG, "直接配对未发现设备");
        } else {
            Log.d(TAG, "无绑定设备");
        }
        Log.d(TAG, "开始搜索设备");
        Toast.makeText(MainActivity.this, "--开始搜索设备---", Toast.LENGTH_SHORT).show();
        //直接配对失败,开始搜索设备
        if (!adapter.isDiscovering()) {
            adapter.startDiscovery();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "---requestCode:" + requestCode + "--resultCode:" + resultCode);
        if(requestCode==REQUEST_OPEN_BT_CODE){
            if(resultCode==0){
                Log.i(TAG,"--requestCode 拒绝打开蓝牙设备--");
            } else if(resultCode==-1){
                Log.i(TAG,"---requestCode-打开蓝牙设备----");
            }
        }
    }
}
