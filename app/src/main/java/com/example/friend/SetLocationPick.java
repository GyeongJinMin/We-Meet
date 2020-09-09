package com.example.friend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.friend.databinding.ActivitySetLocationPickBinding;

public class SetLocationPick extends AppCompatActivity {
    private ActivitySetLocationPickBinding activitySetLocationPickBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySetLocationPickBinding = ActivitySetLocationPickBinding.inflate(getLayoutInflater());
        setContentView(activitySetLocationPickBinding.getRoot());
        activitySetLocationPickBinding.centerLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SetLocationCenter.class);
                startActivity(intent);
            }
        });
    }

    public void OnClickHandler (View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("장소 선택").setMessage("이 곳으로 하시겠습니까?");

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}