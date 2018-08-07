package com.collegeapp.collegeapp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        SharedPreferences prefs = getSharedPreferences("login",MODE_PRIVATE);
        int f = prefs.getInt("persistent",0);
        if (f == 0) {
            prefs.edit().putInt("persistent", 1).apply();
            Toast.makeText(this, "value of pesistent is changed to 1", Toast.LENGTH_SHORT).show();
        }
    }
}
