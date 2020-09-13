package com.example.friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.friend.databinding.ActivityVoteBinding;
import com.example.friend.databinding.VoteListBinding;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class VoteActivity extends AppCompatActivity { // DateVote
    private ActivityVoteBinding activityVoteBinding;
    private VoteAdapter voteAdapter;
    private String[] date_vote_list;
    private ArrayList<Vote> votes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVoteBinding = ActivityVoteBinding.inflate(getLayoutInflater());
        setContentView(activityVoteBinding.getRoot());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        activityVoteBinding.voteRecycler.setLayoutManager(linearLayoutManager);

        votes = new ArrayList<>();
        voteAdapter = new VoteAdapter(this, votes);
        activityVoteBinding.voteRecycler.setAdapter(voteAdapter);

        try {
            String result = new CustomTask().execute("loadDateVote").get();
            if (result.getBytes().length > 0) {
                date_vote_list = result.split("\t");
                for (int i = 0; i < date_vote_list.length; i = i+3) { //나누기
                    Log.i("vote", date_vote_list[i]);
                    votes.add(0, new Vote(date_vote_list[i], date_vote_list[i+1], date_vote_list[i+2]));
                    voteAdapter.notifyItemInserted(0);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(activityVoteBinding.voteRecycler.getContext(), linearLayoutManager.getOrientation());
        activityVoteBinding.voteRecycler.addItemDecoration(dividerItemDecoration);

        activityVoteBinding.locationVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VoteActivity.this, VoteLocationActivity.class);
                startActivity(intent);

            }
        });
    }


}

