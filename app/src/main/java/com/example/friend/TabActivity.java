package com.example.friend;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;

public class TabActivity extends AppCompatActivity {
    private LocalActivityManager mlam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // ... 코드 계속

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab);
        mlam = new LocalActivityManager(this, false);
        mlam.dispatchCreate(savedInstanceState);
        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup(mlam);
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, MainActivity.class);
        spec = tabHost1.newTabSpec("friend").setIndicator("friend").setContent(intent);
        tabHost1.addTab(spec);



        intent = new Intent().setClass(this, Calendar_main.class);
        spec = tabHost1.newTabSpec("calendar").setIndicator("calendar").setContent(intent);
        tabHost1.addTab(spec);

        intent = new Intent().setClass(this, ScheduleMainActivity.class);
        spec = tabHost1.newTabSpec("schedule").setIndicator("schedule").setContent(intent);
        tabHost1.addTab(spec);

        tabHost1.setCurrentTab(0);

    }





}