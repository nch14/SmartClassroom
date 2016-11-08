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

/**
 * Created by chenh on 2016/9/15.
 */
public class NetController {

    public final static int FAIL_LOGIN =0;
    public final static int LOGIN=21001;
    public final static int CONNECT=10001;
    public final static int USER_RESULT=10002;

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
    public final static int GET_MY_COURSE=1001;
    public final static int GET_MY_COURSE_RESULT=1002;
    public final static int GET_USER_COURSE=1101;
    public final static int GET_USER_COURSE_RESULT=1102;
    public final static int INIT_USER_COURSE=1201;
    public final static int REMOVE_USER_COURSE=1202;
    public final static int ADD_USER_COURSE=1103;
    public final static int REFRESH_USER=1301;
    public final static int FORGET_PASSWORD=1401;
    public final static int SUGGESTIONS=1601;

    public static final int PUT_HEAD = 0;
    public static final int GET_HEAD = 1;
    public static final int CHECK_HEAD=2;

//115.159.48.160
    public static final String IP_ADDR = "192.168.1.106";//服务器地址  这里要改成服务器的ip
    public static final int APP_PORT = 12346;//服务器端口号

    public static final int INSTANT_SERVICE_PORT =12347;//图片服务端口

    private int maxRetryTimes=5;

    private int retryTimes=0;

    private boolean netState;

    private boolean hasNotifyUser;

    private static Client client;

    private static NetController mNetController;

    private String userID;
    private String password;
    private String initMessage;

    public static void createNetController(String id,String password){
        mNetController=new NetController(id,password);
    }
    public static NetController getNetController(){
        return mNetController;
    }

    private NetController(String id,String password){
        userID=id;
        this.password=password;
        controllerStart();
    }

    private void controllerStart(){
        client=new Client();
        try {
            initMessage = new JSONObject().put("op",CONNECT).put("id",userID).put("password",password).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //这是一个工作线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if (retryTimes<maxRetryTimes) {
                        //检查网络状况。如果连接过期、就重新连接。
                        if (!netState) {
                            tryConnect();
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        //如果尝试5次连接均失败。则停止尝试连接网络。并通知用户
                        if (!hasNotifyUser){
                            notifyUnableToConnectServer();
                            hasNotifyUser=true;
                        }
                        break;
                    }
                }
            }
        }).start();
    }

    private void tryConnect(){
        Log.e("netError","2333333333333333333333网络被重置");
        try {
            client.setSocket(new Socket(NetController.IP_ADDR, NetController.APP_PORT));
            client.startWorking(initMessage);
            netState=true;
            retryTimes=0;
        } catch (IOException e) {
            retryTimes++;
            networkFailHappens();
            e.printStackTrace();
        }
    }

    public void networkFailHappens(){
        netState=false;
        hasNotifyUser=false;
    }


    public void addTask(String s){
        if (retryTimes>=maxRetryTimes)
            controllerStart();

        client.addTask(s);
    }

    private void notifyUnableToConnectServer(){
        /*Handler handler= CurrentStateTool.getCurrentHandler();
        handler.sendMessage(handler.obtainMessage(23333,""));*/
    }


    /**
     * 不允许在主线程中调用该方法！！！
     * @param s
     * @return
     * @throws IOException
     */
    public static String callInstantNetService(String s) throws IOException {
        Socket socket = new Socket(NetController.IP_ADDR, NetController.INSTANT_SERVICE_PORT);
        DataOutputStream outputStream;
        String writeMessageCache=s;
        outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        outputStream.writeUTF(writeMessageCache);
        outputStream.flush();

        DataInputStream inputStream;
        inputStream =new DataInputStream((socket.getInputStream()));
        String result=inputStream.readUTF();
        socket.close();
        return result;
    }
}
