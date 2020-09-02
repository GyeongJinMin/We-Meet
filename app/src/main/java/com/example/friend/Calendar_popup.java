package com.example.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


//2 캘린더에서 날짜 클릭했을 때 뜨는 팝업창
public class Calendar_popup extends Activity {
    private int year, month, day;
    private TextView mText_date;
    private Button mBtn_add;
    private String add_name, add_memo, id;
    private ListView mListview;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> cname;
    private ArrayList<String> cmemo;
    private String calendar_db, date;
    private int edit_num;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //팝업으로 띄우려면 manifest에서 theme 설정해야됨
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.calendar_list2);
        mText_date = (TextView)findViewById(R.id.text_date);
        mBtn_add = (Button)findViewById(R.id.btn_add);
        mListview = (ListView) findViewById(R.id.listview);
        Intent intent = getIntent();
        year = intent.getIntExtra("year",0);
        month = intent.getIntExtra("month", 0);
        month = month+1;
        day = intent.getIntExtra("day",0);
        date = year + "-" + month + "-" + day;

        readData(new File(getFilesDir(), "id.txt"));

        //db 연결
        try {
            calendar_db = new CustomTask().execute(id, date, "", "", "calendar_main").get();
            cname = new ArrayList<>();
            cmemo = new ArrayList<>();
            if (calendar_db.getBytes().length > 0) {
                String calendar_split[] = calendar_db.split(","); // date,schedule,memo, 이런식으로되어있음
                for (int i = 0; i < calendar_split.length; i = i + 3) { //나누기
                    cname.add(calendar_split[i + 1]);
                    cmemo.add(calendar_split[i + 2]);
                }
            }
        }catch (Exception e) { }

        adapter = new ArrayAdapter<String>(this,
                R.layout.calendar_recycler, cname);
        mListview.setAdapter(adapter);

        //일정 수정
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                edit_num = position;
                Intent intent = new Intent(getApplicationContext(), Calender_edit.class);
                intent.putExtra("list_name", cname.get(position)); //서버 만들고 수정 필요함
                intent.putExtra("list_memo", cmemo.get(position));
                startActivityForResult(intent, 2);

            }
        });


        mText_date.setText(String.format("%d/%d/%d", year,month, day));
        mBtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //일정 추가 버튼 클릭 시
                Intent intent = new Intent(getApplicationContext(), Calendar_add.class);
                startActivityForResult(intent, 1);

            }
        });

    }

    //일정 추가 후 결과 받아옴
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode==RESULT_OK) {
            add_name = data.getStringExtra("cname");
            add_memo = data.getStringExtra("cmemo");

            try {
                calendar_db = new CustomTask().execute(id, date, add_name, add_memo, "calendar_add").get();
                Toast.makeText(this,calendar_db ,Toast.LENGTH_SHORT).show();
                if (calendar_db.equals("false")) {
                    Toast.makeText(this,"이미 존재하는 일정입니다." ,Toast.LENGTH_SHORT).show();
                }
                else if (calendar_db.equals("done")) {
                    cname.add(add_name);
                    cmemo.add(add_memo);
                }
            }catch (Exception e) { }

            Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
        else if(requestCode == 2 && resultCode == RESULT_OK) {
            add_name = data.getStringExtra("edit_name");
            add_memo = data.getStringExtra("edit_memo");

            try {
                calendar_db = new CustomTask().execute(id, date, add_name, add_memo, cname.get(edit_num), "calendar_edit").get();
                Toast.makeText(this,calendar_db ,Toast.LENGTH_SHORT).show();
                if (calendar_db.equals("false")) {
                    Toast.makeText(this,"이미 존재하는 일정입니다." ,Toast.LENGTH_SHORT).show();
                }
                else if (calendar_db.equals("done")) {
                    cname.set(edit_num, add_name);
                    cmemo.set(edit_num, add_memo);
                }
            }catch (Exception e) { }

            Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();

        }
    }

    void readData(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            id = new String(data);
            fis.close();
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }


}