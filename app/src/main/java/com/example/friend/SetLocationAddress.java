package com.example.friend;

import android.content.Intent;
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

public class SetLocationAddress extends AppCompatActivity {
    private Handler handler;
    private WebView webView;
    private TextView result;
    private WebSettings webSettings;
    private Button okButton;
    private String pickAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location_address);
        result = (TextView) findViewById(R.id.locationPickResult);
        okButton = (Button) findViewById(R.id.locationPickButton);
        okButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intentback = new Intent();
                pickAddress = result.getText().toString();
                System.out.println(pickAddress);
                intentback.putExtra("pickAddress", pickAddress);

                //사용자에게 입력받은값 넣기
                setResult(RESULT_OK,intentback); //결과를 저장
                finish();
            }
        });

        //웹뷰 초기화
        init_webView();
        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }



    public void init_webView() {
        // WebView 설정
        webView = (WebView)findViewById(R.id.locationPickWebView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new SetLocationAddress.AndroidBridge(), "TestApp");


        webView.loadUrl("http://172.30.1.18:8080/server/address.jsp");
    }


    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    result.setText(String.format("%s", arg1));

                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                }
            });
        }
    }

}