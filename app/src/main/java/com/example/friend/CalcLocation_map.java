package com.example.friend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CalcLocation_map extends AppCompatActivity {
    private WebView mWebView;
    private WebSettings mWebSettings;
    private ImageButton imageButton;
    private double mapPointx, mapPointy;
    private Handler handler;
    private String sendMsg, category;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_map);
        mWebView = (WebView)findViewById(R.id.daum_webView2);
        imageButton = (ImageButton)findViewById(R.id.imageButton);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        mapPointx = intent.getDoubleExtra("mapPointx",0);
        mapPointy = intent.getDoubleExtra("mapPointy", 0);

        init_webView();
        handler = new Handler();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void init_webView() {
        mWebView = (WebView)findViewById(R.id.daum_webView2);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //mWebView.addJavascriptInterface(new AndroidBridge(), "sendMessage");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String keyword = Double.toString(mapPointx);
                String keyword2 = Double.toString(mapPointy);
                String keyword3 = category.toString();

                String script = "javascript:function afterLoad() {" +
                        "document.getElementById('mapPointx').value = '" + keyword + "';"
                        + "};"
                        + "change('" + keyword + "','" + keyword2 + "','" + keyword3 + "');"
                        //,'" + keyword2 + "','" + keyword3 + "'
                        + "afterLoad();";
                view.loadUrl(script);
            }
        });
        mWebView.loadUrl("http://192.168.0.7:8080/project_Server/kakaomap_rec.jsp");
        //mWebView.loadUrl("http://192.168.123.105:8080/server/kakaomap_rec.jsp");
        //mWebView.loadUrl("http://172.30.1.7:8080/server/kakaomap_rec.jsp"); //수연

    }


}