package com.chenh.smartclassroom.vo;

import java.io.Serializable;

/**
 * Created by chenh on 2016/7/27.
 */
public class User implements Serializable {
    public static final int STUDENT=1;
    public static final int MANAGER=2;


    public String username;

    public String id;

    public String password;

    public int identify;

    public String nickName;

    public String motto;

    public String university;//所在学校

    public boolean onLine;//是否启用课程表

}
