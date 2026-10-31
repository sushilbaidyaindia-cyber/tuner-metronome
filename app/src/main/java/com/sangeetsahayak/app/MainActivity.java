package com.sangeetsahayak.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Native home only — no WebView here.
 * Opens WebActivity with tuner or metronome page.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnTuner).setOnClickListener(v -> openPage("tuner"));
        findViewById(R.id.btnMetro).setOnClickListener(v -> openPage("metro"));
    }

    private void openPage(String page) {
        Intent i = new Intent(this, WebActivity.class);
        i.putExtra(WebActivity.EXTRA_PAGE, page);
        startActivity(i);
    }
}
