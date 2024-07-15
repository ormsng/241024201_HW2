package com.example.logviewer;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.Spanned;

import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {
    private List<LogEntry> logs;

    public LogAdapter(List<LogEntry> logs) {
        this.logs = logs;
    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, int position) {
        LogEntry log = logs.get(position);

        // Format the log entry as requested
        String formattedLog = String.format("%s",
                log.getMessage());

        Spanned coloredLog = Html.fromHtml(formattedLog, Html.FROM_HTML_MODE_LEGACY);
        holder.logTextView.setText(coloredLog);

        // Set color based on log type
        int color;
        switch (log.getType()) {
            case DEBUG:
                color = Color.BLUE;
                break;
            case INFO:
                color = Color.GREEN;
                break;
            case WARNING:
                color = Color.YELLOW;
                break;
            case ERROR:
                color = Color.RED;
                break;
            default:
                color = Color.BLACK;
                break;
        }
        holder.logTextView.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {
        public TextView logTextView;

        public LogViewHolder(View itemView) {
            super(itemView);
            logTextView = itemView.findViewById(R.id.logTextView);
        }
    }
}