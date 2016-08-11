package com.chenh.smartclassroom.model;

import android.os.Handler;

import com.chenh.smartclassroom.view.login.LoginActivity;
import com.chenh.smartclassroom.vo.User;

/**
 * Created by chenh on 2016/7/27.
 */
public class LocalUser {

    private User user;

    private static LocalUser localUser;

    private Handler handler;

    public static LocalUser getLocalUser() {
        if (localUser == null)
            localUser = new LocalUser();
        return localUser;
    }
    private LocalUser() {

    }

    public User getUser(){
        return user;
    }

    public void giveHandler(final Handler handler){
        this.handler=handler;
    }

    /**
     * 登陆成功
     * @param user
     */
    public void loginCallBack(User user){
        this.user=user;
        handler.sendMessage(handler.obtainMessage(LoginActivity.LOGIN_SUCCESS,""));
    }

    /**
     *登陆失败
     * @param message 说明
     */
    public void loginCallBack(String message){
        handler.sendMessage(handler.obtainMessage(LoginActivity.LOGIN_FAIL,message));
    }

    public String getUserId(){
        return user.id;
    }

}
