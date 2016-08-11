package com.chenh.smartclassroom.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.util.HeadUtil;
import com.chenh.smartclassroom.view.login.LoginActivity;
import com.chenh.smartclassroom.vo.User;

public class UserInfoActivity extends AppCompatActivity {

    private ImageView head;
    private TextView mottoView;
    private TextView nickNameView;
    private TextView nameView;
    private TextView idView;
    private TextView identifyView;
    
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

        Intent intent=getIntent();
        User user=(User) intent.getSerializableExtra("user");

        head=(ImageView)findViewById(R.id.head);
        head.setImageResource(HeadUtil.getHeadId(user.id));

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
    }
}
