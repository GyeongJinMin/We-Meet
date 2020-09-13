package com.example.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class JoinActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 100;
    EditText registerInputID;
    EditText registerInputPW;
    EditText registerInputName;
    Button registerButton;
    Button inputAddress;
    TextView lat;
    TextView lng;
    private String sendMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        registerInputID = (EditText) findViewById(R.id.registerID);
        registerInputPW = (EditText) findViewById(R.id.registerPW);
        registerInputName =  (EditText) findViewById(R.id.registerName);
        registerButton = (Button) findViewById(R.id.registerButton);
        inputAddress = (Button) findViewById(R.id.inputAddress);
        lat = (TextView) findViewById(R.id.lat);
        lng = (TextView) findViewById(R.id.lng);
        inputAddress.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), AddressWebview.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        registerButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String registerID = registerInputID.getText().toString();
                String registerPW = registerInputPW.getText().toString();
                String registerName = registerInputName.getText().toString();
                String latitude = lat.getText().toString();
                String longitude = lng.getText().toString();
                sendMsg = "join";
                try {
                    String result = new CustomTask(sendMsg).execute(registerID, registerPW, registerName, "join", latitude, longitude).get();

                    if (result.equals("emptyid")) {
                        Toast.makeText(v.getContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                        registerInputID.setText("");
                        registerInputPW.setText("");
                        registerInputName.setText("");
                    } else if (result.equals("emptypw")) {
                        Toast.makeText(v.getContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                        registerInputID.setText("");
                        registerInputPW.setText("");
                        registerInputName.setText("");
                    } else if (result.equals("emptyname")) {
                        Toast.makeText(v.getContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                        registerInputID.setText("");
                        registerInputPW.setText("");
                        registerInputName.setText("");
                    } else if (result.equals("id")) {
                        Toast.makeText(v.getContext(), "이미 있는 아이디입니다.", Toast.LENGTH_SHORT).show();
                        registerInputID.setText("");
                        registerInputPW.setText("");
                        registerInputName.setText("");
                    } else if (result.equals("ok")) {
                        Toast.makeText(v.getContext(), "가입되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }


                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            String mLat = data.getExtras().getString("lat");
            String mLng = data.getExtras().getString("lng");



            System.out.println(mLat);
            System.out.println(mLng);
            lat.setText(mLat);
            lng.setText(mLng);
        }
    }

}