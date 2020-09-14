package com.example.friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.friend.databinding.ItemListBinding;
import com.example.friend.databinding.ItemParticipantsBinding;
import com.example.friend.databinding.ItemSearchBinding;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

class ParticipantsHolder extends RecyclerView.ViewHolder {

    ItemParticipantsBinding mBinding;

    ParticipantsHolder(ItemParticipantsBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }
}

public class ParticipantsAdapter extends RecyclerView.Adapter {

    private ArrayList<Profile> mProfiles;
    private Context context;

    private static final int PARTICIPANTS = -1;

    private String sendMsg, temp = "";
    private String[] participants;
    private String sche_id;
    private String message;

    ParticipantsAdapter(ArrayList<Profile> profiles, String sche_id, String message, Context context) {
        mProfiles = profiles;
        this.sche_id = sche_id;
        this.context = context;
        this.message = message;

        loadServer();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;

        if(viewType == PARTICIPANTS) { // 이미 참여중인 경우
            ItemListBinding listBinding = ItemListBinding.inflate(LayoutInflater.from(context), parent, false);
            holder = new MyListHolder(listBinding);
        } else {
            ItemParticipantsBinding participantsBinding = ItemParticipantsBinding.inflate(LayoutInflater.from(context), parent, false);
            holder = new ParticipantsHolder(participantsBinding);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyListHolder) { // instanceof는 형변환 가능 여부.
            MyListHolder mHolder = (MyListHolder) holder;
            mHolder.mBinding.name.setText(mProfiles.get(position).getName());
        }
        else {
            final ParticipantsHolder mHolder = (ParticipantsHolder) holder;
            mHolder.mBinding.name.setText(mProfiles.get(position).getName());
            mHolder.mBinding.checkFriend.setTag(holder.getAdapterPosition());

            mHolder.mBinding.checkFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 체크 된 것을 checkedList에 넣어야 됨...
                    // 체크 푸는 경우
                    if(mHolder.mBinding.checkFriend.isChecked()) {
                        if(temp.equals("")) {
                            temp += mProfiles.get(position).getName();
                        }
                        else {
                            temp += "," + mProfiles.get(position).getName();
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(message.equals("newSchedule")) {
            return position;
        }
        else {
            for (int i = 0; i < participants.length; i++) {
                if (mProfiles.get(position).getName().equals(participants[i])) // 참여자
                    return PARTICIPANTS;
            }
        }

        return position;
    }

    void loadServer() {
        if(!message.equals("newSchedule")) {
            try {
                sendMsg = "loadParticipants";
                String result = new CustomTask(sendMsg).execute(sche_id, "loadParticipants").get();
                participants = result.split(",");

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mProfiles.size();
    }

    public String getTemp() {
        return temp;
    }
}