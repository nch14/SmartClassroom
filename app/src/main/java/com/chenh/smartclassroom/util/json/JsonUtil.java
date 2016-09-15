package com.chenh.smartclassroom.util.json;

import com.chenh.smartclassroom.util.TimeUtil;
import com.chenh.smartclassroom.vo.AttitudeVO;
import com.chenh.smartclassroom.vo.BlogComments;
import com.chenh.smartclassroom.vo.BlogMessage;
import com.chenh.smartclassroom.vo.Classroom;
import com.chenh.smartclassroom.vo.TimeTableCourse;
import com.chenh.smartclassroom.vo.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Date;

/**
 * Created by chenh on 2016/7/27.
 */
public class JsonUtil {

    public static Classroom getClassroom(JSONObject json){
        Classroom classroom=new Classroom();
        try {
            classroom.temperature=json.getString("temperature");
            classroom.currentNumOfStudents=json.getInt("currentNumOfStudents");
            classroom.humidity=json.getString("humidity");
            classroom.name=json.getString("name");
            classroom.state=json.getInt("state");
            return classroom;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject packClassroom(Classroom classroom){
        JSONObject json=new JSONObject();
        try {
            json.put("temperature",classroom.temperature);
            json.put("currentNumOfStudents",classroom.currentNumOfStudents);
            json.put("humidity",classroom.humidity);
            json.put("name",classroom.name);
            json.put("state",classroom.state);

            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
            return null;
    }

    public static User getUser(JSONObject json){
        User user=new User();
        try {
            user.id=json.getString("id");
            user.identify=json.getInt("identify");
            user.password=json.getString("password");
            user.username=json.getString("username");
            user.nickName=json.getString("nickName");
            user.motto=json.getString("motto");
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject pack(User user){
        JSONObject json=new JSONObject();
        try {
            json.put("id",user.id);
            json.put("password",user.password);
            json.put("identify",user.identify);
            json.put("username",user.username);
            json.put("nickName",user.nickName);
            json.put("motto",user.motto);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static BlogMessage getBlogMessage(JSONObject json){
        BlogMessage blogMessage=new BlogMessage();
        try {
            blogMessage.author=getUser(json.getJSONObject("author"));
            blogMessage.sendTime= TimeUtil.getDate(json.getString("sendTime"));
            blogMessage.tag=json.getString("tag");
            blogMessage.id=json.getLong("id");
            blogMessage.text=json.getString("text");
            return blogMessage;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static BlogComments getBlogComments(JSONObject json){
        BlogComments blogComments=new BlogComments();
        try {
            blogComments.author=getUser(json.getJSONObject("author"));
            blogComments.sendTime=TimeUtil.getDate(json.getString("sendTime"));
            blogComments.rawMessageId=json.getLong("rawMessageId");
            blogComments.id=json.getLong("id");
            blogComments.text=json.getString("text");
            return blogComments;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static AttitudeVO getAtttitude(JSONObject json){
        AttitudeVO attitudeVO=new AttitudeVO();
        try {
            attitudeVO.attitude=json.getBoolean("attitude");
            attitudeVO.sheetId=json.getLong("sheetId");
            attitudeVO.userId=json.getString("userId");
            attitudeVO.id=json.getLong("id");
            return attitudeVO;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     *
     * @param json
     * @return
     */
    public static TimeTableCourse getTimeTableCourse(JSONObject json){
        TimeTableCourse timeTableCourse=new TimeTableCourse();
        try {
           // timeTableCourse.userKey=json.getString("userKey");
           // timeTableCourse.term=json.getString("term");
            timeTableCourse.courseClassroom=json.getString("courseClassroom");
            timeTableCourse.courseDate=json.getString("courseDate");
            timeTableCourse.courseName=json.getString("courseName");
            timeTableCourse.courseTeacher=json.getString("courseTeacher");
            timeTableCourse.courseType=json.getString("courseType");
           // timeTableCourse.note=json.getString("note");
            timeTableCourse.campus=json.getString("campus");
            timeTableCourse.startSection=json.getInt("startSection");
            timeTableCourse.lastSection=json.getInt("lastSection");
            timeTableCourse.week=json.getInt("week");
            return timeTableCourse;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
