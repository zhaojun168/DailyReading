package com.android.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {

	private BroadcastReceiverCallback callback;

	public MyBroadcastReceiver(BroadcastReceiverCallback callback) {
		this.callback = callback;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (callback!=null) {
			callback.receiver(context, intent);
		}
	}
}
