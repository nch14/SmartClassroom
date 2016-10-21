package com.chenh.smartclassroom.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalClassroom;
import com.chenh.smartclassroom.model.LocalCourse;
import com.chenh.smartclassroom.model.LocalMessage;
import com.chenh.smartclassroom.net.Client;
import com.chenh.smartclassroom.net.NetController;
import com.chenh.smartclassroom.util.CurrentStateTool;
import com.chenh.smartclassroom.view.login.LoginActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class WelcomeActivity extends AppCompatActivity {
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int what=msg.what;
                String words;
                switch (what){
                    case 23333:
                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            words="服务器好像又宕机了";
                        } else {
                            words="请检查您的网络";
                        }
                        Toast.makeText(WelcomeActivity.this,words,Toast.LENGTH_SHORT).show();
                }
            }
        };
        CurrentStateTool.setCurrentHandler(mHandler);


        systemInit();
        new Thread(new Runnable() {
            @Override
            public void run() {

                //LocalClassroom.getLocalClassroom().requestRefresh();
                //LocalMessage.getLocalMessage();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
/*                String[] s = loadUser();
                Intent intent;
                if (s!=null){
                    try {
                        new FutureTask<>(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return null;
                            }
                        }).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    intent=new Intent(WelcomeActivity.this, ContentActivity.class);
                }else {
                    intent=new Intent(WelcomeActivity.this, LoginActivity.class);
                }*/
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }).start();
    }


    private void systemInit(){
        //打开网络，连接服务器
        NetController.createNetController();
    }


    private String[] loadUser(){
        File filesDir=getFilesDir();
        File todoFile=new File(filesDir,"user.txt");
        ArrayList<String> items;
        try {
            items=new ArrayList<String>(FileUtils.readLines(todoFile));
        }catch (IOException e){
            items=new ArrayList<>();
        }
        if (items.size()!=0){
            return new String[]{items.get(0),items.get(1)};
        }
        return null;
    }

}
