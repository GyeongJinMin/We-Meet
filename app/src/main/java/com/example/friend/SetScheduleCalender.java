package com.example.friend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.friend.databinding.ActivitySetScheduleCalenderBinding;

public class SetScheduleCalender extends AppCompatActivity {
    private ActivitySetScheduleCalenderBinding activitySetScheduleCalenderBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySetScheduleCalenderBinding = ActivitySetScheduleCalenderBinding.inflate(getLayoutInflater());
        setContentView(activitySetScheduleCalenderBinding.getRoot());
    }



    public void OnClickHandler (View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("일정 선택").setMessage("이 날로 하시겠습니까?");

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //일정 넘기기
                Intent intent = getIntent();
                intent.putExtra("Date",activitySetScheduleCalenderBinding.calendarView.getDate());
                finish();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}