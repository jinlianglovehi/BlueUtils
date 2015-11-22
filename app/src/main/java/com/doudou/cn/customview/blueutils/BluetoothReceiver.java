package com.doudou.cn.customview.blueutils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;


/**
 * @author by liuhongjian on 15/7/16 11:20.
 */
public class BluetoothReceiver extends BroadcastReceiver {
	private static final String TAG = "BluetoothReceiver";
	private BluetoothListener listener;

	public void register(Context context) {
		context.registerReceiver(this, getBluetoothIntentFilter());
	}

	public void unRegister(Context context) {
		context.unregisterReceiver(this);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.i(TAG, "---蓝牙onReceiver_Action--:" + action);
		Bundle extras = intent.getExtras();
		printIntent(action, extras);
		switch (action) {
			/**
			 * 发现配对请求
			 * EXTRA_DEVICE
			 * EXTRA_NAME
			 * EXTRA_PAIRING_KEY
			 * EXTRA_PAIRING_VARIANT
			 */
			case BluetoothDevice.ACTION_PAIRING_REQUEST:
				Log.d(TAG, "onReceive BluetoothDevice.ACTION_PAIRING_REQUEST");
				BluetoothDevice remoteDevice4 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				String remoteName4 = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
				String pairingKey = intent.getStringExtra(BluetoothDevice.EXTRA_PAIRING_KEY);
				int pairingVariant = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, -1);
				listener.onPairingRequest(remoteDevice4, remoteName4, pairingKey, pairingVariant);
				break;
			/**
			 * 发现设备
			 * EXTRA_DEVICE
			 * EXTRA_CLASS
			 * EXTRA_NAME(可能有)
			 * EXTRA_RSSI(可能有)
			 */
			case BluetoothDevice.ACTION_FOUND:
				Log.v(TAG, "onReceive BluetoothDevice.ACTION_FOUND");
				BluetoothDevice remoteDevice0 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				String remoteName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
				short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
				BluetoothClass bluetoothClass = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
				Log.v(TAG, "onReceive Device info:" + " name::" + remoteDevice0.getName() + " uuid::" + remoteDevice0.getUuids() + " address::" + remoteDevice0.getAddress());
				listener.onFound(remoteDevice0, remoteName, rssi, bluetoothClass);
				break;
			/**
			 * 扫描模式改变,需要通过之前的扫描模式来判断场景
			 *  EXTRA_SCAN_MODE
			 *  EXTRA_PREVIOUS_SCAN_MODE
			 */
			case BluetoothAdapter.ACTION_SCAN_MODE_CHANGED:
				Log.v(TAG, "onReceive BluetoothDevice.ACTION_SCAN_MODE_CHANGED");
				int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1);
				int previousScanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, -1);
				switch (scanMode) {
					/**
					 * 不可连接,不可被发现
					 */
					case BluetoothAdapter.SCAN_MODE_NONE:
						break;
					/**
					 * 可连接,不可被发现
					 */
					case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
						break;
					/**
					 * 可连接,可被发现
					 */
					case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
						break;
				}
				switch (previousScanMode) {
					case BluetoothAdapter.SCAN_MODE_NONE:
						break;
					case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
						break;
					case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
						break;
				}
				break;
			/**
			 * 连接状态改变,需要通过之前的连接状态来判断场景
			 * EXTRA_CONNECTION_STATE
			 * EXTRA_PREVIOUS_CONNECTION_STATE:STATE_DISCONNECTED, STATE_CONNECTING, STATE_CONNECTED, STATE_DISCONNECTING.
			 * BluetoothDevice.EXTRA_DEVICE
			 */
			case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
				Log.v(TAG, "onReceive BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED");
				BluetoothDevice remoteDevice1 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				int connectionState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, -1);
				int previousConnectionState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_CONNECTION_STATE, -1);
				switch (connectionState) {
					/**
					 * 已连接,可以传输数据
					 */
					case BluetoothAdapter.STATE_CONNECTED:
						listener.onConnect(remoteDevice1);
						break;
					/**
					 * 未连接
					 */
					case BluetoothAdapter.STATE_DISCONNECTED:
						listener.onDisconnect(remoteDevice1);
						break;
					/**
					 * 正在断开连接,需要中止数据的传输
					 */
					case BluetoothAdapter.STATE_DISCONNECTING:
						break;
					/**
					 * 连接建立中
					 */
					case BluetoothAdapter.STATE_CONNECTING:
						break;
				}
				switch (previousConnectionState) {
					/**
					 * 之前为已连接,可以根据业务需求,决定直接断开,或者重新连接
					 */
					case BluetoothAdapter.STATE_CONNECTED:
						break;
					/**
					 * 之前为未连接
					 */
					case BluetoothAdapter.STATE_DISCONNECTED:
						break;
					/**
					 * 之前为正在断开连接,后续状态应该为未连接
					 */
					case BluetoothAdapter.STATE_DISCONNECTING:
						break;
					/**
					 * 连接建立中,后续状态为已连接(连接成功)或者未连接(连接失败)
					 */
					case BluetoothAdapter.STATE_CONNECTING:
						break;
				}
				break;
			/**
			 * 状态发生改变,需要通过之前的状态来判断场景
			 * EXTRA_STATE
			 * EXTRA_PREVIOUS_STATE
			 */
			case BluetoothAdapter.ACTION_STATE_CHANGED:
				Log.v(TAG, "onReceive BluetoothAdapter.ACTION_STATE_CHANGED");
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
				int previousState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, -1);
				switch (state) {
					case BluetoothAdapter.STATE_OFF:
						listener.onStateOFF();
						break;
					/**
					 * 状态为开启,可以建立连接
					 */
					case BluetoothAdapter.STATE_ON:
						listener.onStateOn();
						break;
					/**
					 * 如果已连接,请开始做中断连接的操作
					 */
					case BluetoothAdapter.STATE_TURNING_OFF:
						Log.i(TAG,"----本地的蓝牙正在关闭----");
						break;
					case BluetoothAdapter.STATE_TURNING_ON:
						break;
				}
				switch (previousState) {
					case BluetoothAdapter.STATE_OFF:
						break;
					/**
					 * 之前开启
					 */
					case BluetoothAdapter.STATE_ON:
						break;
					case BluetoothAdapter.STATE_TURNING_OFF:
						break;
					case BluetoothAdapter.STATE_TURNING_ON:
						break;
				}
				/**
				 * 请求开启蓝牙
				 * 需要startActivityForResult
				 */
			case BluetoothAdapter.ACTION_REQUEST_ENABLE:
				Log.v(TAG, "onReceive BluetoothAdapter.ACTION_REQUEST_ENABLE");
				listener.onRequestBluetoothEnable();
				break;
			case BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE:
				Log.i(TAG,"-----ACTION_REQUEST_DISCOVERABLE----");
				break;
			/**
			 * 本地蓝牙触发开始搜索的动作
			 * 开启搜索process,连接之前务必关闭搜索
			 */
			case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
				Log.v(TAG, "onReceive BluetoothAdapter.ACTION_DISCOVERY_STARTED");
				listener.onDiscoveryStarted();
				break;
			/**
			 * 本地蓝牙触发结束搜索的动作
			 * 结束搜索process
			 */
			case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
				Log.v(TAG, "onReceive BluetoothAdapter.ACTION_DISCOVERY_FINISHED");
				listener.onDiscoveryFinished();
				break;
			/**
			 * 本地设备更改名称
			 * EXTRA_LOCAL_NAME
			 */
			case BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED:
				Log.v(TAG, "onReceive BluetoothDevice.ACTION_LOCAL_NAME_CHANGED");
				String localName = intent.getStringExtra(BluetoothAdapter.EXTRA_LOCAL_NAME);
				listener.onLocalNameChanged(localName);
				break;
			/**
			 * 远端设备更改名称(与上一次扫描到的结果不一致)或者第一次获取
			 * EXTRA_DEVICE
			 * EXTRA_NAME
			 */
			case BluetoothDevice.ACTION_NAME_CHANGED:
				Log.v(TAG, "onReceive BluetoothDevice.ACTION_NAME_CHANGED");
				BluetoothDevice remoteDevice3 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				String remoteName1 = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
				listener.onRemoteNameChanged(remoteDevice3, remoteName1);
				break;
			/**
			 * 远端设备类型改变(与上一次扫描到的结果不一致)或者第一次获取
			 * EXTRA_DEVICE
			 * EXTRA_CLASS
			 */
			case BluetoothDevice.ACTION_CLASS_CHANGED:
				Log.v(TAG, "onReceive BluetoothDevice.ACTION_CLASS_CHANGED");
				BluetoothDevice remoteDevice2 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				BluetoothClass bluetoothClass1 = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
				listener.remoteClassChanged(remoteDevice2, bluetoothClass1);
				break;
			/**
			 * ACL相关暂不处理
			 */
			case BluetoothDevice.ACTION_ACL_CONNECTED:
				Log.v(TAG, "onReceive BluetoothDevice.ACTION_ACL_CONNECTED");
				break;
			case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
				Log.v(TAG, "onReceive BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED");
				break;
			case BluetoothDevice.ACTION_ACL_DISCONNECTED:
				listener.onActionAclDisConnected();
				Log.v(TAG, "onReceive BluetoothDevice.ACTION_ACL_DISCONNECTED");
				break;
			default:
				break;
		}
	}

	public BluetoothListener getListener() {
		return listener;
	}

	public void setListener(BluetoothListener listener) {
		this.listener = listener;
	}

	private IntentFilter getBluetoothIntentFilter() {
		IntentFilter intentFilter = new IntentFilter();
		//设备相关
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
		intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
		intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
		intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		intentFilter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
		//adapter相关
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		intentFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		intentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
		return intentFilter;
	}

	private void printIntent(String action, Bundle extras) {
		if (extras != null) {
			for (String key : extras.keySet()) {
				if (key != null) {
					Object o = extras.get(key);
					if (o instanceof Integer) {
						Log.v("Bundle Content", String.format("%-20s          %-30s          %-20s", action.substring(action.indexOf("action") + 7), key.substring(key.indexOf("extra") + 6), stateToString(((int) o))));
					} else {
						Log.v("Bundle Content", String.format("%-20s          %-30s          %-20s", action.substring(action.indexOf("action") + 7), key.substring(key.indexOf("extra") + 6), o));
					}
				}
			}
		}
	}

	public String stateToString(int value) {
		switch (value) {
			case BluetoothAdapter.STATE_CONNECTED:
				return "STATE_CONNECTED";
			case BluetoothAdapter.STATE_CONNECTING:
				return "STATE_CONNECTING";
			case BluetoothAdapter.STATE_DISCONNECTING:
				return "STATE_DISCONNECTING";
			case BluetoothAdapter.STATE_DISCONNECTED:
				return "STATE_DISCONNECTED";
			case BluetoothAdapter.STATE_TURNING_ON:
				return "STATE_TURNING_ON";
			case BluetoothAdapter.STATE_TURNING_OFF:
				return "STATE_TURNING_OFF";
			case BluetoothAdapter.STATE_ON:
				return "STATE_ON";
			case BluetoothAdapter.STATE_OFF:
				return "STATE_OFF";
			case BluetoothAdapter.SCAN_MODE_NONE:
				return "SCAN_MODE_NONE";
			case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
				return "SCAN_MODE_CONNECTABLE";
			case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
				return "SCAN_MODE_CONNECTABLE_DISCOVERABLE";
		}
		return "";
	}
}
