package com.transactionreader.app;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionNotificationListener extends NotificationListenerService {
    private static final Pattern TRX = Pattern.compile("(?i)(?:trx\\s*id|transaction\\s*id|txn\\s*id|tr\\s*id)\\s*[:#-]?\\s*([A-Z0-9-]{5,})");
    private static final Pattern AMOUNT = Pattern.compile("(?i)(?:amount|received|payment|cash\\s*in|deposit)\\s*[:=]?\\s*(?:BDT|Tk\\.?|৳)?\\s*([0-9,]+(?:\\.[0-9]{1,2})?)|(?:BDT|Tk\\.?|৳)\\s*([0-9,]+(?:\\.[0-9]{1,2})?)");
    private static final Pattern PHONE = Pattern.compile("(?<!\\d)(01[3-9]\\d{8})(?!\\d)");

    @Override public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn == null || sbn.getNotification() == null) return;
        String pkg = sbn.getPackageName() == null ? "" : sbn.getPackageName();
        Bundle extras = sbn.getNotification().extras;
        CharSequence titleCs = extras == null ? "" : extras.getCharSequence(Notification.EXTRA_TITLE, "");
        CharSequence textCs = extras == null ? "" : extras.getCharSequence(Notification.EXTRA_TEXT, "");
        CharSequence bigCs = extras == null ? "" : extras.getCharSequence(Notification.EXTRA_BIG_TEXT, "");
        String title = titleCs == null ? "" : titleCs.toString();
        String body = textCs == null ? "" : textCs.toString();
        String big = bigCs == null ? "" : bigCs.toString();
        String all = (title + "\n" + body + "\n" + big).trim();
        String lower = (pkg + " " + all).toLowerCase(Locale.ROOT);
        if (!(lower.contains("bkash") || lower.contains("b-kash") || lower.contains("nagad"))) return;
        String trx = find(TRX, all, 1);
        String amount = find(AMOUNT, all, 1);
        if (amount.isEmpty()) amount = find(AMOUNT, all, 2);
        String phone = find(PHONE, all, 1);
        String received = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date(sbn.getPostTime()));
        String result = "Gateway: " + (lower.contains("nagad") ? "Nagad" : "bKash / detected by text") +
                "\nTransaction ID: " + emptyAsUnknown(trx) +
                "\nAmount: " + emptyAsUnknown(amount) +
                "\nSender number: " + emptyAsUnknown(phone) +
                "\nNotification time: " + received +
                "\n\nRaw notification (for parser testing):\n" + all;
        getSharedPreferences("reader", MODE_PRIVATE).edit().putString("latest", result).apply();
    }
    private String find(Pattern pattern, String input, int group) {
        Matcher m = pattern.matcher(input);
        if (!m.find()) return "";
        String value = m.group(group);
        return value == null ? "" : value;
    }
    private String emptyAsUnknown(String value) { return value == null || value.trim().isEmpty() ? "Not found in notification" : value.trim(); }
}
