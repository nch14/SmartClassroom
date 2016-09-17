package com.chenh.smartclassroom.view.course;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.view.ConfigActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by chenh on 2016/9/16.
 */
public class CourseManageActivity extends ConfigActivity {
    private Switch mSwitch;
    private TextView mID;
    private TextView mPWD;

    private int passwordIsValid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_course_manage);
        super.onCreate(savedInstanceState);

        mSwitch= (Switch) findViewById(R.id.course_switch);
        mID= (TextView) findViewById(R.id.s_key);
        mPWD = (TextView) findViewById(R.id.s_pwd);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_commit) {
            boolean isChecked = mSwitch.isChecked();

            if (isChecked){
                //loading动画

                //检查网络连接
                checkPasswordValid();

                //销毁loading
                while (passwordIsValid==0){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                makeNotification();
            }



            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkPassword(){

    }

    private void makeNotification(){
        String words;
        if (passwordIsValid==1){
            words="成功";
        }else {
            words="失败";
        }
        Toast.makeText(this,words,Toast.LENGTH_SHORT).show();
    }


    private void checkPasswordValid(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String,String> data=new HashMap<>();
                data.put("userName",mID.getText().toString());
                data.put("password",mPWD.getText().toString());

                try {
                    Connection.Response index = Jsoup.connect("http://jw.nju.edu.cn:8080/jiaowu/login.do").data(data).timeout(1000).execute();
                    Document doc = index.parse();
                    Elements lb=doc.select("label");
                    if (lb.size()!=0) {
                        String text = lb.get(0).text();
                        if (text.equals("用户名或密码错误！")){
                            passwordIsValid=2;
                        }
                    }else
                        passwordIsValid=1;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }









/*    private void postJson() {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("userName", mID.getText().toString());
            json.put("pasword", mPWD.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, json.toString());
        //创建一个请求对象
        Request request = new Request.Builder()
                .url("http://jw.nju.edu.cn:8080/jiaowu/login.do")
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                //打印服务端返回结果
                Log.i("MainActivity", response.body().string());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


}
