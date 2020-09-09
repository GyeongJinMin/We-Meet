package com.example.friend;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import static androidx.appcompat.app.AlertDialog.*;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private ArrayList<Schedule> schedule_list;
    private Context context;

    public class ScheduleViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        protected TextView name;

        public ScheduleViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.schedule_name);

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
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 1001:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View view = LayoutInflater.from(context).inflate(R.layout.activity_add_new_schedule,null,false);
                        builder.setView(view);

                        final Button finish_btn = (Button) view.findViewById(R.id.finish_btn);
                        final EditText edit_schedule_name = (EditText) view.findViewById(R.id.edit_schedule_name);
                        // 참여자 받아오기 필요
                        edit_schedule_name.setText(schedule_list.get(getAdapterPosition()).getSche_name());

                        final AlertDialog dialog = builder.create();

                        finish_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String schedule_name = edit_schedule_name.getText().toString();

                                Schedule schedule = new Schedule(schedule_name);

                                schedule_list.set(getAdapterPosition(),schedule);
                                notifyItemChanged(getAdapterPosition());
                                // 서버에도 저장하기

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
                        schedule_list.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(),schedule_list.size());

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
        holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        holder.name.setGravity(Gravity.LEFT);
        holder.name.setText(schedule_list.get(position).getSche_name());
    }
}
