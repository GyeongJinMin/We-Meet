package com.example.friend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.friend.databinding.ActivityCalcLocationBinding;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class CalcLocation extends AppCompatActivity {

    private ActivityCalcLocationBinding activityCalcLocationBinding;
    private double mapPointx;
    private double mapPointy;
    private String placename;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityCalcLocationBinding = ActivityCalcLocationBinding.inflate(getLayoutInflater());
        setContentView(activityCalcLocationBinding.getRoot());

        //mapPoint[1] = MapPoint.mapPointWithGeoCoord(mapPointx,mapPointy);

        Intent intent = getIntent();
        mapPointx = intent.getDoubleExtra("mapPointx",0);
        mapPointy = intent.getDoubleExtra("mapPointy", 0);
        placename = intent.getStringExtra("placename");

        Toast.makeText(getApplicationContext(),
                mapPointx + ", " + mapPointy + "," + placename,  Toast.LENGTH_SHORT).show();

        activityCalcLocationBinding.locationTextview.setText(placename);

        activityCalcLocationBinding.pickLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetLocationPick.class);
                startActivity(intent);
                finish();
            }
        });
        activityCalcLocationBinding.btnCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalcLocation_map.class);
                intent.putExtra("category", "카페");
                intent.putExtra("mapPointx", mapPointx);
                intent.putExtra("mapPointy", mapPointy);
                startActivity(intent);
            }
        });
        activityCalcLocationBinding.btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalcLocation_map.class);
                intent.putExtra("category", "맛집");
                intent.putExtra("mapPointx", mapPointx);
                intent.putExtra("mapPointy", mapPointy);
                startActivity(intent);
            }
        });
        activityCalcLocationBinding.btnTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalcLocation_map.class);
                intent.putExtra("category", "관광명소");
                intent.putExtra("mapPointx", mapPointx);
                intent.putExtra("mapPointy", mapPointy);
                startActivity(intent);
            }
        });

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
                intent.putExtra("mapPointx", mapPointx);
                intent.putExtra("mapPointy", mapPointy);
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