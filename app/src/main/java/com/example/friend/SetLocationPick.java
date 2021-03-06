package com.example.friend;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.friend.databinding.ActivitySetLocationPickBinding;

import java.util.concurrent.ExecutionException;

public class SetLocationPick extends AppCompatActivity {
    public static final int REQUEST_CODE = 100;
    private static final String ENTRY_URL = "file:///android_asset/www/index.html";
    private ActivitySetLocationPickBinding activitySetLocationPickBinding;
    private String location;
    private String sche_id, saveDB,sendMsg;
    private TextView locationString;
    private Handler handler;
    private WebView webView;
    private String lat;
    private String lng;
    private double doubleLat;
    private double doubleLng;
    private String name;
    private Button foodButton;
    private Button cafeButton;
    private Button placeButton;
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode==0)
//            location = data.getStringExtra("Location");
//
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySetLocationPickBinding = ActivitySetLocationPickBinding.inflate(getLayoutInflater());
        setContentView(activitySetLocationPickBinding.getRoot());
        Intent getSchdule = getIntent();
        sche_id = getSchdule.getStringExtra("sche_id");
        locationString = (TextView) findViewById(R.id.locationText);
        locationString.setPaintFlags(locationString.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        activitySetLocationPickBinding.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SetLocationPick.this);
                builder.setTitle("장소 선택").setMessage("이 곳으로 하시겠습니까?");

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("Location",location);
                        Log.i("location" ,"calc" + location);
                        setResult(4, intent);

                        try {
                            String result = new CustomTask().execute(sche_id, location, "setLocation").get();
                            String res_vote = new CustomTask().execute(sche_id, location, "setVoteLocation").get();
                            String init = new CustomTask().execute(sche_id,"initVoteLocation").get();
                            sendMsg ="savePoint";
                            saveDB = new CustomTask(sendMsg).execute(sche_id, Double.toString(doubleLat), Double.toString(doubleLng), location, sendMsg).get();

//                            if(result.equals("done"))
//                                Toast.makeText(SetLocationPick.this,"success",Toast.LENGTH_SHORT).show();
//                            else
//                                Toast.makeText(SetLocationPick.this,"fail",Toast.LENGTH_SHORT).show();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        finish();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });
        //아래부터 최지호가 한 코드
        activitySetLocationPickBinding.centerLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetLocationCenter.class);
                intent.putExtra("sche_id", sche_id);
                startActivityForResult(intent, 4);
            }
        });

        activitySetLocationPickBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SetLocationAddress.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        activitySetLocationPickBinding.recomendFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalcLocation_map.class);
                intent.putExtra("category", "맛집");
                intent.putExtra("mapPointx", doubleLat);
                intent.putExtra("mapPointy", doubleLng);
                startActivity(intent);
            }
        });
        activitySetLocationPickBinding.recomendCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalcLocation_map.class);
                intent.putExtra("category", "카페");
                intent.putExtra("mapPointx", doubleLat);
                intent.putExtra("mapPointy", doubleLng);
                startActivity(intent);
            }
        });
        activitySetLocationPickBinding.recomendPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalcLocation_map.class);
                intent.putExtra("category", "관광명소");
                intent.putExtra("mapPointx", doubleLat);
                intent.putExtra("mapPointy", doubleLng);
                startActivity(intent);
            }
        });

        activitySetLocationPickBinding.locationText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //텍스트가 입력된 값이 변화가 있을 때
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                init_webView();
                // 핸들러를 통한 JavaScript 이벤트 반응
                handler = new Handler();
            }


            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }


    public void init_webView() {
        // WebView 설정
        webView = (WebView) findViewById(R.id.locationWebView);
        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // 여기서 WebView의 데이터를 가져오는 작업을 한다.

                String keyword = locationString.getText().toString();

                String script = "javascript:function afterLoad() {"
                        + "document.getElementById('address').value = '" + keyword + "';"
                        + "};"
                        + "change('"+ keyword +"');"
                        + "afterLoad();";
                view.loadUrl(script);
            }
        });

        // webview url load. php 파일 주소
        //webView.loadUrl("http://192.168.123.105:8080/server/locationPickWebView.jsp");
        //webView.loadUrl("http://192.168.0.7:8080/project_Server/locationPickWebView.jsp"); //규영
        webView.loadUrl("http://172.30.1.18:8080/server/locationPickWebView.jsp"); // 수연
    }

    public class AndroidBridge {
        @JavascriptInterface
        public void sendPoint(final String latitude, final String longitude, final String placename) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            location = data.getExtras().getString("pickAddress");
            lat = data.getExtras().getString("lat");
            lng = data.getExtras().getString("lng");
            System.out.println(lat+"pick");
            System.out.println(lng+"pick");
            doubleLat = Double.parseDouble(lat);
            doubleLng = Double.parseDouble(lng);
            locationString.setText(location);
        }
        if (requestCode == 4) {
            String location2 = data.getStringExtra("Location");
            Intent intent = new Intent();
            intent.putExtra("Location", location2);
            Log.i("location" ,"pick" + location2);
            setResult(4,intent);
            finish();
        }
    }


}











//    public void OnClickHandler (View view)
//    {
//
//    }