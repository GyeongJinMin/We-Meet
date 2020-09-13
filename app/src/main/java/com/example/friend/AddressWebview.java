package com.example.friend;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

public class AddressWebview extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings mWebSettings;
    private Handler handler;
    private TextView address;
    private Button addressOK;
    private String juso;
    private double mLat;
    private double mLng;
    private String s_lat;
    private String s_lng;
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


                //주소를 위도 경도로 바꾸는 코드
                List<Address> list = null;
                String str = address.getText().toString();
                try {
                    list = geocoder.getFromLocationName(
                            str, // 지역 이름
                            10); // 읽을 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                }

                if (list != null) {
                    if (list.size() == 0) {
                        address.setText("해당되는 주소 정보는 없습니다");
                    } else {
                        juso = list.get(0).toString();
                        mLat = list.get(0).getLatitude();
                        mLng = list.get(0).getLongitude();
                        s_lat = Double.toString(mLat);
                        s_lng = Double.toString(mLng);

                        System.out.println(mLat);
                        //          list.get(0).getCountryName();  // 국가명
                        //          list.get(0).getLatitude();        // 위도
                        //          list.get(0).getLongitude();    // 경도
                    }
                }


                intentback.putExtra("lat", s_lat);
                intentback.putExtra("lng", s_lng);

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
        //mWebView.loadUrl("http://172.30.1.29:8080/project_Server/address.jsp");
        mWebView.loadUrl("http://192.168.0.4:8080/server/address.jsp"); // 수연


    }
    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    address.setText(String.format("%s", arg1));

                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                }
            });
        }
    }
}