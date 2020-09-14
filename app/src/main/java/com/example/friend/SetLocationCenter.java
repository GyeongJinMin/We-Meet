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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.friend.databinding.ActivitySetLocationCenterBinding;

import java.util.concurrent.ExecutionException;


public class SetLocationCenter extends AppCompatActivity {
    private WebView mWebView;
    private ActivitySetLocationCenterBinding activitySetLocationCenterBinding;
    private WebSettings mWebSettings;
    private Handler handler;
    private Button btn_calc, btn_pick;
    private double mapPointx = 0, mapPointy = 0;
    private String location;
    private String sche_id;

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==2)
            location = data.getStringExtra("Location");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySetLocationCenterBinding = ActivitySetLocationCenterBinding.inflate(getLayoutInflater());
        setContentView(activitySetLocationCenterBinding.getRoot());

        Intent getSchdule = getIntent();
        sche_id = getSchdule.getStringExtra("sche_id");

        init_webView();
        handler = new Handler();

        activitySetLocationCenterBinding.calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnMain = new Intent();
                Intent intent = new Intent(getApplicationContext(), CalcLocation.class);
                intent.putExtra("mapPointx", mapPointx);
                intent.putExtra("mapPointy", mapPointy);
                intent.putExtra("sche_id",sche_id);
                intent.putExtra("location",location);
                startActivityForResult(intent,0);
                returnMain.putExtra("Location",location);
                setResult(2, returnMain);


                try {
                    String result = new CustomTask().execute(sche_id, location, "setLocation").get();
                    String res_vote = new CustomTask().execute(sche_id, location, "setVoteLocation").get();
                    String init = new CustomTask().execute(sche_id,"initVoteLocation").get();


//                    if(result.equals("done"))
//                        Toast.makeText(SetLocationCenter.this,"success",Toast.LENGTH_SHORT).show();
//                    else
//                        Toast.makeText(SetLocationCenter.this,"fail",Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



                //startActivity(intent);
                finish();
            }
        });

        activitySetLocationCenterBinding.pickLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void init_webView() {
        mWebView = (WebView)findViewById(R.id.daum_webView2);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.addJavascriptInterface(new AndroidBridge(), "sendMessage");
        //mWebView.loadUrl("http://192.168.0.7:8080/project_Server/kakaomap.jsp");
        //mWebView.loadUrl("http://172.30.1.29:8080/server/kakaomap.jsp");
        mWebView.loadUrl("http://192.168.200.138:8080/server/kakaomap.jsp"); // 수연
    }

    public class AndroidBridge {
        @JavascriptInterface
        public void sendPoint(final String latitude, final String longitude, final String placename) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(),"통신 성공", Toast.LENGTH_SHORT).show();
                    mapPointx = Double.parseDouble(latitude);
                    mapPointy = Double.parseDouble(longitude);
                    location = placename;
                    Toast.makeText(getApplicationContext(),
                            mapPointx + ", " + mapPointy + "," + location,  Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}