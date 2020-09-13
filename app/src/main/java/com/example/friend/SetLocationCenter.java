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
    private String name = "aa";
    private String location;
    private String sche_id;

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==0)
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

                intent.putExtra("placename", name);
                intent.putExtra("mapPointx", mapPointx);
                intent.putExtra("mapPointy", mapPointy);
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
        //mWebView.loadUrl("http://172.30.1.29:8080/project_Server/kakaomap.jsp");
        mWebView.loadUrl("http://192.168.0.4:8080/server/kakaomap.jsp"); // 수연
        //mWebView.loadUrl("http://192.168.123.105:8080/server/kakaomap.jsp");
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
                    name = placename;
                    Toast.makeText(getApplicationContext(),
                            mapPointx + ", " + mapPointy + "," + name,  Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}


        /*
        private ActivitySetLocationCenterBinding activitySetLocationCenterBinding;
    private ArrayList<Double> mapPointx, mapPointy;
    private MapPoint mapPoint, centerPoint;
    private MapView mapView;
    private MapPOIItem mDefaultMarker;
    private double centerX, centerY;




        super.onCreate(savedInstanceState);
        activitySetLocationCenterBinding = ActivitySetLocationCenterBinding.inflate(getLayoutInflater());

        setContentView(activitySetLocationCenterBinding.getRoot());

        mapView = new MapView(this);
        mapPointx = new ArrayList<>();
        mapPointy = new ArrayList<>();
        mapPointx.add(37.537229);
        mapPointy.add(127.005515);
        mapPointx.add(37.582482);
        mapPointy.add(127.009468);
        double x = 0;
        double y= 0;
        for (int i=0; i< mapPointx.size(); i++) {
            x = x + mapPointx.get(i);
            y = y + mapPointy.get(i);
        }
        centerX = x/mapPointx.size();
        centerY = y/mapPointy.size();
        centerPoint.mapPointWithGeoCoord(centerX, centerY);

        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        createDefaultMarker(mapView);
        activitySetLocationCenterBinding.pickLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activitySetLocationCenterBinding.calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalcLocation.class);
                intent.putExtra("mapPointx", centerX);
                intent.putExtra("mapPointy", centerY);
                startActivity(intent);
                finish();
            }
        });

    }

    private void createDefaultMarker(MapView mapView) {
        for (int i = 0 ; i<mapPointx.size(); i++) {
            mDefaultMarker = new MapPOIItem();
            String name = "사용자" + i;
            mapPoint = MapPoint.mapPointWithGeoCoord(mapPointx.get(i), mapPointy.get(i));
            mDefaultMarker.setItemName(name);
            mDefaultMarker.setTag(i);
            mDefaultMarker.setMapPoint(mapPoint);
            mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

            mapView.addPOIItem(mDefaultMarker);

        }
        mapView.selectPOIItem(mDefaultMarker, false);
        mapView.setMapCenterPoint(centerPoint, true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activitySetLocationCenterBinding = ActivitySetLocationCenterBinding.inflate(getLayoutInflater());

        activitySetLocationCenterBinding.pickLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        activitySetLocationCenterBinding.calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CalcLocation.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return activitySetLocationCenterBinding.getRoot();
    }

 */
