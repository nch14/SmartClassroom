package com.chenh.smartclassroom.vo;

import java.util.ArrayList;

/**
 * Created by chenh on 2016/9/8.
 */

public class Course {

    public static final int MY_COURSE_SOURCE=1;

    public static final int SELECT_COMMON_COURSE_SOURCE=2;

    /**
     * 课程号
     */
    public String courseId;
    /**
     * 原编号
     */
    public String courseOldId;
    /**
     * 课程名
     */
    public String courseName;
    /**
     * 学分
     */
    public String courseCredit;
    /**
     * 上课时间
     */
    public String courseTime;
    /**
     * 教师
     */
    public String courseTeacher;
    /**
     * 限额
     */
    public String limitNum;
    /**
     * 已选
     */
    public String acceptNum;
    /**
     * 备注
     */
    public String note;

    /**
     *隐式的ID
     */
    public String courseChooseValue;

    /**
     * 课程类型
     */
    public String courseType;

    /**
     * 校区
     */
    public String campus;

    /**
     * 课程时间和位置
     */
    public ArrayList<CourseTimeAndClassroom> courseTimeAndClassrooms;


    public Course(){}
    public Course(String[] strings,int type){
        switch (type){
            case MY_COURSE_SOURCE:
                courseId=strings[0];
                courseOldId=strings[1];
                courseName=strings[2];
                campus=strings[3];
                courseTeacher=strings[4];
                courseTimeAndClassrooms=CourseTool.parseDateAndClassroom(strings[5]);
                courseType=strings[7];
                break;
            case SELECT_COMMON_COURSE_SOURCE:
                courseId=strings[0];
                courseOldId=strings[1];
                courseName=strings[2];
                courseCredit=strings[3];
                courseTime=strings[4];
                courseTeacher=strings[5];
                limitNum=strings[6];
                acceptNum=strings[7];
                note=strings[8];
                courseChooseValue=strings[9];
                break;
        }

    }
}
