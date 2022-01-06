package com.example.kr2rpois;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;


public class AlertReceiver extends BroadcastReceiver {

    private List<DataBD> dataBDList = new ArrayList<>();


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.
                getChannelNotification((String) intent.getSerializableExtra("Heading"),
                        (String) intent.getSerializableExtra("Text"));

        notificationHelper.getManager().notify((Integer) intent.getSerializableExtra("ID"), nb.build());
    }
}