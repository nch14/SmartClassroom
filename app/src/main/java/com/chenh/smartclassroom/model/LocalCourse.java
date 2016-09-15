package com.chenh.smartclassroom.model;

import android.os.Handler;

import com.chenh.smartclassroom.net.Client;
import com.chenh.smartclassroom.vo.TimeTableCourse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chenh on 2016/9/12.
 */
public class LocalCourse {

    public static ArrayList<TimeTableCourse> courses;

    private Handler mHandler;

    public static void getCourse(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("op", Client.GET_MY_COURSE);
            String message= jsonObject.toString();
            Client.getClient().addMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
