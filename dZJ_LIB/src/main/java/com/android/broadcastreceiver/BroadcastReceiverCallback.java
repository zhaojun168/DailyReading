package com.android.broadcastreceiver;

import android.content.Context;
import android.content.Intent;

public interface BroadcastReceiverCallback {

	public void receiver(Context context, Intent intent);
}
