package com.chenh.smartclassroom.vo;

import java.util.ArrayList;

/**
 * 把课程翻译成课程表存进数据库
 * Created by chenh on 2016/9/10.
 */
public class TimeTableCourse {

    /**
     * 学号
     */
    public String userKey;

    /**
     *学期
     */
    public String term;

    /**
     * 课程名
     */
    public String courseName;

    /**
     * 教师
     */
    public String courseTeacher;

    /**
     * 备注
     */
    public String note;

    /**
     * 课程类型
     */
    public String courseType;

    /**
     * 校区
     */
    public String campus;

    /**
     * 从那一节开始
     */
    public int startSection;

    /**
     * 持续几节课
     */
    public  int lastSection;

    /**
     * 周几
     */
    public  String courseDate;
    /**
     * 教室
     */
    public String courseClassroom;
    /**
     * 周数
     */
    public int week;


    public TimeTableCourse(){

    }


}
