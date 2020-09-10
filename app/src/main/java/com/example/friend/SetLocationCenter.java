package com.example.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.friend.databinding.ActivitySetLocationCenterBinding;

import java.util.concurrent.ExecutionException;

public class SetLocationCenter extends AppCompatActivity {
    private ActivitySetLocationCenterBinding activitySetLocationCenterBinding;
    private String location;
    private String sche_id;

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==0)
            location = data.getStringExtra("Location");

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySetLocationCenterBinding = ActivitySetLocationCenterBinding.inflate(getLayoutInflater());
        setContentView(activitySetLocationCenterBinding.getRoot());

        Intent getSchdule = getIntent();
        sche_id = getSchdule.getStringExtra("sche_id");

        activitySetLocationCenterBinding.pickLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activitySetLocationCenterBinding.calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnMain = new Intent();
                Intent intent = new Intent(getApplicationContext(), CalcLocation.class);
                startActivityForResult(intent,0);
                returnMain.putExtra("Location",location);
                setResult(2, returnMain);

                try {
                    String result = new CustomTask().execute(sche_id, location, "setLocation").get();

//                    if(result.equals("done"))
//                        Toast.makeText(SetLocationCenter.this,"success",Toast.LENGTH_SHORT).show();
//                    else
//                        Toast.makeText(SetLocationCenter.this,"fail",Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finish();
            }
        });

    }
/*
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

 */


}