package com.example.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.friend.databinding.ActivityScheduleMainHomeBinding;

import java.util.concurrent.ExecutionException;

public class ScheduleMainHome extends AppCompatActivity {
    private ActivityScheduleMainHomeBinding activityScheduleMainHomeBinding;
    private String date;
    private String location;
    private String inform;
    private String participants, person;
    private String[] schedule;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Log.i("request", Integer.toString(requestCode));

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 1) {
                date = data.getStringExtra("Date");
            }
            if (requestCode == 2) {
                location = data.getStringExtra("Location");
            }
            if (requestCode == 3) {
                person = data.getStringExtra("Participants");
            }

            if (date != null && location != null)
                inform = date + "\n" + location;
            else {
                if (location == null)
                    inform = date;
                if (date == null)
                    inform = location;
            }

            if(participants != null)
                participants += person;
            else
                participants = person;

            activityScheduleMainHomeBinding.informBtn.setText(inform);
            activityScheduleMainHomeBinding.personName.setText(participants);

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityScheduleMainHomeBinding = ActivityScheduleMainHomeBinding.inflate(getLayoutInflater());
        setContentView(activityScheduleMainHomeBinding.getRoot());
        Intent getMainIntent = getIntent();
        String schedule_name = getMainIntent.getStringExtra("schedule_name");
        final String schedule_id = getMainIntent.getStringExtra("schedule_id");
        activityScheduleMainHomeBinding.scheduleName.setText(schedule_name);

        try {
            String result = new CustomTask().execute(schedule_id, "loadSche").get();

            if (result.getBytes().length > 0) {
                schedule = result.split("\t");
                for(int i=0; i<4;i++)
                {
                    Log.i("schedule","schduel : " + i + schedule[i]);
                }
                schedule_name = schedule[0];
                activityScheduleMainHomeBinding.scheduleName.setText(schedule_name);
                if (!schedule[1].equals("null"))
                    date = schedule[1];
                if (!schedule[2].equals("null"))
                    location = schedule[2];
                if(!schedule[3].equals("null"))
                    participants = schedule[3];

                if (date == null && location == null)
                    inform = "약속정보";
                else if (date != null && location != null)
                    inform = date + "\n" + location;
                else {
                    if (location == null)
                        inform = date;
                    if (date == null)
                        inform = location;
                }
                activityScheduleMainHomeBinding.informBtn.setText(inform);
                activityScheduleMainHomeBinding.personName.setText(participants);

            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        activityScheduleMainHomeBinding.addPersonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetParticipants.class);
                intent.putExtra("sche_id",schedule_id);
                intent.putExtra("message","addPerson");
                startActivityForResult(intent, 3);
            }
        });

        activityScheduleMainHomeBinding.setScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetScheduleCalender.class);
                intent.putExtra("sche_id", schedule_id);
                startActivityForResult(intent, 1);
            }
        });


        activityScheduleMainHomeBinding.setLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetLocationPick.class);
                intent.putExtra("sche_id", schedule_id);
                startActivityForResult(intent, 2);
            }
        });

        activityScheduleMainHomeBinding.realTimePositionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemberPosition.class);
                startActivity(intent);
            }
        });

        activityScheduleMainHomeBinding.informBtn.setOnClickListener(new View.OnClickListener() {
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