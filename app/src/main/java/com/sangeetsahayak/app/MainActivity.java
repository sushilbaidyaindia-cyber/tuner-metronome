package com.sangeetsahayak.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            View tuner = findViewById(R.id.btnTuner);
            View metro = findViewById(R.id.btnMetro);
            if (tuner != null) {
                tuner.setOnClickListener(v -> openPage("tuner"));
            }
            if (metro != null) {
                metro.setOnClickListener(v -> openPage("metro"));
            }
        } catch (Exception e) {
            Toast.makeText(this, "UI error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void openPage(String page) {
        Intent i = new Intent(this, WebActivity.class);
        i.putExtra(WebActivity.EXTRA_PAGE, page);
        startActivity(i);
    }
}
