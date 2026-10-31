package com.sangeetsahayak.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Loads assets/index.html#tuner or #metro.
 * System Back or HTML "Home" → stop audio and finish (return to native MainActivity).
 */
public class WebActivity extends AppCompatActivity {

    public static final String EXTRA_PAGE = "page";

    private WebView webView;

    public class Bridge {
        @JavascriptInterface
        public void goHome() {
            runOnUiThread(() -> {
                stopJsAudio();
                finish();
            });
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
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
        webView.addJavascriptInterface(new Bridge(), "Android");

        String page = getIntent().getStringExtra(EXTRA_PAGE);
        if (page == null || page.isEmpty()) page = "metro";
        // hash opens the right section inside HTML
        webView.loadUrl("file:///android_asset/index.html#" + page);
    }

    private void stopJsAudio() {
        if (webView == null) return;
        webView.evaluateJavascript(
                "(function(){try{if(typeof leaveAll==='function')leaveAll();}catch(e){}})();",
                null);
    }

    @Override
    public void onBackPressed() {
        stopJsAudio();
        finish(); // back to native home
    }

    @Override
    protected void onPause() {
        stopJsAudio();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopJsAudio();
        if (webView != null) {
            webView.removeJavascriptInterface("Android");
            webView.loadUrl("about:blank");
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
