package dev.mrz3t4.literatureclub.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import dev.mrz3t4.literatureclub.R;

public class NotificationsBuilder {

    private NotificationManagerCompat notificationManager = NotificationManagerCompat.from(GenericContext.getContext());

    public void createNotification(String title, String text, int count){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(GenericContext.getContext(), "DIRECTORY")
                .setContentTitle(title)
                .setContentText(text + count)
                .setSmallIcon(R.drawable.force_reload_ic)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("DIRECTORY", "RELOAD", importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = GenericContext.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(GenericContext.getContext());

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }

    public void cancelNotification(int id){
        notificationManager.cancel(id);
    }

    public void createToast(String message, int duration){
        Toast.makeText(GenericContext.getContext(), message, duration).show();
    }


}
