package com.chenh.smartclassroom.view.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.net.NetController;
import com.chenh.smartclassroom.view.ConfigActivity;
import com.chenh.smartclassroom.view.common.OneLineFillActivity;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by chenh on 2016/9/16.
 */
public class CourseManageActivity extends ConfigActivity {
    private Switch mSwitch;
    private TextView mID;
    private TextView mPWD;
    private TextView mTerm;

    private int passwordIsValid;
    private boolean dirtyFlag;


    private final static int FILL_USER_NAME=1;
    private final static int FILL_USER_PASSWORD=2;
    private final static int FILL_USER_TERM=3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_course_manage);
        super.onCreate(savedInstanceState);

        mSwitch= (Switch) findViewById(R.id.course_switch);
        mID= (TextView) findViewById(R.id.s_key);
        mID.setText(LocalUser.getLocalUser().getUserId());

        mPWD = (TextView) findViewById(R.id.s_pwd);
        String[] password=loadPassword();
        if (password!=null)
            mPWD.setText(password[0]);
        mTerm = (TextView) findViewById(R.id.s_term);


        boolean courseModuleIsOpen=LocalUser.getLocalUser().getUser().courseEnabled;
        mSwitch.setChecked(courseModuleIsOpen);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(CourseManageActivity.this,"请填写教务网用户名以及密码。我们仅用它获取您的课表信息，您的账号密码不会被上传到网络。如果您拒绝相信，请来鼓楼陶二707我拿代码给您看！",Toast.LENGTH_LONG).show();
                    addOnClickListener();
                }else {
                    LocalUser.getLocalUser().getUser().courseEnabled=false;
                    Toast.makeText(CourseManageActivity.this,"您已成功关闭此功能。",Toast.LENGTH_SHORT).show();
                    removeOnClickListener();
                    JSONObject json=new JSONObject();
                    try {
                        json.put("op",NetController.REMOVE_USER_COURSE);
                        json.put("id",LocalUser.getLocalUser().getUserId());
                        NetController.getNetController().addTask(json.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

       setTerm();

        if (courseModuleIsOpen)
            addOnClickListener();

    }
    private void addOnClickListener(){
        /*findViewById(R.id.jw_username).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dirtyFlag=true;
                Intent intent=new Intent(CourseManageActivity.this, OneLineFillActivity.class);
                intent.putExtra("ACTIVITY_NAME","用户名");
                startActivityForResult(intent,FILL_USER_NAME);
            }
        });*/
        findViewById(R.id.jw_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dirtyFlag=true;
                Intent intent=new Intent(CourseManageActivity.this, OneLineFillActivity.class);
                intent.putExtra("ACTIVITY_NAME","密码");
                startActivityForResult(intent,FILL_USER_PASSWORD);
            }
        });
        /*findViewById(R.id.jw_term).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CourseManageActivity.this, OneLineFillActivity.class);
                intent.putExtra("ACTIVITY_NAME","当前学期");
                startActivityForResult(intent,FILL_USER_TERM);
            }
        });*/
        findViewById(R.id.jw_hack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CourseManageActivity.this,"根据有关法律法规，本功能暂不开放。缅怀阿里五仁月饼，打倒资本主义!\n——Developer",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void removeOnClickListener(){
        findViewById(R.id.jw_username).setOnClickListener(null);
        findViewById(R.id.jw_password).setOnClickListener(null);
        findViewById(R.id.jw_term).setOnClickListener(null);
        findViewById(R.id.jw_hack).setOnClickListener(null);
    }

    private void setTerm(){
        String id=LocalUser.getLocalUser().getUserId();
        String year="20"+id.substring(0,2);
        int yearInt= Integer.parseInt(year);
        int nowYear= Calendar.getInstance().get(Calendar.YEAR);
        int term = nowYear-yearInt;
        int nowMonth= Calendar.getInstance().get(Calendar.MONTH)+1;
        if (nowMonth<8){
            term=term*2;
        }else {
            term=term*2+1;
        }
        mTerm.setText(""+term);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_commit) {
            boolean isChecked = mSwitch.isChecked();

            //当用户试图启用课程表时（首先用户本来应该没有启用课程表
            if (isChecked&&!LocalUser.getLocalUser().getUser().courseEnabled){
                //loading动画

                //检查用户名和密码
                checkPasswordValid();

                //等待检查结束
                while (passwordIsValid==0){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (passwordIsValid==1){
                    JSONObject json=new JSONObject();
                    try {
                        json.put("op",NetController.INIT_USER_COURSE);
                        json.put("id",mID.getText().toString());
                        json.put("password",mPWD.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    NetController.getNetController().addTask(json.toString());
                }
                LocalUser.getLocalUser().getUser().courseEnabled=true;
                makeNotification();
                finish();
            }else {
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeNotification(){
        String words;
        if (passwordIsValid==1){
            words="成功启用课程表功能。将在下次登陆生效";
        }else {
            words="你竟然用假的教务网账号&密码骗我！快回去改过来！";
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

    private String[] loadPassword(){
        File filesDir=getFilesDir();
        File todoFile=new File(filesDir,"password.txt");
        ArrayList<String> items;
        try {
            items=new ArrayList<String>(FileUtils.readLines(todoFile));
        }catch (IOException e){
            items=new ArrayList<>();
        }
        if (items.size()!=0){
            return new String[]{items.get(0)};
        }
        return null;
    }

    public void savePassword(String[] strings){
        ArrayList items=new ArrayList();
        items.add(strings[0]);
        File filesDir=getFilesDir();
        File todoFile=new File(filesDir,"password.txt");
        try {
            FileUtils.writeLines(todoFile,items);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_CANCELED)
            return;
        String itemValue=data.getStringExtra("ITEM_VALUE");
        switch (requestCode){
            case FILL_USER_NAME:
                mID.setText(itemValue);
                break;
            case FILL_USER_PASSWORD:
                mPWD.setText(itemValue);
                savePassword(new String[]{itemValue});
                break;
        }
    }
}
