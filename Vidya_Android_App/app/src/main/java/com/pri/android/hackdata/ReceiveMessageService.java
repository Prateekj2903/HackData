package com.pri.android.hackdata;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class ReceiveMessageService extends FirebaseMessagingService {

    private LocalBroadcastManager broadcaster;

    public ReceiveMessageService() {
    }

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        Intent intent = new Intent("Answer");
        intent.putExtra("type", data.get("type"));
        intent.putExtra("ans", data.get("ans"));
        broadcaster.sendBroadcast(intent);
    }
}

