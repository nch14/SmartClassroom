package com.chenh.smartclassroom.view;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalAvailableClassroom;
import com.chenh.smartclassroom.model.LocalClassroom;
import com.chenh.smartclassroom.model.LocalMessage;
import com.chenh.smartclassroom.net.Client;
import com.chenh.smartclassroom.view.login.LoginActivity;

public class WelcomeActivity extends AppCompatActivity {

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                Client.createClient();
                LocalClassroom.getLocalClassroom().requestRefresh();
                //LocalMessage.getLocalMessage();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }).start();
    }
}
