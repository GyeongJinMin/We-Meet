package com.example.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.friend.databinding.ActivityScheduleMainHomeBinding;

public class ScheduleMainHome extends AppCompatActivity {
    private ActivityScheduleMainHomeBinding activityScheduleMainHomeBinding;
    private String date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityScheduleMainHomeBinding = ActivityScheduleMainHomeBinding.inflate(getLayoutInflater());
        setContentView(activityScheduleMainHomeBinding.getRoot());
        Intent getMainIntent = getIntent();
        String schedule_name = getMainIntent.getStringExtra("schedule_name");
        activityScheduleMainHomeBinding.scheduleName.setText(schedule_name);


        activityScheduleMainHomeBinding.setScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SetScheduleCalender.class);
                startActivity(intent);

                Intent getCalender = getIntent();
                date = getCalender.getStringExtra("Date");
                activityScheduleMainHomeBinding.infromBtn.setText(date);
                //finish();
            }
        });

        activityScheduleMainHomeBinding.setLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetLocationPick.class);
                startActivity(intent);
            }
        });

        activityScheduleMainHomeBinding.realTimePositionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemberPosition.class);
                startActivity(intent);
            }
        });

        activityScheduleMainHomeBinding.infromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InformLocation.class);
                startActivity(intent);
            }
        });

    }
/*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Intent getMainIntent = getActivity().getIntent();
        Intent intent = getIntent();
        String schedule_name = getMainIntent.getStringExtra("schedule_name");
        activityScheduleMainHomeBinding.scheduleName.setText(schedule_name);


        activityScheduleMainHomeBinding.setScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SetScheduleCalender.class);
                startActivity(intent);

                Intent getCalender = getActivity().getIntent();
                date = getCalender.getStringExtra("Date");
                activityScheduleMainHomeBinding.infromBtn.setText(date);
                //finish();
            }
        });

        activityScheduleMainHomeBinding.setLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SetLocationPick.class);
                startActivity(intent);
            }
        });

        activityScheduleMainHomeBinding.realTimePositionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MemberPosition.class);
                startActivity(intent);
            }
        });

        activityScheduleMainHomeBinding.infromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InformLocation.class);
                startActivity(intent);
            }
        });


        return activityScheduleMainHomeBinding.getRoot();
    }
    */

}