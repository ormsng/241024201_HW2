package com.example.logviewer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LogAdapter adapter;
    private List<LogEntry> logs = new ArrayList<>();
    private List<LogEntry> filteredLogs = new ArrayList<>();
    private LogType currentFilter = null;

    private BroadcastReceiver logUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (LogService.ACTION_LOG_UPDATED.equals(intent.getAction())) {
                LogEntry logEntry = (LogEntry) intent.getSerializableExtra(LogService.EXTRA_LOG);
                logs.add(logEntry);
                filterLogs();
                Log.d("MainActivity", "Log entry added: " + logEntry.getMessage());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new LogAdapter(filteredLogs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button allButton = findViewById(R.id.allButton);
        Button debugButton = findViewById(R.id.debugButton);
        Button infoButton = findViewById(R.id.infoButton);
        Button errorButton = findViewById(R.id.errorButton);

        allButton.setOnClickListener(v -> {
            currentFilter = null;
            filterLogs();
        });

        debugButton.setOnClickListener(v -> {
            currentFilter = LogType.DEBUG;
            filterLogs();
        });

        infoButton.setOnClickListener(v -> {
            currentFilter = LogType.INFO;
            filterLogs();
        });

        errorButton.setOnClickListener(v -> {
            currentFilter = LogType.ERROR;
            filterLogs();
        });

        // Register the local broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(logUpdateReceiver, new IntentFilter(LogService.ACTION_LOG_UPDATED));

        // Start the LogService
        Intent intent = new Intent(this, LogService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the local broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(logUpdateReceiver);

        // Stop the LogService when the activity is destroyed
        Intent intent = new Intent(this, LogService.class);
        stopService(intent);
    }

    private void filterLogs() {
        filteredLogs.clear();
        if (currentFilter == null) {
            filteredLogs.addAll(logs);
        } else {
            for (LogEntry log : logs) {
                if (log.getType() == currentFilter) {
                    filteredLogs.add(log);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
