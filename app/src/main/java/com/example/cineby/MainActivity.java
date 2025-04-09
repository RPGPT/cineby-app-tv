package com.example.cineby;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.FragmentActivity;

import java.util.Objects;

public class MainActivity extends FragmentActivity {

    private WebView myWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebView = findViewById(R.id.webview);

        // Enable debugging with Chrome DevTools
        WebView.setWebContentsDebuggingEnabled(true);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // Ensure the WebView handles its own navigation
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                return !Objects.requireNonNull(Uri.parse(url).getHost()).endsWith("cineby.app");
            }
        });

        // Set focus to the WebView to handle D-pad navigation
        myWebView.requestFocus();

        // Load the desired URL
        myWebView.loadUrl("https://cineby.app");

        // Handle D-pad navigation within the WebView
        myWebView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        // Allow WebView to handle vertical navigation
                        return false;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        // Simulate swipe left for swiper-slide navigation
                        myWebView.evaluateJavascript(
                                "document.querySelector('.swiper-button-next').click();",
                                null
                        );
                        return true; // Indicate we handled this key event
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        // Simulate swipe right for swiper-slide navigation
                        myWebView.evaluateJavascript(
                                "document.querySelector('.swiper-button-prev').click();",
                                null
                        );
                        return true; // Indicate we handled this key event
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                        // Handle the Enter key (select button)
                        return false; // Let WebView handle the click as normal
                    default:
                        break;
                }
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
