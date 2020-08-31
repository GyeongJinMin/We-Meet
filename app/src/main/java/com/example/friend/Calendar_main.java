package com.example.friend;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.friend.databinding.CalendarMainBinding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//1 캘린더 메인
public class Calendar_main extends Fragment {
    CalendarView mCalendar;
    CalendarMainBinding calendarMainBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calendarMainBinding = CalendarMainBinding.inflate(getLayoutInflater());
        try {
            PackageInfo info = getContext().getPackageManager().getPackageInfo("com.example.friend", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } // 카카오 api 키 해시 구하는 과정
        mCalendar=calendarMainBinding.calendarView;


        mCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m,
                                            int d) {
                Intent intent = new Intent(getContext(), Calendar_popup.class);
                intent.putExtra("year", y);
                intent.putExtra("month", m);
                intent.putExtra("day",d);
                startActivity(intent);
            }
        });

        return calendarMainBinding.getRoot();
    }

}