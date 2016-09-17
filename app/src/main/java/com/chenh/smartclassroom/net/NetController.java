package com.chenh.smartclassroom.net;

import android.os.Handler;
import android.util.Log;

import com.chenh.smartclassroom.util.CurrentStateTool;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by chenh on 2016/9/15.
 */
public class NetController {

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
    public final static int GET_MY_COURSE=1001;
    public final static int GET_MY_COURSE_RESULT=1002;
    public static final String IP_ADDR = "ss.chenhaonee.cn";//服务器地址  这里要改成服务器的ip
    public static final int PORT = 12346;//服务器端口号

    private int maxRetryTimes=5;

    private int retryTimes=0;

    private boolean netState;

    private boolean hasNotifyUser;

    private static Client client;

    private static NetController mNetController;

    public static void createNetController(){
        mNetController=new NetController();
    }
    public static NetController getNetController(){
        if (mNetController==null)
            mNetController=new NetController();
        return mNetController;
    }

    private NetController(){
        controllerStart();
    }

    private void controllerStart(){
        client=new Client();
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

/*    private void netOn(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                tryConnect();
            }
        }).start();
    }*/


    private void tryConnect(){
        Log.e("netError","2333333333333333333333网络被重置");
        try {
            client.setSocket(new Socket(NetController.IP_ADDR, NetController.PORT));
            client.startWorking();
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
        Handler handler= CurrentStateTool.getCurrentHandler();
        handler.sendMessage(handler.obtainMessage(23333,""));
    }
}
