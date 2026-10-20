package com.transactionreader.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView accessStatus, resultText;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accessStatus = findViewById(R.id.accessStatus);
        resultText = findViewById(R.id.resultText);
        Button accessButton = findViewById(R.id.accessButton);
        Button refreshButton = findViewById(R.id.refreshButton);
        accessButton.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)));
        refreshButton.setOnClickListener(v -> refresh());
    }
    @Override protected void onResume() { super.onResume(); refresh(); }
    private void refresh() {
        boolean enabled = false;
        String enabledListeners = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(enabledListeners)) enabled = enabledListeners.contains(getPackageName());
        accessStatus.setText("Notification access: " + (enabled ? "ENABLED" : "NOT ENABLED"));
        String saved = getSharedPreferences("reader", MODE_PRIVATE).getString("latest", null);
        resultText.setText(saved == null ? "No matching notification received yet. Make sure notifications are enabled for your payment app." : saved);
    }
}
