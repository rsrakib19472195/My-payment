package com.transactionreader.app;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.app.Notification;
import android.os.Bundle;

public class PaymentNotificationListener extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Bundle extras = sbn.getNotification().extras;
        CharSequence title = extras.getCharSequence(Notification.EXTRA_TITLE);
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);

        String content = "App: " + sbn.getPackageName()
                + "\nTitle: " + (title == null ? "" : title)
                + "\nMessage: " + (text == null ? "" : text);

        getSharedPreferences("reader", MODE_PRIVATE)
                .edit().putString("latest", content).apply();
    }
}
