package com.example.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.friend.databinding.CalendarDetailBinding;


//2-1 일정 클릭해서 상세정보 볼 수 있고 수정할 수 있는 창
public class Calender_edit extends AppCompatActivity {
    private EditText edit_name, edit_memo;
    private Button btn_edit;
    private String name, memo;
    private CalendarDetailBinding calendarDetailBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_detail);
//        calendarDetailBinding.inflate(getLayoutInflater());
        edit_name = (EditText) findViewById(R.id.edit_editname);
        edit_memo = (EditText) findViewById(R.id.edit_editmemo);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        Intent intent = getIntent();
        name = intent.getStringExtra("list_name");
        memo = intent.getStringExtra("list_memo");
        edit_name.setText(name);
        edit_memo.setText(memo);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edit_name.getText().toString().trim();
                memo = edit_memo.getText().toString().trim();
                if (name.getBytes().length <= 0) {//빈값이 넘어올때의 처리
                    Toast.makeText(getBaseContext(), "일정 이름을 입력하세요.", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("edit_name", name);
                    intent.putExtra("edit_memo", memo);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

            }
        });
    }
}
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        calendarDetailBinding.inflate(getLayoutInflater());
//        edit_name = calendarDetailBinding.editEditname;
//        edit_memo = calendarDetailBinding.editEditmemo;
//        btn_edit = calendarDetailBinding.btnEdit;
//
//        Intent intent = getActivity().getIntent();
//        name = intent.getStringExtra("list_name");
//        memo = intent.getStringExtra("list_memo");
//
//        edit_name.setText(name);
//        edit_memo.setText(memo);
//
//        btn_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                name = edit_name.getText().toString().trim();
//                memo = edit_memo.getText().toString().trim();
//                if(name.getBytes().length <= 0) {//빈값이 넘어올때의 처리
//
//                    Toast.makeText(getContext(), "일정 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Intent intent = new Intent();
//                    intent.putExtra("edit_name",name);
//                    intent.putExtra("edit_memo",memo);
//                    getActivity().setResult(Activity.RESULT_OK, intent);
//                    getActivity().finish();
//                }
//
//            }
//        });
