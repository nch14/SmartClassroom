package com.chenh.smartclassroom.model;

import android.os.Handler;

import com.chenh.smartclassroom.net.Client;
import com.chenh.smartclassroom.net.NetController;
import com.chenh.smartclassroom.vo.TimeTableCourse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chenh on 2016/9/12.
 */
public class LocalCourse {

    public static ArrayList<TimeTableCourse> courses;

    private static Handler mHandler;

    public static void getCourse(Handler handler){
        mHandler=handler;
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("op", NetController.GET_USER_COURSE);
            jsonObject.put("id",LocalUser.getLocalUser().getUserId());
            String message= jsonObject.toString();
            NetController.getNetController().addTask(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public static void setCourses(ArrayList<TimeTableCourse> cs){
        courses=cs;
        mHandler.sendMessage(mHandler.obtainMessage(1,"refresh"));

    }


}
