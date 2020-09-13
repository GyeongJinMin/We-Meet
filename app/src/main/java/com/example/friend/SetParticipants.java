package com.example.friend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.friend.databinding.ActivityFriendSearchBinding;
import com.example.friend.databinding.ActivitySetParticipantsBinding;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SetParticipants extends AppCompatActivity {

    ArrayList<Profile> list = new ArrayList<>(); // 복사본(전체 데이터, 입력된 텍스트와 비교할 리스트)
    ArrayList<Profile> mFriends = new ArrayList<>(); // 검색 결과(띄워주는 리스트)
    private ActivitySetParticipantsBinding activitySetParticipantsBinding;
    ParticipantsAdapter adapter;
    private String[] friendName;
    private String id, sche_id, sche_name, sendMsg, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySetParticipantsBinding = ActivitySetParticipantsBinding.inflate(getLayoutInflater());
        setContentView(activitySetParticipantsBinding.getRoot());

        Intent getSchdule = getIntent();
        sche_id = getSchdule.getStringExtra("sche_id");
        message = getSchdule.getStringExtra("message");

        readData(new File(getFilesDir(), "id.txt"));
    }

    public void mShowFriends() { // 검색 결과 보여주기
        setting(); // 리스트에 전체 목록(등록된 모든 사용자) 추가

        list.addAll(mFriends); // 전체 목록 복사본

        // 어댑터 할당
        adapter = new ParticipantsAdapter(mFriends, sche_id, message, this);
        activitySetParticipantsBinding.rvFriend.setAdapter(adapter);
        activitySetParticipantsBinding.rvFriend.setLayoutManager(new LinearLayoutManager(this));
        activitySetParticipantsBinding.rvFriend.setHasFixedSize(true);

        RecyclerView.ItemAnimator animator = activitySetParticipantsBinding.rvFriend.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        activitySetParticipantsBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { // 입력하기 전 처리

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { // 변화와 동시에 처리

            }

            @Override
            public void afterTextChanged(Editable s) { // 입력이 끝났을 때 처리
                String text = activitySetParticipantsBinding.etSearch.getText().toString();
                search(text);
            }
        });

        activitySetParticipantsBinding.etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER: // Enter키 눌렀을 때
                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(activitySetParticipantsBinding.etSearch.getWindowToken(), 0); // 키보드 숨기기
                            return true;
                    }
                }
                return false;
            }
        });
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack: // 뒤로가기 버튼 눌렀을 때
                finish();
                break;
            case R.id.imgClear: // x 버튼 눌렀을 때
                activitySetParticipantsBinding.etSearch.setText(""); // editText 초기화
                break;
            case R.id.ok: // 확인 버튼 눌렀을 때
                if(message.equals("newSchedule")) {
                    String participants = ((ParticipantsAdapter)adapter).getTemp();

                    Intent intent = new Intent();
                    intent.putExtra("Participants", participants);
                    setResult(3, intent);
                }
                else {
                    sendMsg = "addParticipants";

                    try {
                        String participants = ((ParticipantsAdapter)adapter).getTemp();

                        String result = new CustomTask(sendMsg).execute(sche_id, participants, "addParticipants").get();

                        Intent intent = new Intent();
                        intent.putExtra("Participants", participants);
                        setResult(3, intent);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                finish();
                break;
        }
    }

    public void search(String text) {

        mFriends.clear(); // mFriends 초기화

        if (text.length() == 0) { // 입력이 없을때
            mFriends.addAll(list); // mFriends 전체 목록 추가
        }
        else {
            for(int i = 0;i < list.size(); i++) {
                if (list.get(i).getName().toLowerCase().contains(text)) { // 데이터(list)에 text가 포함된 경우
                    mFriends.add(list.get(i)); // mFriends 추가
                }
            }
        }

        adapter.notifyDataSetChanged(); // adapter 갱신
    }

    public void setting() { // 친구 목록 불러오기
        sendMsg = "loadFriends";

        try {
            String result = new CustomTask(sendMsg).execute(id, "loadFriends").get();
            friendName = result.split("\t");

            // mFriends 리스트에 사용자 목록 추가
            for(int i=0; i<friendName.length; i++) {
                String name = friendName[i];
                mFriends.add(new Profile(name));
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 사전순 정렬
        Collections.sort(mFriends);
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
        mShowFriends();
    }
}