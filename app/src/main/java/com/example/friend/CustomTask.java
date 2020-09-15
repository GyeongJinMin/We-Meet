package com.example.friend;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class CustomTask extends AsyncTask<String, Void, String> {
    String sendMsg, receiveMsg;

    CustomTask() {
        this.sendMsg = "";
    }

    CustomTask(String sendMsg) {
        this.sendMsg = sendMsg;
    }


    @Override
    protected String doInBackground(String... strings) {
        try {
            String str;
            URL url = new URL("http://172.30.1.18:8080/server/DBserver.jsp"); //수연
            //URL url = new URL("http://192.168.0.7:8080/project_Server/DB.jsp"); //규영
            //URL url = new URL("http://192.168.123.105:8080/server/DBserver.jsp");// 경진
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            if(sendMsg.equals("loadUser")||sendMsg.equals("loadOthers")||sendMsg.equals("loadFriends")||sendMsg.equals("loadWaiters")) {
                sendMsg = "id="+strings[0] + "&type="+strings[1];
            }
            else if(sendMsg.equals("loadAllUsers")) {
                sendMsg = "&type="+strings[0];
            }
            else if(sendMsg.equals("friendAccept")||sendMsg.equals("friendReject")||sendMsg.equals("friendRequest")) {
                sendMsg = "id=" + strings[0] + "&friendName=" + strings[1] + "&type=" + strings[2];
            }
            else if(sendMsg.equals("loadParticipants")) {
                sendMsg = "sche_id=" + strings[0] + "&type=" + strings[1];
            }
            else if(sendMsg.equals("addParticipants")) {
                sendMsg = "sche_id=" + strings[0] + "&friendName=" + strings[1] + "&type=" + strings[2];
            }
            else if (sendMsg.equals("join") ) {
                sendMsg = "id="+strings[0]+"&pwd="+strings[1]+"&name="+strings[2]+"&type="+strings[3]+"&latitude="+strings[4]+"&longitude="+strings[5];
            }
            else if (sendMsg.equals("savePoint")) {
                //sche_id, mapPointx, mapPointy, placename, sendMsg
                sendMsg = "sche_id="+strings[0]+"&latitude="+strings[1]+"&longitude="+strings[2]+"&location="+strings[3]+"&type=" + strings[4];
            }
            else if(strings.length==1){ //load All Schedule
                sendMsg = "type="+strings[0];
            }
            else if(strings.length==2) { //load Schedule
                if(strings[1].equals("loadSche"))
                    sendMsg = "sche_id="+strings[0]+"&type="+strings[1];
                if(strings[1].equals("initVoteLocation"))
                    sendMsg = "sche_id="+strings[0]+"&type="+strings[1];
                if(strings[1].equals("initVoteDate"))
                    sendMsg = "sche_id="+strings[0]+"&type="+strings[1];
                if(strings[1].equals("loadPosition"))
                    sendMsg = "sche_id="+strings[0]+"&type="+strings[1];
            }
            else if(strings.length==3){ //add Schedule
                if(strings[2].equals("setDate"))
                    sendMsg = "sche_id="+strings[0]+"&sche_date="+strings[1]+"&type="+strings[2];
                if(strings[2].equals("setLocation"))
                    sendMsg = "sche_id="+strings[0]+"&location="+strings[1]+"&type="+strings[2];
                if(strings[2].equals("modiSche"))
                    sendMsg = "sche_id="+strings[0]+"&new_sche_name="+strings[1]+"&type="+strings[2];
                if(strings[2].equals("addVote"))
                    sendMsg = "sche_id="+strings[0]+"&sche_name="+strings[1]+"&type="+strings[2];
                if(strings[2].equals("modiVote"))
                    sendMsg = "sche_id="+strings[0]+"&sche_name="+strings[1]+"&type="+strings[2];
                if(strings[2].equals("setVoteDate"))
                    sendMsg = "sche_id="+strings[0]+"&date="+strings[1]+"&type="+strings[2];
                if(strings[2].equals("setVoteLocation"))
                    sendMsg = "sche_id="+strings[0]+"&location="+strings[1]+"&type="+strings[2];
                if(strings[2].equals("voteDate"))
                    sendMsg = "sche_id="+strings[0]+"&sign="+strings[1]+"&type="+strings[2];
                if(strings[2].equals("voteLocation"))
                    sendMsg = "sche_id="+strings[0]+"&sign="+strings[1]+"&type="+strings[2];
            }
            else if(strings.length==4) {
                if(strings[3].equals("addSche"))
                    sendMsg = "id="+strings[0]+"&sche_name="+strings[1]+"&participants="+strings[2]+"&type="+strings[3];
                if(strings[3].equals("login"))
                    sendMsg = "id="+strings[0]+"&pwd="+strings[1]+"&name="+strings[2]+"&type="+strings[3];
            }
            else if (strings.length <6) {
                sendMsg = "id=" + strings[0] + "&date=" + strings[1] + "&schedule=" + strings[2] +
                        "&memo=" + strings[3] + "&type=" + strings[4];
            }
            else  {
                sendMsg = "id=" + strings[0] + "&date=" + strings[1] + "&schedule=" + strings[2] +
                        "&memo=" + strings[3] + "&old=" + strings[4] + "&type=" + strings[5];
            }
            osw.write(sendMsg);
            osw.flush();
            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            } else {
                Log.i("통신 결과", conn.getResponseCode()+"에러");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }
}