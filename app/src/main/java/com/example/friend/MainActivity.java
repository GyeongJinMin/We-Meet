package com.example.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.friend.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Fragment {

    ArrayList<Profile> mProfiles = new ArrayList<>();
    ActivityMainBinding activityMainBinding;
    Intent intent;
    private String[] friends;
    private String id;
    private String sendMsg, userName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        activityMainBinding.friendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), FriendSearchActivity.class);
                startActivity(intent);
            }
        });
        activityMainBinding.friendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), FriendRequestActivity.class);
                startActivity(intent);
            }
        });



        return activityMainBinding.getRoot();
    }

    public void mShowProfile() { // 프로필 보여주기
        int image;

        readData(new File(getContext().getFilesDir(), "id.txt"));

        // mProfiles 초기화
        mProfiles.clear();

        // 내 프로필
        mProfiles.add(new Profile(0, "내 프로필")); // 헤더

        sendMsg = "loadUser";
        try {
            userName = new CustomTask(sendMsg).execute(id, "loadUser").get();

            mProfiles.add(new Profile(R.drawable.ic_launcher_foreground, userName));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 친구
        sendMsg = "loadFriends";
        try {
            String result = new CustomTask(sendMsg).execute(id, "loadFriends").get();

            if(!result.equals("")) { // 친구가 있는 경우
                mProfiles.add(new Profile(0, "친구")); // 헤더

                friends = result.split("\t");
                image = R.drawable.ic_launcher_foreground;

                // 친구가 없는 경우
                // mProfiles 리스트에 친구 목록 추가
                for (int i = 0; i < friends.length; i++) {
                    String name = friends[i];
                    int img = image;
                    mProfiles.add(new Profile(img, name));
                }

                // 사전순 정렬
                Collections.sort(mProfiles.subList(3, mProfiles.size()));
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 어댑터 할당
        MyAdapter adapter = new MyAdapter(mProfiles, getContext());
        activityMainBinding.rvProfile.setAdapter(adapter);
        activityMainBinding.rvProfile.setLayoutManager(new LinearLayoutManager(getContext()));
        activityMainBinding.rvProfile.setHasFixedSize(true);
        adapter.notifyDataSetChanged(); // adapter 갱신
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
        mShowProfile();
    }
}

