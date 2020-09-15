package com.example.friend;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddressWebview extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings mWebSettings;
    private Handler handler;
    private TextView address;
    private Button addressOK;
    private String juso;
    private String lng;
    private String lat;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_webview);
        address = findViewById(R.id.daum_result);
        addressOK = findViewById(R.id.addressOK);
        init_webView();
        handler = new Handler();

        final Geocoder geocoder = new Geocoder(this);

        addressOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentback = new Intent();
                juso = address.getText().toString();
                System.out.println(juso);
                intentback.putExtra("lat", lat);
                intentback.putExtra("lng", lng);

                //사용자에게 입력받은값 넣기
                setResult(RESULT_OK,intentback); //결과를 저장
                finish();
            }
        });
    }

    public void init_webView() {
        mWebView = (WebView)findViewById(R.id.locationWebView);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.addJavascriptInterface(new AndroidBridge(), "TestApp");


        //mWebView.loadUrl("http://172.30.1.18:8080/server/address.jsp");
        //mWebView.loadUrl("http://192.168.0.7:8080/project_Server/address.jsp");
        mWebView.loadUrl("http://172.30.1.18:8080/server/address.jsp");


    }
    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    address.setText(String.format("%s", arg1));
                    lat = String.format("%s", arg2);
                    lng = String.format("%s", arg3);
                    System.out.print(lat);
                    System.out.println(lng);
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                }
            });
        }
    }
}