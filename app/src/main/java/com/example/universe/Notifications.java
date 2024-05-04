package com.example.universe;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class Notifications {

    private static final String TAG = "Notifications";
    private static final String CHANNEL_ID_1 = "default";
    private static final String CHANNEL_NAME_1 = "Default Channel";

    public static void statusBar(View view, String userId) {
        Context context = view.getContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Retrieve device token for the user who uploaded the post
        FirebaseFirestore.getInstance().collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String deviceToken = documentSnapshot.getString("deviceToken");
                        if (deviceToken != null && !deviceToken.isEmpty()) {
                            // Configure notification using builder
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_1)
                                    .setContentTitle("New comment!")
                                    .setContentText("One of your posts has received a new comment")
                                    .setSmallIcon(R.drawable.logo_mobile_removebg);

                            // Create a notification channel for devices running Android Oreo and higher
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel channel = new NotificationChannel(CHANNEL_ID_1, CHANNEL_NAME_1, NotificationManager.IMPORTANCE_DEFAULT);
                                notificationManager.createNotificationChannel(channel);
                            }

                            // Show the notification
                            int notificationId = 0;
                            notificationManager.notify(notificationId, builder.build());
                        } else {
                            Log.e(TAG, "Device Token not found for userId: " + userId);
                        }
                    } else {
                        Log.e(TAG, "User document not found for userId: " + userId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user document: " + e.getMessage());
                });
    }
}
