package com.fimomsn;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

/**
 * Created by Diep_Chelsea on 25/08/2016.
 */
public class RebootReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
//                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                int interval = 60000;
//                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

            }
    }
}
