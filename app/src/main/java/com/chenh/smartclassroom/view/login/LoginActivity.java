package com.chenh.smartclassroom.view.login;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalMessage;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.net.Client;
import com.chenh.smartclassroom.view.ContentActivity;
import com.chenh.smartclassroom.view.LoadingDiaolog;
import com.chenh.smartclassroom.vo.User;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {


    public static final int LOGIN_SUCCESS=1;
    public static final int LOGIN_FAIL=2;

    private EditText userNameView;
    private EditText passwordView;

    private Button loginButton;

    private Handler handler;

    private LoadingDiaolog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int what=msg.what;
                String words= (String) msg.obj;

                switch (what){
                    case LOGIN_SUCCESS:
                        hideLoadingDialog();
                        saveUser(new String[]{userNameView.getText().toString(),passwordView.getText().toString()});
                        LocalMessage.getLocalMessage().refresh();
                        Intent intent=new Intent(LoginActivity.this, ContentActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case LOGIN_FAIL:
                        hideLoadingDialog();
                        Toast.makeText(LoginActivity.this,words,Toast.LENGTH_SHORT).show();
                        passwordView.setText("");
                        break;
                }
            }
        };
        showLogin();

        Intent intent=getIntent();
        String s=intent.getStringExtra("auto");
        if (s==null){
            autoLogin();
        }
        LocalUser.getLocalUser().giveHandler(handler);

    }

    private void showLogin(){
        userNameView= (EditText) findViewById(R.id.input_id);
        passwordView= (EditText) findViewById(R.id.input_password);

        loginButton= (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new DoLogin());
    }

    private void disableViews(){
        userNameView.setVisibility(View.INVISIBLE);
        passwordView.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.INVISIBLE);
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
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("op",Client.LOGIN);
            jsonObject.put("id",userNameView.getText().toString());
            jsonObject.put("password",passwordView.getText().toString());
            Client.getClient().addMessage(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

}
