package com.example.friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.friend.databinding.ActivityVoteBinding;
import com.example.friend.databinding.ActivityVoteLocationBinding;
import com.example.friend.databinding.VoteListBinding;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class VoteLocationActivity extends AppCompatActivity { // DateVote
    private ActivityVoteLocationBinding activityVoteLocationBinding;
    private VoteLocationAdapter voteLocationAdapter;
    private String[] location_vote_list;
    private ArrayList<VoteLocation> votes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVoteLocationBinding = ActivityVoteLocationBinding.inflate(getLayoutInflater());
        setContentView(activityVoteLocationBinding.getRoot());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        activityVoteLocationBinding.voteRecycler.setLayoutManager(linearLayoutManager);

        votes = new ArrayList<>();
        voteLocationAdapter = new VoteLocationAdapter(this, votes);
        activityVoteLocationBinding.voteRecycler.setAdapter(voteLocationAdapter);

        try {
            String result = new CustomTask().execute("loadLocationVote").get();
            if (result.getBytes().length > 0) {
                location_vote_list = result.split("\t");

                for (int i = 0; i < location_vote_list.length; i = i+3) { //나누기
                    votes.add(0, new VoteLocation(location_vote_list[i], location_vote_list[i+1], location_vote_list[i+2]));
                    voteLocationAdapter.notifyItemInserted(0);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(activityVoteLocationBinding.voteRecycler.getContext(), linearLayoutManager.getOrientation());
        activityVoteLocationBinding.voteRecycler.addItemDecoration(dividerItemDecoration);

        activityVoteLocationBinding.dateVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(VoteLocationActivity.this, VoteActivity.class);
////                startActivity(intent);

                finish();

            }
        });
    }
}

