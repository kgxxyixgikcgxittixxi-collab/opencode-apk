package com.opencode.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.webkit.HttpAuthHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "https://opencode-proxy.idkyoohdtsu.workers.dev";
    private static final String USER = "opencode";
    private static final String PASS = "opencode2026";

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            final String msg = "Lỗi: " + throwable + "\n\n" + Log.getStackTraceString(throwable);
            runOnUiThread(() -> {
                try {
                    if (webView != null) {
                        webView.loadDataWithBaseURL(null,
                                "<html><body style='background:#0f1117;color:#eee;font-family:monospace;padding:16px'><h3>Ứng dụng gặp lỗi</h3><pre style='white-space:pre-wrap'>" + escapeHtml(msg) + "</pre></body></html>",
                                "text/html", "utf-8", null);
                    } else {
                        TextView tv = new TextView(this);
                        tv.setText(msg);
                        tv.setTextColor(0xFFFFFFFF);
                        tv.setGravity(Gravity.CENTER);
                        setContentView(tv);
                    }
                } catch (Throwable ignored) {
                }
            });
        });
        try {
            webView = new WebView(this);
        } catch (Throwable t) {
            TextView tv = new TextView(this);
            tv.setText("Không thể tạo WebView: " + t + "\nĐiện thoại của bạn không có WebView!");
            tv.setTextColor(0xFFFFFFFF);
            tv.setGravity(Gravity.CENTER);
            setContentView(tv);
            return;
        }
        SwipeRefreshLayout swipe = new SwipeRefreshLayout(this);
        swipe.addView(webView);
        setContentView(swipe);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setUserAgentString(settings.getUserAgentString() + " OpenCodeAPK/1.0");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                handler.proceed(USER, PASS);
            }
        });

        swipe.setOnRefreshListener(() -> {
            webView.reload();
            swipe.setRefreshing(false);
        });

        webView.loadUrl(URL);
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}