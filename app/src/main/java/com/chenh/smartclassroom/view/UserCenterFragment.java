package com.chenh.smartclassroom.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.util.HeadUtil;
import com.chenh.smartclassroom.view.course.CourseManageActivity;
import com.chenh.smartclassroom.view.login.LoginActivity;
import com.chenh.smartclassroom.vo.BlogComments;
import com.chenh.smartclassroom.vo.User;

/**
 * Created by chenh on 2016/8/3.
 */
public class UserCenterFragment extends ContentFragment {

    private ImageView head;
    private TextView mottoView;
    private TextView nickNameView;
    private TextView nameView;
    private TextView idView;
    private TextView identifyView;

    private Button logOut;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_center, container, false);

        final User user= LocalUser.getLocalUser().getUser();

        head=(ImageView)rootView.findViewById(R.id.head);
        head.setImageResource(HeadUtil.getHeadId(user.id));
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        LinearLayout courseManage = (LinearLayout) rootView.findViewById(R.id.course_manage_layout);
        courseManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), CourseManageActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
