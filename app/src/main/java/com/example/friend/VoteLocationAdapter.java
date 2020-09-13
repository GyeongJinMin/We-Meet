package com.example.friend;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class VoteLocationAdapter extends RecyclerView.Adapter<VoteLocationAdapter.VoteLocationViewHolder> {

    private ArrayList<VoteLocation> vote_list;
    private Context context;

    public VoteLocationAdapter(Context context, ArrayList<VoteLocation> list) {
        this.vote_list = list;
        this.context = context;
    }

    public class VoteLocationViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        protected TextView name;
        protected TextView location;
        protected Button yes_btn;
        protected Button no_btn;

        public VoteLocationViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.schedule_name);
            this.location = (TextView) v.findViewById(R.id.date);
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
    public VoteLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_list, parent, false);

        VoteLocationViewHolder VoteViewHolder = new VoteLocationViewHolder(view);

        return VoteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VoteLocationViewHolder holder, final int position) {
        holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        holder.name.setGravity(Gravity.LEFT);
        holder.name.setText(vote_list.get(position).getSchedule_name());
        holder.location.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        holder.location.setGravity(Gravity.LEFT);
        holder.location.setText(vote_list.get(position).getLocation());

        holder.yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String result = new CustomTask().execute(vote_list.get(position).getSchedule_id(),"yes","voteLocation").get();
                    Log.i("res" , result);
                    if(result.equals("revote")) {
                        String init = new CustomTask().execute(vote_list.get(position).getSchedule_id(),"initVoteLocation").get();
                        //String reset = new CustomTask().execute()
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        });

        holder.no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String result = new CustomTask().execute(vote_list.get(position).getSchedule_id(),"no","voteLocation").get();
                    if(result.equals("revote")) {
                        String init = new CustomTask().execute(vote_list.get(position).getSchedule_id(),"initVoteLocation").get();
                        //String reset = new CustomTask().execute()
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != vote_list ? vote_list.size() : 0);
    }


}
