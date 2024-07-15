// File: MainActivity.java
package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.orslogger.OrsLogger;

public class MainActivity extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OrsLogger.init(this);

        editText = findViewById(R.id.editText);

        findViewById(R.id.btnDebug).setOnClickListener(v -> log(OrsLogger::d));
        findViewById(R.id.btnInfo).setOnClickListener(v -> log(OrsLogger::i));
        findViewById(R.id.btnError).setOnClickListener(v -> log((tag, message) -> OrsLogger.e(tag, message, null)));
        findViewById(R.id.btnWarning).setOnClickListener(v -> log(OrsLogger::w));
        findViewById(R.id.btnVerbose).setOnClickListener(v -> log(OrsLogger::v));
    }

    private void log(LogFunction logFunction) {
        String message = editText.getText().toString();
        logFunction.log("LoggerDemo", message);
    }

    @FunctionalInterface
    interface LogFunction {
        void log(String tag, String message);
    }
}
