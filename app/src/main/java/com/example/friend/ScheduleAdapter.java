package com.example.friend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static androidx.appcompat.app.AlertDialog.*;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private ArrayList<Schedule> schedule_list;
    private Context context;
    private String sendMsg;

    private String participants;

    public class ScheduleViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        protected TextView name;
//        protected Button date_btn;
//        protected Button location_btn;
//        protected Button inform_btn;

        public ScheduleViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.schedule_name);
//            this.date_btn = (Button) v.findViewById(R.id.set_schedule_btn);
//            this.location_btn = (Button) v.findViewById(R.id.set_location_btn);
//
//
//            this.inform_btn = (Button) v.findViewById(R.id.inform_btn);
//            //Log.i("btn",this.inform_btn.toString());

            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem edit = menu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            edit.setOnMenuItemClickListener(onEditMenu);
            delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                switch (item.getItemId()) {
                    case 1001:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View view = LayoutInflater.from(context).inflate(R.layout.activity_add_new_schedule, null, false);
                        builder.setView(view);

                        final Button finish_btn = (Button) view.findViewById(R.id.finish_btn);
                        final EditText edit_schedule_name = (EditText) view.findViewById(R.id.edit_schedule_name);
                        final Button add_person_btn = (Button) view.findViewById(R.id.add_person_btn);

                        String sche_id = schedule_list.get(getAdapterPosition()).getSche_id();
                        edit_schedule_name.setText(schedule_list.get(getAdapterPosition()).getSche_name());

                        final AlertDialog dialog = builder.create();

                        final String finalSche_id = sche_id;

                        try {
                            sendMsg = "loadParticipants";
                            String result = new CustomTask(sendMsg).execute(finalSche_id, "loadParticipants").get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        add_person_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, SetParticipants.class);
                                intent.putExtra("sche_id",finalSche_id);
                                intent.putExtra("message","addPerson");
                                context.startActivity(intent);
                            }
                        });

                        finish_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String schedule_name = edit_schedule_name.getText().toString();

                                Schedule schedule = new Schedule(schedule_name);

                                schedule_list.set(getAdapterPosition(), schedule);
                                notifyItemChanged(getAdapterPosition());

                                try {
                                    String result = new CustomTask().execute(finalSche_id, schedule_name, "modiSche").get();
                                    String res = new CustomTask().execute(finalSche_id, schedule_name, "modiVote").get();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }  // 서버에도 저장하기

                                dialog.dismiss();
                            }
                        });

                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                        dialog.show();

                        Window window = dialog.getWindow();
                        window.setAttributes(layoutParams);
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        break;

                    case 1002:
                        sche_id = schedule_list.get(getAdapterPosition()).getSche_id();
                        try {
                            String result = new CustomTask().execute(sche_id, "delSche").get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        schedule_list.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), schedule_list.size());

                        break;
                }

                return true;
            }
        };
    }

    public ScheduleAdapter(Context context, ArrayList<Schedule> list) {
        this.schedule_list = list;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return (null != schedule_list ? schedule_list.size() : 0);
    }

    @NonNull
    @Override
    public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list, parent, false);

        ScheduleViewHolder scheduleViewHolder = new ScheduleViewHolder(view);

        return scheduleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ScheduleViewHolder holder, int position) {
//        Log.i("holder","IN");
//        String[] schedule = null;
//        String date = null;
//        String location = null;
//        String inform = null;
//        try {
//            String result = new CustomTask().execute(schedule_list.get(position).getSche_id(), "loadSche").get();
//
//            if (result.getBytes().length > 0) {
//                schedule = result.split("\t");
//                for (int i = 0; i < 4; i++) {
//                    Log.i("schedule", "schduel : " + i + schedule[i]);
//                }
//                if (!schedule[1].equals("null"))
//                    date = schedule[1];
//                if (!schedule[2].equals("null"))
//                    location = schedule[2];
//
//                if (date == null && location == null)
//                    inform = "약속정보";
//                else if (date != null && location != null)
//                    inform = date + "\n" + location;
//                else {
//                    if (location == null)
//                        inform = date;
//                    if (date == null)
//                        inform = location;
//                }
//
//            }
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        holder.name.setGravity(Gravity.LEFT);
        holder.name.setText(schedule_list.get(position).getSche_name());

//        if (holder.inform_btn != null)
//            holder.inform_btn.setText(inform);
//        else
//            Log.i("holder","NULL");
    }
}