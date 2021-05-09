package dev.mrz3t4.literatureclub.Utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import dev.mrz3t4.literatureclub.R;

public class NotificationsBuilder {

    private NotificationManagerCompat notificationManager = NotificationManagerCompat.from(GenericContext.getContext());
    private NotificationCompat.Builder builder;

    public void createNotification(String title, String text, int importancy, String channelId, String name, int id){

        builder = new NotificationCompat.Builder(GenericContext.getContext(), channelId)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, name, importancy);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = GenericContext.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(GenericContext.getContext());

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());
    }

    public void cancelNotification(int id){
        notificationManager.cancel(id);
    }

    public void createToast(String message, int duration){
        Toast.makeText(GenericContext.getContext(), message, duration).show();
    }


}
