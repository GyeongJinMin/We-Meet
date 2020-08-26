package com.example.friend;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends Activity {
    EditText userID, userPW;
    Button login, join;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.friend", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } // 카카오 api 키 해시 구하는 과정
        userID = (EditText)findViewById(R.id.userID);
        userPW = (EditText)findViewById(R.id.userPW);
        login = (Button)findViewById(R.id.login);
        join = (Button)findViewById(R.id.join);
    }



    public void mOnClick(View view){
        switch (view.getId()) {
            case R.id.login : // 로그인 버튼 눌렀을 경우
                String loginid = userID.getText().toString();
                String loginpwd = userPW.getText().toString();

                try {
                    String result  = new CustomTask().execute(loginid, loginpwd, "name", "login").get();
                    if(result.equals("true")) {
                        Toast.makeText(this,"로그인",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, TabActivity.class);
                        startActivity(intent);
                        finish();
                    } else if(result.equals("false")) {
                        Toast.makeText(this,"아이디 또는 비밀번호가 틀렸음",Toast.LENGTH_SHORT).show();
                        userID.setText("");
                        userPW.setText("");
                    } else if(result.equals("noId")) {
                        Toast.makeText(this,"존재하지 않는 아이디",Toast.LENGTH_SHORT).show();
                        userID.setText("");
                        userPW.setText("");
                    }
                }catch (Exception e) {}
                break;
            case R.id.join : // 회원가입
                Intent intent = new Intent(this, JoinActivity.class);
                startActivity(intent);
                finish();
        }
    }

}