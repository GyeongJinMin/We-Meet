package com.example.friend;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.friend.databinding.ActivityCalcLocationBinding;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class CalcLocation extends AppCompatActivity {

    private ActivityCalcLocationBinding activityCalcLocationBinding;
    private double mapPointx[];
    private double mapPointy[];
    private MapPoint[] mapPoint;
    private MapView mapView;
    private MapPOIItem[] mDefaultMarker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityCalcLocationBinding = ActivityCalcLocationBinding.inflate(getLayoutInflater());
        setContentView(activityCalcLocationBinding.getRoot());

        mapView = new MapView(this);
        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        createDefaultMarker(mapView);

        //mapPoint[1] = MapPoint.mapPointWithGeoCoord(mapPointx,mapPointy);

        activityCalcLocationBinding.pickLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetLocationPick.class);
                startActivity(intent);
                finish();
            }
        });
        activityCalcLocationBinding.btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="kakaomap://search?q=맛집&p=" + mapPointx + "," + mapPointy;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        activityCalcLocationBinding.btnCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="kakaomap://search?q=카페&p=" + mapPointx + "," + mapPointy;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

    }

    private void createDefaultMarker(MapView mapView) {
        mapPointx[1] = 37.537229;
        mapPointy[1] = 127.005515;
        mapPointx[2] =37.582482;
        mapPointy[2] = 127.009468;
        for (int i = 0 ; i<mDefaultMarker.length; i++) {
            mDefaultMarker[i] = new MapPOIItem();
            String name = "사용자" + i;
            mapPoint[i] = MapPoint.mapPointWithGeoCoord(mapPointx[i], mapPointy[i]);
            mDefaultMarker[i].setItemName(name);
            mDefaultMarker[i].setTag(0);
            mDefaultMarker[i].setMapPoint(mapPoint[i]);
            mDefaultMarker[i].setMarkerType(MapPOIItem.MarkerType.BluePin);
            mDefaultMarker[i].setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

            mapView.addPOIItem(mDefaultMarker[1]);
            mapView.selectPOIItem(mDefaultMarker[1], true);
            mapView.setMapCenterPoint(mapPoint[i], true);
        }
    }




    public void OnClickHandler (View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("장소 선택").setMessage("이 곳으로 하시겠습니까?");

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), ScheduleMainHome.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activityCalcLocationBinding = ActivityCalcLocationBinding.inflate(getLayoutInflater());
        activityCalcLocationBinding.pickLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SetLocationPick.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return activityCalcLocationBinding.getRoot();
    }



    public void OnClickHandler (View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("장소 선택").setMessage("이 곳으로 하시겠습니까?");

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), ScheduleMainHome.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

     */
}