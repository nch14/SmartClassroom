package com.chenh.smartclassroom.net;

import com.chenh.smartclassroom.model.LocalAvailableClassroom;
import com.chenh.smartclassroom.model.LocalClassroom;
import com.chenh.smartclassroom.model.LocalComment;
import com.chenh.smartclassroom.model.LocalMessage;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.util.json.JsonUtil;
import com.chenh.smartclassroom.vo.AttitudeVO;
import com.chenh.smartclassroom.vo.BlogComments;
import com.chenh.smartclassroom.vo.BlogMessage;
import com.chenh.smartclassroom.vo.Classroom;
import com.chenh.smartclassroom.vo.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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

    public final static int FAIL_LOGIN =0;

    public final static int LOGIN=1;
    public final static int LOGIN_RESULT=2;

    public final static int SHOW_OPEN_CLASSES=3;
    public final static int SHOW_OPEN_CLASSES_RESULT=4;

    public final static int LOAD_BLOG_MESSAGE=5;
    public final static int LOAD_BLOG_MESSAGE_RESULT=6;

    public final static int CREATE_BLOG_MESSAGE=7;
    public final static int CREATE_BLOG_MESSAGE_RESULT=8;

    public final static int LIKE_BLOG_MESSAGE=9;

    public final static int CANCEL_BLOG_MESSAGE =10;

    public final static int ATTITUDE_BLOG_RESULT=11;

    public final static int REQUEST_BLOG_COMMENT=12;
    public final static int REQUEST_BLOG_COMMENT_RESULT=13;

    public final static int COMMENT_BLOG=14;
    public final static int COMMENT_BLOGRESULT=15;

    public final static int ASK_FOR_AVAILABLE_CLASSROOM=16;
    public final static int ASK_FOR_AVAILABLE_CLASSROOM_RESULT=17;



    public static final String IP_ADDR = "192.168.1.101";//服务器地址  这里要改成服务器的ip
    public static final int PORT = 12346;//服务器端口号
    private static ArrayList<String> messages;
    private Socket socket;

    private boolean netCannotConnect;

    private static Client client;


    private boolean exception;


    public static void createClient(){
        client=new Client();
    }
    public static Client getClient(){
        return client;
    }

    private Client(){
        messages=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(IP_ADDR, PORT);
                    startWorking();
                } catch (IOException e) {
                    e.printStackTrace();
                    netCannotConnect=true;
                }
            }
        }).start();
    }

    protected void write() throws IOException {
        DataOutputStream outputStream;
        String message=messages.get(0);
        messages.remove(0);
        outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        outputStream.writeUTF(message);
        outputStream.flush();
    }

    protected void read() throws IOException {
        DataInputStream inputStream;
        inputStream =new DataInputStream((socket.getInputStream()));
        String s=inputStream.readUTF();
        handleMessage(s);
    }

    private void handleMessage(String s) {
        try {
            JSONObject json = new JSONObject(s);
            int op=json.getInt("rp");
            switch (op) {
                case LOGIN_RESULT:
                    loginCallBack(json,true);
                    break;
                case SHOW_OPEN_CLASSES_RESULT:
                    doOpenClass(json);
                    break;
                case FAIL_LOGIN:
                    loginCallBack(json,false);
                    break;
                case LOAD_BLOG_MESSAGE_RESULT:
                    doLoadBlogMessage(json);
                    break;
                case ATTITUDE_BLOG_RESULT:
                    doAttitudeResult(json);
                    break;
                case REQUEST_BLOG_COMMENT_RESULT:
                    doLoadBlogComment(json);
                    break;
                case ASK_FOR_AVAILABLE_CLASSROOM_RESULT:
                    doProcessAvailableClassroom(json);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void startWorking() {
        //有没有要读的。要读就读一下
        new Thread(new Runnable() {
            public void run() {
                while (true){
                    try {
                        read();
                    } catch (IOException e) {
                        e.printStackTrace();
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
                            handleException();
                            break;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        handleException();
                    }
                }
            }
        }).start();


    }

    public void addMessage(String s){
        messages.add(s);
    }




    private void loginCallBack(JSONObject json,boolean success){
        if (success){
            User user= JsonUtil.getUser(json);
            LocalUser.getLocalUser().loginCallBack(user);
        }else {
            try {
                String message=json.getString("message");
                LocalUser.getLocalUser().loginCallBack(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void doOpenClass(JSONObject json){

        ArrayList<Classroom> classrooms=new ArrayList<>();
        try {
            int num=json.getInt("num");
            for (int i=0;i<num;i++){
                JSONObject jsonObject=json.getJSONObject(""+i);
                classrooms.add(JsonUtil.getClassroom(jsonObject));
            }
            LocalClassroom.getLocalClassroom().refreshClassroom(classrooms);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void doLoadBlogMessage(JSONObject json){

        ArrayList<BlogMessage> blogMessages =new ArrayList<>();
        try {
            int num=json.getInt("num");
            for (int i=0;i<num;i++){
                JSONObject jsonObject=json.getJSONObject(""+i);
                blogMessages.add(JsonUtil.getBlogMessage(jsonObject));
            }
            LocalMessage.getLocalMessage().addBlogMessages(blogMessages);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doAttitudeResult(JSONObject json){
        ArrayList<AttitudeVO> attitudeVOs =new ArrayList<>();
        try {
            int num=json.getInt("num");
            long sheetId=json.getLong("sheetId");
            for (int i=0;i<num;i++){
                JSONObject jsonObject=json.getJSONObject(""+i);
                attitudeVOs.add(JsonUtil.getAtttitude(jsonObject));
            }
            LocalMessage.getLocalMessage().addAttitudes(sheetId,attitudeVOs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doLoadBlogComment(JSONObject json){
        ArrayList<BlogComments> blogCommentses =new ArrayList<>();
        try {
            long sheetId=json.getLong("sheetId");
            int num=json.getInt("num");
            for (int i=0;i<num;i++){
                JSONObject jsonObject=json.getJSONObject(""+i);
                blogCommentses.add(JsonUtil.getBlogComments(jsonObject));
            }
            LocalComment.getLocalComment().addComments(sheetId,blogCommentses);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doProcessAvailableClassroom(JSONObject json){
        ArrayList<String> arrayList=new ArrayList<>();
        try {
            int num=json.getInt("num");
            for (int i=0;i<num;i++){
                String s=json.getString(""+i);
                arrayList.add(s);
            }
            LocalAvailableClassroom.getLocalClassroom().addClassroom(arrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void handleException(){
        if (exception)
            return;
        exception=true;
        client=new Client();
    }

    private void retryConnect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(IP_ADDR, PORT);
                    startWorking();
                } catch (IOException e) {
                    e.printStackTrace();
                    netCannotConnect=true;
                }
            }
        }).start();
    }
}
