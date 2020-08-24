package com.example.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.friend.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends Fragment {

    ArrayList<Profile> mProfiles = new ArrayList<>();
    ActivityMainBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        mShowProfile();

        return binding.getRoot();
    }

    public void mShowProfile() { // 프로필 보여주기
        // 여기서부터 ------------------------------------------------------------------------------
        // 내 프로필
        mProfiles.add(new Profile(0,"내 프로필"));
        mProfiles.add(new Profile(R.drawable.ic_launcher_foreground,"민경진"));

        Random r = new Random();
        int num = r.nextInt(1); // 랜덤 숫자 받아오기 0~1

        if (num%2 == 0) {

            // 친구
            mProfiles.add(new Profile(0,"친구"));

            String[] friends = {"이수연", "이규영", "최지호", "허예원", "박서연", "오아람", "홍승현"};
            int[] image = {R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground,
                    R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground};

            // mProfiles 리스트에 친구 목록 추가
            for(int i=0; i<friends.length; i++) {
                String name = friends[i];
                int img = image[i];
                mProfiles.add(new Profile(img, name));
            }

            // 사전순 정렬
            Collections.sort(mProfiles.subList(3, mProfiles.size()));
        }
        // 여기까지 수정----------------------------------------------------------------------------
        // <친구> 화면에 보여줄 본인, 친구목록

        // 어댑터 할당
        MyAdapter adapter = new MyAdapter(mProfiles, getContext());
        binding.rvProfile.setAdapter(adapter);
        binding.rvProfile.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvProfile.setHasFixedSize(true);

        adapter.notifyItemInserted(0);
    }

    public void mOnClick(View v) { // 친구 찾기, 친구 요청 목록
        Intent intent;

        switch(v.getId()) {
            case R.id.friendSearch:
                intent = new Intent(getContext(), FriendSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.friendRequest:
                intent = new Intent(getContext(), FriendRequestActivity.class);
                startActivity(intent);
                break;
        }
    }
}