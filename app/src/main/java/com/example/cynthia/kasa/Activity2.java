package com.example.cynthia.kasa;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity2 extends AppCompatActivity {

    private Button editButton;
    private static final int notificationId = 12345;
    private static String CHANNEL_ID = "channel1";
    public static int finalMin;

    public static int getFinalMin() {
        return finalMin;
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

            finalMin = (MainActivity.getTotalHours() * 60) + MainActivity.getTotalMin();
            System.out.println(finalMin);
            if (MainActivity.getCurrentLocation() == null /**&& MainActivity.getHome() != null && MainActivity.getCurrentMin() == finalMin && MainActivity.getCurrentLocation().latitude != MainActivity.getHome().latitude && MainActivity.getCurrentLocation().longitude != MainActivity.getHome().longitude */) {
                createNotificationChannel();

                Intent intent = new Intent(this, Activity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                Intent snoozeIntent = new Intent(this, SnoozeNotificationReciever.class);
                snoozeIntent.setAction("com.example.kasa.snooze");
                snoozeIntent.putExtra("snooze", 0);
                PendingIntent snoozePendingIntent =
                        PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

                Intent textIntent = new Intent(this, TextNotificationReciever.class);
                textIntent.setAction("com.example.kasa.text");
                textIntent.putExtra("text", 0);
                PendingIntent textPendingIntent =
                        PendingIntent.getBroadcast(this, 0, textIntent, 0);

                Intent callIntent = new Intent(this, CallNotificationReciever.class);
                callIntent.setAction("com.example.kasa.call");
                callIntent.putExtra("call", 0);
                PendingIntent callPendingIntent =
                        PendingIntent.getBroadcast(this, 0, callIntent, 0);




                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Its " + MainActivity.getTime() + "!")
                        .setContentText("Open the app to dismiss timer.")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Open the app to dismiss timer. Pressing text will send a message for help to your contact with your location attached."))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .addAction(R.drawable.common_google_signin_btn_icon_light_normal, getString(R.string.SNOOZE),
                                snoozePendingIntent)
                        .addAction(R.drawable.common_google_signin_btn_icon_light_normal, getString(R.string.TEXT),
                                textPendingIntent)
                        .addAction(R.drawable.common_google_signin_btn_icon_light_normal, getString(R.string.CALL),
                                callPendingIntent);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(notificationId, mBuilder.build());

            }
    }
    public void openMainActivity() {
        Intent intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
    }
}
