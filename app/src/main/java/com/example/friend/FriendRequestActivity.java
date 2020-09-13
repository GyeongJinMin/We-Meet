package com.example.friend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Bundle;
import android.view.View;

import com.example.friend.databinding.ActivityFriendRequestBinding;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class FriendRequestActivity extends AppCompatActivity {

    ArrayList<Profile> mRequests = new ArrayList<>();
    ActivityFriendRequestBinding binding;

    private String[] waiters;
    private String id, sendMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void mShowRequest() { // 친구 신청 목록 보여주기
        sendMsg = "loadWaiters";
        readData(new File(this.getFilesDir(), "id.txt"));

        try {
            String result = new CustomTask(sendMsg).execute(id, "loadWaiters").get();
            waiters = result.split("\t");

            if(!result.equals("")) { // 친구 요청이 있는 경우
                // mRequests 리스트에 친구 목록 추가
                for(int i=0; i<waiters.length; i++) {
                    String name = waiters[i];
                    int image = R.drawable.ic_launcher_foreground;
                    mRequests.add(new Profile(image, name));
                }

                // 사전순 정렬
                Collections.sort(mRequests.subList(0, mRequests.size()));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 사전순 정렬
        Collections.sort(mRequests);

        // 어댑터 할당
        MyAdapterRequest adapter = new MyAdapterRequest(mRequests, this);
        binding.rvRequest.setAdapter(adapter);
        binding.rvRequest.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRequest.setHasFixedSize(true);

        RecyclerView.ItemAnimator animator = binding.rvRequest.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        adapter.notifyItemInserted(0);
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
    public void onResume() {
        super.onResume();
        mShowRequest();
    }
}