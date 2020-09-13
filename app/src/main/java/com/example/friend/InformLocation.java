package com.example.friend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.friend.databinding.ActivityInformLocationBinding;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public class InformLocation extends AppCompatActivity {
    private ActivityInformLocationBinding activityInformLocationBinding;
    private String[] position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityInformLocationBinding = ActivityInformLocationBinding.inflate(getLayoutInflater());
        setContentView(activityInformLocationBinding.getRoot());

        Intent getmain = getIntent();
        String sche_id = getmain.getStringExtra("sche_id");
        Log.i("sche_id",sche_id);

        try {
            String result = new CustomTask().execute(sche_id,"loadPosition").get();
            Log.i("result",result);
            position = result.split("\t");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String url = "kakaomap://search?q=맛집&p="+position[0]+","+position[1];
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        startActivity(intent);

        Intent intent = null;
        try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
        if (existPackage != null)
            startActivity(intent);

        activityInformLocationBinding.mapLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),InformLocationMap.class);
                startActivity(intent);
                finish();
            }
        });
    }
}