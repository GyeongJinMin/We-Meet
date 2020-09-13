package com.example.friend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.friend.databinding.ActivitySetScheduleCalenderBinding;
import com.example.friend.databinding.CalendarMainBinding;

import java.util.concurrent.ExecutionException;

public class SetScheduleCalender extends AppCompatActivity {
    private ActivitySetScheduleCalenderBinding activitySetScheduleCalenderBinding;
    private String date;
    private String sche_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySetScheduleCalenderBinding = ActivitySetScheduleCalenderBinding.inflate(getLayoutInflater());
        setContentView(activitySetScheduleCalenderBinding.getRoot());

        Intent getSchdule = getIntent();
        sche_id = getSchdule.getStringExtra("sche_id");

        activitySetScheduleCalenderBinding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = year + "-" + (month + 1) + "-" + dayOfMonth;
            }
        });

        activitySetScheduleCalenderBinding.selectDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SetScheduleCalender.this);
                builder.setTitle("일정 선택").setMessage("이 날로 하시겠습니까?");

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("Date", date);
                        setResult(1, intent);

                        try {
                            String result = new CustomTask().execute(sche_id, date, "setDate").get();
                            //Log.i("sche_date",date);

//                            if(result.equals("done"))
//                                Toast.makeText(SetScheduleCalender.this,"success",Toast.LENGTH_SHORT).show();
//                            else
//                                Toast.makeText(SetScheduleCalender.this,"fail",Toast.LENGTH_SHORT).show();
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
    }


    /*public void OnClickHandler(View view) {
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
                intent.putExtra("Date", date);
                Log.i("date", "setDate = " + date);
                finish();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }*/
}