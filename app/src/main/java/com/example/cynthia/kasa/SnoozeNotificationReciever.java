package com.example.cynthia.kasa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SnoozeNotificationReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Activity2.finalMin += 10;
    }
}
