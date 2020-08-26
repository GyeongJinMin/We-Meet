package com.example.friend;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friend.databinding.ActivityScheduleMainBinding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ScheduleMainActivity extends Fragment {
    private ActivityScheduleMainBinding activityScheduleMainBinding;
    private ArrayList<Schedule> schedules;
    private ScheduleAdapter scheduleAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activityScheduleMainBinding = ActivityScheduleMainBinding.inflate(getLayoutInflater());
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        activityScheduleMainBinding.scheduleList.setLayoutManager(linearLayoutManager);

        schedules = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(getContext(), schedules);
        activityScheduleMainBinding.scheduleList.setAdapter(scheduleAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(activityScheduleMainBinding.scheduleList.getContext(),linearLayoutManager.getOrientation());
        activityScheduleMainBinding.scheduleList.addItemDecoration(dividerItemDecoration);

        activityScheduleMainBinding.scheduleList.addOnItemTouchListener(new RecyclerTouchListener(getContext(), activityScheduleMainBinding.scheduleList, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Schedule schedule = schedules.get(position);
                Intent intent = new Intent(getContext(),ScheduleMainHome.class);
                intent.putExtra("schedule_name",schedule.getSchedule_name());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        activityScheduleMainBinding.addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_add_new_schedule,null,false);
                builder.setView(view);

                final Button finish_btn = (Button) view.findViewById(R.id.finish_btn);
                final EditText edit_schedule_name = (EditText) view.findViewById(R.id.edit_schedule_name);
                // 참여자 받아오기 필요

                final AlertDialog dialog = builder.create();

                finish_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String schedule_name = edit_schedule_name.getText().toString();

                        Schedule schedule = new Schedule(schedule_name);
                        schedules.add(0,schedule);
                        scheduleAdapter.notifyItemInserted(0);
                        // 서버에도 저장하기

                        dialog.dismiss();

                        Intent intent = new Intent(getContext(),ScheduleMainHome.class);
                        intent.putExtra("schedule_name",schedule_name);
                        startActivity(intent);
                    }
                });
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.show();

                Window window = dialog.getWindow();
                window.setAttributes(layoutParams);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
        return activityScheduleMainBinding.getRoot();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ScheduleMainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ScheduleMainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}


