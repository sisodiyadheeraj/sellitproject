package app.reatailx.sellitapp.service;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.legacy.content.WakefulBroadcastReceiver;

import app.reatailx.sellitapp.R;
import app.reatailx.sellitapp.util.NotificationUtils;

/**
 * Created by dheeraj on 6/26/2018.
 */

public class NotificationReceiver {/* extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            if (!NotificationUtils.isAppIsInBackground(context)) {
                playNotificationSound(context);
                // there are app in foreground
                System.out.println("Show_notif_plus_mainactivity");
            } else if (NotificationUtils.isAppIsInBackground(context)) {
                playNotificationSound(context);
                // there are app in background
                System.out.println("Show_notif_in_background");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playNotificationSound(Context context) {
        try {
            Uri notification = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.highsound);
            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/
}