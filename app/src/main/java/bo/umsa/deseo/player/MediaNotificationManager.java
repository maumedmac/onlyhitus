package bo.umsa.deseo.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import bo.umsa.deseo.R;

import bo.umsa.deseo.MainActivity;

public class MediaNotificationManager extends MainActivity {

    public static final int NOTIFICATION_ID = 1;

    private final RadioService service;

    private final String appName;

    private final Resources resources;

    private final NotificationManagerCompat notificationManager;

    public MediaNotificationManager(RadioService service) {

        this.service = service;
        this.resources = service.getResources();

        appName = resources.getString(R.string.app_name);

        notificationManager = NotificationManagerCompat.from(service);
    }

    public void startNotify(String playbackStatus) {

        Bitmap largeIcon = BitmapFactory.decodeResource(resources, service.Shoutcast.getImage());

        int icon = R.drawable.exo_icon_stop;
        Intent notificationAction = new Intent(service, RadioService.class);
        notificationAction.setAction(RadioService.ACTION_STOP);
        PendingIntent action = PendingIntent.getService(service, 1, notificationAction, 0);

        if (playbackStatus.equals(PlaybackStatus.PAUSED)) {
            icon = R.drawable.exo_icon_play;
            notificationAction.setAction(RadioService.ACTION_PLAY);
            action = PendingIntent.getService(service, 2, notificationAction, 0);
        }

        Intent intent = new Intent(service, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_MUSIC);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(service, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationManager.cancel(NOTIFICATION_ID);

        String PRIMARY_CHANNEL = "PRIMARY_CHANNEL_ID";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL, appName, NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(service, PRIMARY_CHANNEL)
                .setAutoCancel(false)
                .setContentTitle("Radio Deseo")
                .setContentText("Programacion")
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.radiod)
                .addAction(icon, "pause", action)

                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis())
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(service.getMediaSession().getSessionToken())
                        .setShowActionsInCompactView(0)
                        .setShowCancelButton(false)
                        .setCancelButtonIntent(action));

        service.startForeground(NOTIFICATION_ID, builder.build());
        Log.d("startNotify", playbackStatus + " ");
    }

    public void cancelNotify() {

        service.stopForeground(true);
    }
}

