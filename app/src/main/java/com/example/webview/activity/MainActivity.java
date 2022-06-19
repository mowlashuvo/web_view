package com.example.webview.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.webview.utils.Configs;
import com.example.webview.R;
import com.google.firebase.messaging.FirebaseMessaging;

import im.delight.android.webview.AdvancedWebView;

public class MainActivity extends AppCompatActivity implements AdvancedWebView.Listener {

    private AdvancedWebView webView;
    private Configs configs;
    private ContentLoadingProgressBar loading;
    private SwipeRefreshLayout refresh;
    private String currentUrl;
    private Toolbar toolbar;
    private RelativeLayout connectionError;
    private ActionBar bar;
    private TextView errorText;
    private ProgressBar circleLoading;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configs = new Configs(this);

        FirebaseMessaging.getInstance().subscribeToTopic("webapp");

        toolbar = findViewById(R.id.appBar);
        if (configs.isFullscreen()) {
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
        }

        webView = findViewById(R.id.web);
        loading = findViewById(R.id.loading);
        circleLoading = findViewById(R.id.circular_loading);
        refresh = findViewById(R.id.refresh);

        connectionError = findViewById(R.id.connection);

        setupToolbar();

        refresh.setOnRefreshListener(() -> {
            webView.reload();
            refresh.setRefreshing(false);
        });

        loadWebPage("https://equipmentlagbe.com/");


    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebPage(String currentUrl) {
        webView.setListener(this, this);
        webView.setMixedContentAllowed(false);
        webView.loadUrl(currentUrl);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayUseLogoEnabled(false);
            bar.setDisplayShowTitleEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
            bar.setTitle("google");
        }


        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void showLoading() {
        if (configs.getLoadingStyle() == 0) {
            loading.setVisibility(View.VISIBLE);
        } else if (configs.getLoadingStyle() == 1) {
            circleLoading.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoading() {
        if (configs.getLoadingStyle() == 0) {
            loading.setVisibility(View.INVISIBLE);
        } else if (configs.getLoadingStyle() == 1) {
            circleLoading.setVisibility(View.INVISIBLE);
        }
    }

    private void showConnectionError() {
        webView.setVisibility(View.INVISIBLE);
        connectionError.setVisibility(View.VISIBLE);
    }

    private void hideConnectionError() {
        webView.setVisibility(View.VISIBLE);
        connectionError.setVisibility(View.GONE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void handleError() {
        //nada
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        if (!url.contains("file:///android_asset")) {
            if (isNetworkAvailable()) {
                showLoading();
                hideConnectionError();
                if (url != null) {
                    currentUrl = url;
                }
            } else {
                showConnectionError();
            }
        } else {
            hideConnectionError();
        }
    }

    @Override
    public void onPageFinished(String url) {
        webView.loadUrl("javascript:(function() { " +
                "var head = document.getElementsByTagName('header')[0];"
                + "head.parentNode.removeChild(head);" +
                "})()");
        hideLoading();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        handleError();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
//        showDownloadDialog(mimeType, suggestedFilename, url);
    }

    @Override
    public void onExternalPageRequest(String url) {
        //nada
    }


    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webView.onActivityResult(requestCode, resultCode, intent);
    }


}