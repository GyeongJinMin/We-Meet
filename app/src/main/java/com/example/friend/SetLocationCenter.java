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

import androidx.appcompat.app.AppCompatActivity;


public class SetLocationCenter extends AppCompatActivity {
    private WebView mWebView;
    private WebSettings mWebSettings;
    private Handler handler;
    private Button btn_calc, btn_pick;
    private double mapPointx = 0, mapPointy = 0;
    private String name = "aa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location_center);
        btn_calc = (Button)findViewById(R.id.calc_btn);
        btn_pick = (Button)findViewById(R.id.pick_location_btn);
        init_webView();
        handler = new Handler();

        btn_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalcLocation.class);

                intent.putExtra("placename", name);
                intent.putExtra("mapPointx", mapPointx);
                intent.putExtra("mapPointy", mapPointy);
                startActivity(intent);
                finish();
            }
        });

        btn_pick.setOnClickListener(new View.OnClickListener() {
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
        mWebView.loadUrl("http://172.30.1.29:8080/project_Server/kakaomap.jsp");

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
