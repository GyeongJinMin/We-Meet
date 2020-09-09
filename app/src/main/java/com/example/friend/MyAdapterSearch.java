package com.example.friend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.friend.databinding.ItemListBinding;
import com.example.friend.databinding.ItemSearchBinding;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

class MySearchHolder extends RecyclerView.ViewHolder {

    ItemSearchBinding mBinding;

    MySearchHolder(ItemSearchBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }
}

public class MyAdapterSearch extends RecyclerView.Adapter {

    private ArrayList<Profile> mProfiles;
    private Context context;

    private static final int MY = -1;
    private static final int FRIEND = -2;

    private String[] friends;
    private String id, sendMsg, userName;

    MyAdapterSearch(ArrayList<Profile> profiles, Context context) {
        mProfiles = profiles;
        this.context = context;

        readData(new File(context.getFilesDir(), "id.txt"));
        loadServer();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;

        if( viewType == MY || viewType == FRIEND ) { // 본인 또는 친구
            ItemListBinding listBinding = ItemListBinding.inflate(LayoutInflater.from(context), parent, false);
            holder = new MyListHolder(listBinding);
        } else { // 친구가 아닐 때
            ItemSearchBinding searchBinding = ItemSearchBinding.inflate(LayoutInflater.from(context), parent, false);
            holder = new MySearchHolder(searchBinding);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyListHolder) { // instanceof는 형변환 가능 여부.
            MyListHolder mHolder = (MyListHolder) holder;
            mHolder.mBinding.image.setBackgroundResource(mProfiles.get(position).getIcon());
            // mHolder.mBinding.image.setImageResource(mProfiles.get(position).getIcon());
            mHolder.mBinding.name.setText(mProfiles.get(position).getName());
        }
        else {
            MySearchHolder mHolder = (MySearchHolder) holder;
            mHolder.mBinding.image.setBackgroundResource(mProfiles.get(position).getIcon());
            // mHolder.mBinding.image.setImageResource(mProfiles.get(position).getIcon());
            mHolder.mBinding.name.setText(mProfiles.get(position).getName());

            mHolder.mBinding.btnRequest.setTag(holder.getAdapterPosition());
            mHolder.mBinding.btnRequest.setOnClickListener(new View.OnClickListener() { // 요청하기 버튼
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(context);
                    builder.setTitle("요청하기");
                    builder.setIcon(mProfiles.get(position).getIcon());
                    builder.setMessage(Html.fromHtml(mProfiles.get(position).getName() + " 님에게 친구 신청을 보내시겠습니까?"));
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 친구에게 신청알림 보내기
                            // 요청하기 버튼 지우기..?
                            Toast.makeText(context, "요청", Toast.LENGTH_SHORT).show();
                            int pos = (int) v.getTag();
                            // mProfiles.remove(pos); // 전체사용자 리스트. 아이템 삭제..?
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 변화 없음.
                            Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.create().show();

                    notifyDataSetChanged(); // 갱신
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) { // 본인, 친구, 다른 사용자 구분
        if (mProfiles.get(position).getName().equals(userName)) { // 본인인 경우
            return MY;
        }
        else {
            for(int i=0; i< friends.length; i++) {
                if(mProfiles.get(position).getName().equals(friends[i])) // 이미 친구인 경우
                    return FRIEND;
            }
        }
        return position;
    }

    void loadServer() {
        try {
            sendMsg = "loadUser";
            userName = new CustomTask(sendMsg).execute(id, "loadUser").get();

            sendMsg = "loadFriends";
            String result = new CustomTask(sendMsg).execute(id, "loadFriends").get();
            friends = result.split("\t");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
    public int getItemCount() {
        return mProfiles.size();
    }
}