package com.example.friend;

import android.content.Context;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.VoteViewHolder> {

    private ArrayList<Vote> vote_list;
    private Context context;

    public VoteAdapter(Context context, ArrayList<Vote> list) {
        this.vote_list = list;
        this.context = context;
    }

    public class VoteViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        protected TextView name;
        protected TextView date;
        protected Button yes_btn;
        protected Button no_btn;

        public VoteViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.schedule_name);
            this.date = (TextView) v.findViewById(R.id.date);
            this.yes_btn = (Button) v.findViewById(R.id.y_btn);
            this.no_btn = (Button) v.findViewById(R.id.n_btn);

            v.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }
    @NonNull
    @Override
    public VoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_list, parent, false);

        VoteViewHolder VoteViewHolder = new VoteViewHolder(view);

        return VoteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VoteViewHolder holder, final int position) {
        holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        holder.name.setGravity(Gravity.LEFT);
        holder.name.setText(vote_list.get(position).getSchedule_name());
        holder.date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        holder.date.setGravity(Gravity.LEFT);
        holder.date.setText(vote_list.get(position).getDate());

        holder.yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String result = new CustomTask().execute(vote_list.get(position).getSchedule_id(),"yes","voteDate").get();
                    if(result.equals("revote")) {
                        String init = new CustomTask().execute(vote_list.get(position).getSchedule_id(),"initVoteDate").get();
                        String re = new CustomTask().execute(vote_list.get(position).getSchedule_id(),null,"setDate").get();
                        //String reset = new CustomTask().execute()
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context,"Yes",Toast.LENGTH_SHORT).show();
            }
        });

        holder.no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String result = new CustomTask().execute(vote_list.get(position).getSchedule_id(),"no","voteDate").get();
                    if(result.equals("revote")) {
                        String init = new CustomTask().execute(vote_list.get(position).getSchedule_id(),"initVoteDate").get();
                        //String reset = new CustomTask().execute()
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context,"No",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != vote_list ? vote_list.size() : 0);
    }


}