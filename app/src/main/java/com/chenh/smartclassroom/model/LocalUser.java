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

    public static LocalUser getLocalUser() {
        if (localUser == null)
            localUser = new LocalUser();
        return localUser;
    }

    public static void setLocalUser(User user){
        if (localUser == null)
            localUser = new LocalUser();
        localUser.user=user;
    }
    private LocalUser() {

    }

    public User getUser(){
        return user;
    }

    public String getUserId(){
        return user.id;
    }

}
