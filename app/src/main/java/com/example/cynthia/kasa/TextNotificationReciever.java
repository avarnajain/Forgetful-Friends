package com.example.cynthia.kasa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TextNotificationReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("It works!");
    }
}
