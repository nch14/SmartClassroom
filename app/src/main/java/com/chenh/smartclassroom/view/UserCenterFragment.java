package com.chenh.smartclassroom.view;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.net.NetController;
import com.chenh.smartclassroom.util.HeadUtil;
import com.chenh.smartclassroom.util.json.JsonUtil;
import com.chenh.smartclassroom.view.course.CourseManageActivity;
import com.chenh.smartclassroom.view.course.TempAcitivity;
import com.chenh.smartclassroom.view.login.LoginActivity;
import com.chenh.smartclassroom.vo.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chenh on 2016/8/3.
 */
public class UserCenterFragment extends ContentFragment {

    private ImageView head;
    private TextView course_used;

    private TextView version_code;

    private Button logOut;

    public static final int CHANGE_PASSWORD=1;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_center, container, false);

        final User user= LocalUser.getLocalUser().getUser();

        head=(ImageView)rootView.findViewById(R.id.head);
        HeadUtil.setHeadView(head,user.id);
        //head.setImageResource(HeadUtil.getHeadId(user.id));
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        course_used=(TextView)rootView.findViewById(R.id.course_enabled);
        if (user.courseEnabled)
            course_used.setText("已启用");

        version_code= (TextView) rootView.findViewById(R.id.version_code);
        getAPPVersion();

        LinearLayout courseManage = (LinearLayout) rootView.findViewById(R.id.course_manage_layout);
        courseManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), CourseManageActivity.class);
                startActivity(intent);
            }
        });

        rootView.findViewById(R.id.temp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), TempAcitivity.class);
                startActivity(intent);
            }
        });

        rootView.findViewById(R.id.version).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"还没想好这儿说什么，下次见",Toast.LENGTH_SHORT).show();
            }
        });

        rootView.findViewById(R.id.change_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), OneLineFillActivity.class);
                intent.putExtra("ACTIVITY_NAME","更改密码");
                startActivityForResult(intent,CHANGE_PASSWORD);
            }
        });

        rootView.findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), TeamInfoActivity.class);
                startActivity(intent);
            }
        });

        rootView.findViewById(R.id.version).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        logOut= (Button) rootView.findViewById(R.id.btn_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("auto","");
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== Activity.RESULT_CANCELED)
            return;
        String itemValue=data.getStringExtra("ITEM_VALUE");
        switch (requestCode){
            case CHANGE_PASSWORD:
                User user = LocalUser.getLocalUser().getUser();
                user.password = itemValue;
                JSONObject message=new JSONObject();
                try {
                    message.put("op", NetController.REFRESH_USER);
                    message.put("user",JsonUtil.pack(user));
                    NetController.getNetController().addTask(message.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("auto","");
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取软件版本号
     */
    private void getAPPVersion() {


        PackageManager pm = getActivity().getPackageManager();//得到PackageManager对象
        try {
            PackageInfo pi = pm.getPackageInfo(getActivity().getPackageName(), 0);//得到PackageInfo对象，封装了一些软件包的信息在里面
            int appVersion = pi.versionCode;//获取清单文件中versionCode节点的值
            Log.d("app version", "appVersion="+appVersion);
            version_code.setText("v "+pi.versionName);
            //setVersion(getString(R.string.app_version)+":"+String.valueOf(appVersion));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("app version", "getAppVersion:"+e.getCause());
        }
    }
}
