package com.chenh.smartclassroom.view.login;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.net.NetController;
import com.chenh.smartclassroom.util.json.JsonUtil;
import com.chenh.smartclassroom.view.ContentActivity;
import com.chenh.smartclassroom.view.common.LoadingDiaolog;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText userNameView;
    private EditText passwordView;

    private Button loginButton;

    private TextView sendPassword;

    private LoadingDiaolog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showLogin();

        //如果存在这样一个auto字符串，说明此处为注销登陆后返回登陆界面。无需执行自动登陆选项。
        String auto=getIntent().getStringExtra("auto");
        if (auto==null) {
            autoLogin();
        }
    }

    private void showLogin(){
        userNameView= (EditText) findViewById(R.id.input_id);
        passwordView= (EditText) findViewById(R.id.input_password);

        loginButton= (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new DoLogin());

        sendPassword = (TextView) findViewById(R.id.link_signup);
        sendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean idValid = checkId();
                if (idValid){
                    JSONObject message=new JSONObject();
                    try {
                        message.put("op",NetController.FORGET_PASSWORD);
                        message.put("id",userNameView.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    NetController.getNetController().addTask(message.toString());

                    Toast.makeText(LoginActivity.this,"您的密码已被发送至您的学校邮箱。请查收",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LoginActivity.this,"你用不存在的账号骗我！我不上当！",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private class DoLogin implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            boolean checkId=checkId();
            boolean checkPwd=checkPassword();
            if (checkId&&checkPwd){
                login();
            }
        }
    }


    private boolean checkId(){
        String s=userNameView.getText().toString();
        if (s==null){
            userNameView.setError("账号不能为空");
            return false;
        }
        if (s.length()!=9) {
            userNameView.setError("位数不正确");
            return false;
        }
        return true;
    }

    private boolean checkPassword(){
        String s=passwordView.getText().toString();
        if (s==null){
            passwordView.setError("密码不能为空");
            return false;
        }
        return true;
    }

    private void showLoadingDialog(){
        FragmentManager fm=getFragmentManager();
        dialog= new LoadingDiaolog();
        dialog.show(fm,"");
    }

    private void hideLoadingDialog(){
        if (dialog!=null)
            dialog.dismiss();
    }

    private void autoLogin(){
        String[] keys=loadUser();
        if (keys!=null){
            userNameView.setText(keys[0]);
            passwordView.setText(keys[1]);
            login();
        }
    }

    private void login(){
        loginButton.requestFocus();
        showLoadingDialog();

       new LoginAsyncTask().execute(new String[]{userNameView.getText().toString(),passwordView.getText().toString()});
    }

    private String[] loadUser(){
        File filesDir=getFilesDir();
        File todoFile=new File(filesDir,"user.txt");
        ArrayList<String> items;
        try {
            items=new ArrayList<>(FileUtils.readLines(todoFile));
        }catch (IOException e){
            items=new ArrayList<>();
        }
        if (items.size()!=0){
            return new String[]{items.get(0),items.get(1)};
        }
        return null;
    }

    public void saveUser(String[] strings){
        ArrayList items=new ArrayList();
        items.add(strings[0]);
        items.add(strings[1]);
        File filesDir=getFilesDir();
        File todoFile=new File(filesDir,"user.txt");
        try {
            FileUtils.writeLines(todoFile,items);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 完成异步登陆操作
     */
    class LoginAsyncTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {

            try {
                String result = NetController.callInstantNetService(new JSONObject()
                        .put("id",strings[0]).put("password",strings[1]).put("op",NetController.LOGIN).toString());
                return new JSONObject(result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (dialog!=null)
                hideLoadingDialog();

            if (json==null){
                String words;
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    words="服务器异常，无法连接";
                } else {
                    words="请检查您的网络";
                }
                Toast.makeText(LoginActivity.this,words,Toast.LENGTH_SHORT).show();
            }else {
                try {
                    boolean status= json.getBoolean("status");
                    if (status){
                        //login success
                        LocalUser.setLocalUser(JsonUtil.getUser(json.getJSONObject("user")));
                        String[] value = new String[]{userNameView.getText().toString(),passwordView.getText().toString()};
                        saveUser(value);
                        NetController.createNetController(value[0],value[1]);
                        startActivity(new Intent(LoginActivity.this, ContentActivity.class));
                        finish();
                    }else {
                        //login failure
                        String message = json.getString("message");
                        Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
                        passwordView.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
