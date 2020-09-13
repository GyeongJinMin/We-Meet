package com.example.friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.friend.databinding.ItemRequestBinding;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

class MyRequestHolder extends RecyclerView.ViewHolder {

    ItemRequestBinding mBinding;

    MyRequestHolder(ItemRequestBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }
}

public class MyAdapterRequest extends RecyclerView.Adapter {

    private List<Profile> mProfiles;
    private Context context;

    private String id, friendName, sendMsg;

    MyAdapterRequest(List<Profile> profiles, Context context) {
        mProfiles = profiles;
        this.context = context;

        readData(new File(context.getFilesDir(), "id.txt"));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;

        ItemRequestBinding requestBinding = ItemRequestBinding.inflate(LayoutInflater.from(context), parent, false);
        holder = new MyRequestHolder(requestBinding);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        MyRequestHolder mHolder = (MyRequestHolder) holder;
        //mHolder.mBinding.image.setBackgroundResource(mProfiles.get(position).getIcon());
        // mHolder.mBinding.image.setImageResource(mProfiles.get(position).getIcon());
        mHolder.mBinding.name.setText(mProfiles.get(position).getName());

        mHolder.mBinding.btnNo.setTag(holder.getAdapterPosition());
        mHolder.mBinding.btnNo.setOnClickListener(new View.OnClickListener() { // 거절 버튼
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                friendName = mProfiles.get(pos).getName(); // 요청을 보낸 친구의 이름

                sendMsg = "friendReject";

                try {
                    String result = new CustomTask(sendMsg).execute(id, friendName, "friendReject").get();
                    if (result.equals("true")) {
                        mProfiles.remove(pos);
                        Toast.makeText(context, "거절", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mHolder.mBinding.btnYes.setTag(holder.getAdapterPosition());
        mHolder.mBinding.btnYes.setOnClickListener(new View.OnClickListener() { // 수락 버튼
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                friendName = mProfiles.get(pos).getName(); // 요청을 보낸 친구의 이름

                sendMsg = "friendAccept";

                try {
                    String result = new CustomTask(sendMsg).execute(id, friendName, "friendAccept").get();
                    if (result.equals("true")) {
                        mProfiles.remove(pos);
                        Toast.makeText(context, "수락", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void readData(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            id = new String(data);
            fis.close();
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() { return mProfiles.size();}
}