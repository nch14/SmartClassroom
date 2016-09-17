package com.chenh.smartclassroom.net;

import com.chenh.smartclassroom.model.LocalAvailableClassroom;
import com.chenh.smartclassroom.model.LocalClassroom;
import com.chenh.smartclassroom.model.LocalComment;
import com.chenh.smartclassroom.model.LocalCourse;
import com.chenh.smartclassroom.model.LocalMessage;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.util.json.JsonUtil;
import com.chenh.smartclassroom.vo.AttitudeVO;
import com.chenh.smartclassroom.vo.BlogComments;
import com.chenh.smartclassroom.vo.BlogMessage;
import com.chenh.smartclassroom.vo.Classroom;
import com.chenh.smartclassroom.vo.TimeTableCourse;
import com.chenh.smartclassroom.vo.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chenh on 2016/9/14.
 */
public class ClientCallBackProcess {


    public static void loginCallBack(JSONObject json, boolean success){
        if (success){
            User user= JsonUtil.getUser(json);
            LocalUser.getLocalUser().loginCallBack(user);
        }else {
            try {
                String message=json.getString("message");
                LocalUser.getLocalUser().loginCallBack(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public static void doOpenClass(JSONObject json){

        ArrayList<Classroom> classrooms=new ArrayList<>();
        try {
            int num=json.getInt("num");
            for (int i=0;i<num;i++){
                JSONObject jsonObject=json.getJSONObject(""+i);
                classrooms.add(JsonUtil.getClassroom(jsonObject));
            }
            LocalClassroom.getLocalClassroom().refreshClassroom(classrooms);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static void doLoadBlogMessage(JSONObject json){

        ArrayList<BlogMessage> blogMessages =new ArrayList<>();
        try {
            int num=json.getInt("num");
            for (int i=0;i<num;i++){
                JSONObject jsonObject=json.getJSONObject(""+i);
                blogMessages.add(JsonUtil.getBlogMessage(jsonObject));
            }
            LocalMessage.getLocalMessage().addBlogMessages(blogMessages);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doAttitudeResult(JSONObject json){
        ArrayList<AttitudeVO> attitudeVOs =new ArrayList<>();
        try {
            int num=json.getInt("num");
            long sheetId=json.getLong("sheetId");
            for (int i=0;i<num;i++){
                JSONObject jsonObject=json.getJSONObject(""+i);
                attitudeVOs.add(JsonUtil.getAtttitude(jsonObject));
            }
            LocalMessage.getLocalMessage().addAttitudes(sheetId,attitudeVOs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doLoadBlogComment(JSONObject json){
        ArrayList<BlogComments> blogCommentses =new ArrayList<>();
        try {
            long sheetId=json.getLong("sheetId");
            int num=json.getInt("num");
            for (int i=0;i<num;i++){
                JSONObject jsonObject=json.getJSONObject(""+i);
                blogCommentses.add(JsonUtil.getBlogComments(jsonObject));
            }
            LocalComment.getLocalComment().addComments(sheetId,blogCommentses);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doProcessAvailableClassroom(JSONObject json){
        ArrayList<String> arrayList=new ArrayList<>();
        try {
            int num=json.getInt("num");
            for (int i=0;i<num;i++){
                String s=json.getString(""+i);
                arrayList.add(s);
            }
            LocalAvailableClassroom.getLocalClassroom().addClassroom(arrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void doGetMyCourse(JSONObject json){
        ArrayList<TimeTableCourse> arrayList=new ArrayList<>();
        try {
            int num=json.getInt("num");
            for (int i=0;i<num;i++){
                TimeTableCourse t=JsonUtil.getTimeTableCourse(json.getJSONObject(""+i));
                arrayList.add(t);
            }
            LocalCourse.courses=arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
