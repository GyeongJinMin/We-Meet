package com.example.friend;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.friend.databinding.ActivitySetLocationPickBinding;

import java.util.concurrent.ExecutionException;

public class SetLocationPick extends AppCompatActivity {
    private ActivitySetLocationPickBinding activitySetLocationPickBinding;
    private String location;
    private String sche_id;

//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode==0)
//            location = data.getStringExtra("Location");
//
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySetLocationPickBinding = ActivitySetLocationPickBinding.inflate(getLayoutInflater());
        setContentView(activitySetLocationPickBinding.getRoot());

        Intent getSchdule = getIntent();
        sche_id = getSchdule.getStringExtra("sche_id");

        activitySetLocationPickBinding.centerLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SetLocationCenter.class);
                intent.putExtra("sche_id",sche_id);
                startActivity(intent);
            }
        });

        activitySetLocationPickBinding.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SetLocationPick.this);
                builder.setTitle("장소 선택").setMessage("이 곳으로 하시겠습니까?");

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("Location", "여기");
                        location = "여기";
                        setResult(2, intent);

                        try {
                            String result = new CustomTask().execute(sche_id, location, "setLocation").get();
                            String res_vote = new CustomTask().execute(sche_id, location, "setVoteLocation").get();

//                            if(result.equals("done"))
//                                Toast.makeText(SetLocationPick.this,"success",Toast.LENGTH_SHORT).show();
//                            else
//                                Toast.makeText(SetLocationPick.this,"fail",Toast.LENGTH_SHORT).show();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        finish();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

//    public void OnClickHandler (View view)
//    {
//
//    }
}