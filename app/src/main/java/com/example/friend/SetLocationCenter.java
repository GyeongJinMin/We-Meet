package com.example.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.friend.databinding.ActivitySetLocationCenterBinding;

public class SetLocationCenter extends Fragment {
    private ActivitySetLocationCenterBinding activitySetLocationCenterBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activitySetLocationCenterBinding = ActivitySetLocationCenterBinding.inflate(getLayoutInflater());

        activitySetLocationCenterBinding.pickLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        activitySetLocationCenterBinding.calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CalcLocation.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return activitySetLocationCenterBinding.getRoot();
    }


}