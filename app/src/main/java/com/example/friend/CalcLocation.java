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


public class CalcLocation extends AppCompatActivity {

    private ActivityCalcLocationBinding activityCalcLocationBinding;
    private double mapPointx;
    private double mapPointy;
    private String location = "";
    private String sche_id, saveDB,sendMsg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityCalcLocationBinding = ActivityCalcLocationBinding.inflate(getLayoutInflater());
        setContentView(activityCalcLocationBinding.getRoot());

        Intent intent = getIntent();
        mapPointx = intent.getDoubleExtra("mapPointx",0);
        mapPointy = intent.getDoubleExtra("mapPointy", 0);
        location = intent.getStringExtra("location");
        sche_id = intent.getStringExtra("sche_id");

        Toast.makeText(getApplicationContext(),
                mapPointx + ", " + mapPointy + "," + location,  Toast.LENGTH_SHORT).show();

        activityCalcLocationBinding.locationTextview.setText(location);

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
                try {
                    sendMsg ="savePoint";
                    saveDB = new CustomTask(sendMsg).execute(sche_id, Double.toString(mapPointx), Double.toString(mapPointy), location, sendMsg).get();
                    Intent intent = new Intent(getApplicationContext(), ScheduleMainHome.class);
                    startActivity(intent);
                } catch (Exception e) { }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
