package com.chenh.smartclassroom.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.net.NetController;
import com.chenh.smartclassroom.util.Base64Image;
import com.chenh.smartclassroom.util.HeadUtil;
import com.chenh.smartclassroom.util.json.JsonUtil;
import com.chenh.smartclassroom.vo.User;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class UserInfoActivity extends AppCompatActivity {

    private ImageView head;
    private TextView mottoView;
    private TextView nickNameView;
    private TextView nameView;
    private TextView idView;
    private TextView identifyView;

    private User user;

    private final static int FILL_USER_NAME=1;
    private final static int FILL_USER_MOTTO=2;
    private final static int FILL_USER_TRUE_NAME=3;
    private final static int UPDATE_HEAD_IMAGE=4;

    private final static int NET_ERROR = 1;
    private final static int SUCCESS = 2;


    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle("个人信息");

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int what = msg.what;
                String message = (String)msg.obj;

                switch (what){
                    case NET_ERROR:
                        Toast.makeText(UserInfoActivity.this,"請稍後重試",Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        HeadUtil.setHeadView(head,user.id);
                        break;
                }
            }
        };

        Intent intent=getIntent();
        user=(User) intent.getSerializableExtra("user");


        head=(ImageView)findViewById(R.id.head);
        HeadUtil.setHeadView(head,user.id);

        mottoView= (TextView) findViewById(R.id.motto);
        mottoView.setText(user.motto);

        nickNameView= (TextView)  findViewById(R.id.nick_name);
        nickNameView.setText(user.nickName);

        nameView= (TextView)  findViewById(R.id.name);
        nameView.setText(user.username);

        idView= (TextView)  findViewById(R.id.id);
        idView.setText(user.id);

        identifyView= (TextView)  findViewById(R.id.identify);
        identifyView.setText((user.identify==User.STUDENT)?"学生":"管理员");


        if (user.id.equals(LocalUser.getLocalUser().getUserId())){
            addOnClickListener();
        }else {
            removeOnClickListener();
        }
    }


    private void addOnClickListener(){

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserInfoActivity.this, HeadUpdateActivity.class);
                intent.putExtra("id","141250096");
                startActivityForResult(intent,UPDATE_HEAD_IMAGE);
            }
        });


        mottoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserInfoActivity.this, OneLineFillActivity.class);
                intent.putExtra("ACTIVITY_NAME","更改签名");
                startActivityForResult(intent,FILL_USER_MOTTO);
            }
        });

        findViewById(R.id.layout_nickname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserInfoActivity.this, OneLineFillActivity.class);
                intent.putExtra("ACTIVITY_NAME","更改昵称");
                startActivityForResult(intent,FILL_USER_NAME);
            }
        });

        findViewById(R.id.layout_truename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserInfoActivity.this, OneLineFillActivity.class);
                intent.putExtra("ACTIVITY_NAME","更改姓名");
                startActivityForResult(intent,FILL_USER_TRUE_NAME);
            }
        });

        /*findViewById(R.id.layout_sid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserInfoActivity.this, OneLineFillActivity.class);
                intent.putExtra("ACTIVITY_NAME","更改学号");
                startActivityForResult(intent,CHANGE_PASSWORD);
            }
        });*/


    }

    private void removeOnClickListener(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_CANCELED)
            return;
        String itemValue=data.getStringExtra("ITEM_VALUE");
        User user = LocalUser.getLocalUser().getUser();

        switch (requestCode){
            case FILL_USER_NAME:
                nickNameView.setText(itemValue);
                user.nickName=itemValue;
                sendMessageToServerToUpdateUserInfo();
                break;
            case FILL_USER_MOTTO:
                mottoView.setText(itemValue);
                user.motto=itemValue;
                sendMessageToServerToUpdateUserInfo();
                break;
            case FILL_USER_TRUE_NAME:
                nameView.setText(itemValue);
                user.username=itemValue;
                sendMessageToServerToUpdateUserInfo();
                break;
            case UPDATE_HEAD_IMAGE:
                sendHeadToServerToUpdate();
                break;

        }

    }

    private void sendMessageToServerToUpdateUserInfo(){
        JSONObject message=new JSONObject();
        try {
            message.put("op", NetController.REFRESH_USER);
            message.put("user", JsonUtil.pack(user));
            NetController.getNetController().addTask(message.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendHeadToServerToUpdate(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject json = new JSONObject();
                try {
                    json.put("op",NetController.PUT_HEAD);
                    json.put("id",user.id);
                    json.put("image", Base64Image.storeImage(Environment.getExternalStorageDirectory() + "/head/" + user.id + ".jpg"));
                    String message = json.toString();
                    String back = NetController.getNetController().callPicService(message);
                    JSONObject result =new JSONObject(back);
                    boolean success = result.getBoolean("status");

                    if (success)
                        mHandler.sendMessage(mHandler.obtainMessage(SUCCESS,""));
                    else
                        mHandler.sendMessage(mHandler.obtainMessage(NET_ERROR,""));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendMessage(mHandler.obtainMessage(NET_ERROR,""));
                }

            }
        }).start();
    }
}
