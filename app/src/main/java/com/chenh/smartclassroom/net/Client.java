package com.chenh.smartclassroom.net;

import android.os.Handler;
import android.util.Log;

import com.chenh.smartclassroom.util.CurrentStateTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by chenh on 2016/8/1.
 */
public class Client {

    private ArrayList<String> messages;

    /**
     * 和服务器端的管道入口
     */
    private Socket socket;

    public Client(){
        messages=new ArrayList<>();
    }

    private void write() throws IOException {
        DataOutputStream outputStream;
        String writeMessageCache=messages.get(0);
        messages.remove(0);
        outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        outputStream.writeUTF(writeMessageCache);
        outputStream.flush();
    }

    private void read() throws IOException {
        DataInputStream inputStream;
        inputStream =new DataInputStream((socket.getInputStream()));
        String s=inputStream.readUTF();
        Log.e("read",s);
        handleMessage(s);
    }



    private void handleMessage(String s) {
        try {
            JSONObject json = new JSONObject(s);
            int op=json.getInt("rp");
            switch (op) {
                case NetController.USER_RESULT:
                    ClientCallBackProcess.getUserBack(json);
                    break;
                case NetController.SHOW_OPEN_CLASSES_RESULT:
                    ClientCallBackProcess.doOpenClass(json);
                    break;
               /* case NetController.FAIL_LOGIN:
                    ClientCallBackProcess.loginCallBack(json,false);
                    break;*/
                case NetController.LOAD_BLOG_MESSAGE_RESULT:
                    ClientCallBackProcess.doLoadBlogMessage(json);
                    break;
                case NetController.ATTITUDE_BLOG_RESULT:
                    ClientCallBackProcess.doAttitudeResult(json);
                    break;
                case NetController.REQUEST_BLOG_COMMENT_RESULT:
                    ClientCallBackProcess.doLoadBlogComment(json);
                    break;
                case NetController.ASK_FOR_AVAILABLE_CLASSROOM_RESULT:
                    ClientCallBackProcess.doProcessAvailableClassroom(json);
                    break;
                case NetController.GET_MY_COURSE_RESULT:
                    ClientCallBackProcess.doGetMyCourse(json);
                    break;
                case NetController.GET_USER_COURSE_RESULT:
                    ClientCallBackProcess.doGetUserCourse(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startWorking(String initTask) {
        messages.add(initTask);
        //有没有要读的。要读就读一下
        new Thread(new Runnable() {
            public void run() {
                while (true){
                    try {
                        read();
                    } catch (IOException e) {
                        e.printStackTrace();
                        handleException();
                        break;
                    }
                }
            }
        }).start();
        //有没有要写的。要写就写一下
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (messages.size()!=0) {
                        try {
                            write();
                        } catch (IOException e) {
                            e.printStackTrace();
                            //messages.add(0,writeMessageCache);
                            handleException();
                            break;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void addTask(String s){
        messages.add(s);
    }

    private void handleException(){
        NetController.getNetController().networkFailHappens();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
