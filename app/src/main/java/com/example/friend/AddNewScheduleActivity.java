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

import com.example.friend.databinding.ActivityAddNewScheduleBinding;

import java.util.ArrayList;


public class AddNewScheduleActivity extends Fragment {
    private ActivityAddNewScheduleBinding activityAddNewScheduleBinding;
    //private RecyclerView member_list;
    private MemberAdapter memberAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activityAddNewScheduleBinding = ActivityAddNewScheduleBinding.inflate(getLayoutInflater());
        member_init();

        activityAddNewScheduleBinding.finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScheduleMainHome.class);
                startActivity(intent);
                //finish();
            }
        });
        return activityAddNewScheduleBinding.getRoot();
    }


    private void member_init(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        activityAddNewScheduleBinding.memberView.setLayoutManager(layoutManager);

        ArrayList<Person> member = new ArrayList<>();
        member.add(new Person("이수연"));
        member.add(new Person("이규영"));
        member.add(new Person("민경진"));
        member.add(new Person("최지호"));

        memberAdapter = new MemberAdapter(getContext(), member, onClickItem);
        activityAddNewScheduleBinding.memberView.setAdapter(memberAdapter);

    }

    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tag = (String) v.getTag();
            Toast.makeText(getContext(), tag, Toast.LENGTH_SHORT).show();
        }
    };
}
