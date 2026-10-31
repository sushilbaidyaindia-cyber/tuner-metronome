package com.sangeetsahayak.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccess(true);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/index.html");
    }

    /**
     * Back: if on Tuner/Metro page → go Home and stop sound.
     * If already on Home → exit app.
     */
    @Override
    public void onBackPressed() {
        if (webView == null) {
            super.onBackPressed();
            return;
        }
        webView.evaluateJavascript(
                "(function(){try{"
                        + "if(typeof leaveAll==='function')leaveAll();"
                        + "var t=document.getElementById('pgTuner');"
                        + "var m=document.getElementById('pgMetro');"
                        + "var home=document.getElementById('pgHome');"
                        + "if(t&&t.classList.contains('show')){"
                        + "  if(typeof show==='function')show('pgHome');"
                        + "  return true;"
                        + "}"
                        + "if(m&&m.classList.contains('show')){"
                        + "  if(typeof show==='function')show('pgHome');"
                        + "  return true;"
                        + "}"
                        + "return false;"
                        + "}catch(e){return false;}})()",
                value -> {
                    if ("true".equals(value)) {
                        return; // stayed in app, now on home, audio stopped
                    }
                    MainActivity.super.onBackPressed();
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop tick/tone when leaving app screen
        if (webView != null) {
            webView.evaluateJavascript(
                    "(function(){try{if(typeof leaveAll==='function')leaveAll();}catch(e){}})();",
                    null);
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.evaluateJavascript(
                    "(function(){try{if(typeof leaveAll==='function')leaveAll();}catch(e){}})();",
                    null);
            webView.loadUrl("about:blank");
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
