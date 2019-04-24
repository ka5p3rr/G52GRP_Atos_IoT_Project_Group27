package uk.ac.nottingham.group27atosproject.helperclasses;

import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import uk.ac.nottingham.group27atosproject.R;

public class NotificationManager {
  /** Unique int for each notification */
  private static final int NOTIFICATION_ID = 1;
  /** Channel ID. */
  private static final String CHANNEL_ID = "100";

  /** Private constructor to ensure that this class can't be instantiated. */
  private NotificationManager() {}

  /**
   * Creates a notification.
   *
   * @param notificationContent text content of the notification
   */
  static void createNotification(String notificationContent, @NonNull Context context) {
    android.app.NotificationManager notificationManager =
        (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = "General";
      String description = "Main notification channel of this app";
      int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
      channel.setDescription(description);
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      notificationManager.createNotificationChannel(channel);
    }

    // intent to be launched on notification click - currently creates an empty intent
    PendingIntent contentIntent =
        PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

    NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_warning_notification_icon)
            .setContentTitle("WARNING!")
            .setContentText(notificationContent)
            .setContentIntent(contentIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setColor(Color.RED)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000});

    notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
  }

  /** Removes notification. */
  public static void cancelNotification(@NonNull Context context) {
    android.app.NotificationManager notificationManager =
        (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.cancel(NOTIFICATION_ID);
  }
}
