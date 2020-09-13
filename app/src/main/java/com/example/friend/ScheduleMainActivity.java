package com.example.friend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ScheduleMainActivity extends Fragment {
    private ActivityScheduleMainBinding activityScheduleMainBinding;
    private ArrayList<Schedule> schedules;
    private ScheduleAdapter scheduleAdapter;
    private String[] schedule_list;
    private String id;
    private String sche_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activityScheduleMainBinding = ActivityScheduleMainBinding.inflate(getLayoutInflater());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        activityScheduleMainBinding.scheduleList.setLayoutManager(linearLayoutManager);

        schedules = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(getContext(), schedules);
        activityScheduleMainBinding.scheduleList.setAdapter(scheduleAdapter);

        readData(new File(getContext().getFilesDir(), "id.txt"));

        try {
            String result = new CustomTask().execute("loadAllSche").get();
            if (result.getBytes().length > 0) {
                schedule_list = result.split("\t");

                for (int i = 0; i < schedule_list.length; i = i + 2) { //나누기
                    schedules.add(0, new Schedule(schedule_list[i],schedule_list[i+1]));
                    scheduleAdapter.notifyItemInserted(0);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(activityScheduleMainBinding.scheduleList.getContext(), linearLayoutManager.getOrientation());
        activityScheduleMainBinding.scheduleList.addItemDecoration(dividerItemDecoration);

        activityScheduleMainBinding.scheduleList.addOnItemTouchListener(new RecyclerTouchListener(getContext(), activityScheduleMainBinding.scheduleList, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Schedule schedule = schedules.get(position);
                Intent intent = new Intent(getContext(), ScheduleMainHome.class);
                intent.putExtra("schedule_name", schedule.getSche_name());
                intent.putExtra("schedule_id", schedule.getSche_id());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        activityScheduleMainBinding.voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VoteActivity.class);
                startActivity(intent);
            }
        });

        activityScheduleMainBinding.addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_add_new_schedule, null, false);
                builder.setView(view);

                final Button finish_btn = (Button) view.findViewById(R.id.finish_btn);
                final EditText edit_schedule_name = (EditText) view.findViewById(R.id.edit_schedule_name);
                // 참여자 받아오기 필요

                final AlertDialog dialog = builder.create();

                finish_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String schedule_name = edit_schedule_name.getText().toString();
                        try {
                            String result = new CustomTask().execute(id, schedule_name, "addSche").get();
                            sche_id = result;
                            String vote_add = new CustomTask().execute(sche_id, schedule_name,"addVote").get();

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();

                        schedules.add(0, new Schedule(schedule_name, sche_id));
                        scheduleAdapter.notifyItemInserted(0);

                        Intent intent = new Intent(getContext(), ScheduleMainHome.class);
                        intent.putExtra("schedule_name", schedule_name);
                        intent.putExtra("schedule_id",sche_id);
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

    void readData(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            id = new String(data);
            Log.i("id","id = "+id);
            fis.close();
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
}


