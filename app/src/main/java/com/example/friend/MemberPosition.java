package com.example.friend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.friend.databinding.ActivityMemberPositionBinding;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MemberPosition extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member_position);
        MapPoint syPoint;
        MapPoint kyPoint;
        MapPoint gjPoint;
        MapPoint Point;
        MapView mapView = new MapView(this);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5397907743775, 127.001765135176), true);
        mapView.setZoomLevel(7, true);

        MapPOIItem location = new MapPOIItem();
        Point = MapPoint.mapPointWithGeoCoord(37.5397907743775, 127.001765135176);
        location.setItemName("목적지");
        location.setTag(0);
        location.setMapPoint(Point);
        location.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.
        location.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        MapPOIItem suyeon = new MapPOIItem();
        syPoint = MapPoint.mapPointWithGeoCoord(37.497173599999996,126.88671970000001);
        suyeon.setItemName("이수연");
        suyeon.setTag(0);
        suyeon.setMapPoint(syPoint);
        suyeon.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        suyeon.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        MapPOIItem kyu = new MapPOIItem();
        kyPoint = MapPoint.mapPointWithGeoCoord(37.5106316985927,127.112393866995);
        kyu.setItemName("이규영");
        kyu.setTag(1);
        kyu.setMapPoint(kyPoint);
        kyu.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        kyu.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        MapPOIItem gyeong = new MapPOIItem();
        gjPoint = MapPoint.mapPointWithGeoCoord(37.5883948842004,127.00595033342);
        gyeong.setItemName("민경진");
        gyeong.setTag(2);
        gyeong.setMapPoint(gjPoint);
        gyeong.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        gyeong.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(suyeon);
        mapView.addPOIItem(kyu);
        mapView.addPOIItem(gyeong);
        mapView.addPOIItem(location);
        ViewGroup mapViewContainer = findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);

    }
}