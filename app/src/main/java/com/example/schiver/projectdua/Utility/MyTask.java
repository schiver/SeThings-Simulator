package com.example.schiver.projectdua.Utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;

public class MyTask extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer mp = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        mp.start();
    }
}
