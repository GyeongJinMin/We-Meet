package com.example.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.friend.databinding.CalendarAddBinding;

//2-2 일정 추가
public class Calendar_add extends Fragment {
    private EditText edit_name, edit_memo;
    private Button btn_save, btn_cancel;
    private String name, memo;
    private CalendarAddBinding calendarAddBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calendarAddBinding = CalendarAddBinding.inflate(getLayoutInflater());

        edit_memo = calendarAddBinding.editCmemo;
        edit_name = calendarAddBinding.editCname;
        btn_cancel = calendarAddBinding.btnCancel;
        btn_save = calendarAddBinding.btnSave;

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edit_name.getText().toString().trim();
                memo = edit_memo.getText().toString().trim();
                if(name.getBytes().length <= 0) {//빈값이 넘어올때의 처리

                    Toast.makeText(getContext(), "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra("cname",name);
                    intent.putExtra("cmemo",memo);
//                    getActivity().setResult(Activity.RESULT_OK, data);
 //                   getActivity().finish();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();            }
        });

        return calendarAddBinding.getRoot();
    }

    }