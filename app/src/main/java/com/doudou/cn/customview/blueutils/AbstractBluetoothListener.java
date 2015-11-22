package com.doudou.cn.customview.blueutils;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

/**
 * @author by liuhongjian on 15/8/5 21:14.
 */
public abstract class AbstractBluetoothListener implements BluetoothListener {
	@Override
	public void onLocalNameChanged(String localName) {
	}

	@Override
	public void onRemoteNameChanged(BluetoothDevice remoteDevice, String remoteName) {
	}

	@Override
	public void remoteClassChanged(BluetoothDevice remoteDevice, BluetoothClass bluetoothClass) {
	}
}
