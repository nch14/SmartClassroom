package com.chenh.smartclassroom.vo;

/**
 * Created by chenh on 2016/7/27.
 */
public class Classroom {

    public static final int OPEN=1;
    public static final int Close=2;
    public static final int rent=3;


    /**
     * 例如：甲区501
     * 作为教室的唯一标识、不可重复
     */
    public String name;

    /**
     * 教室当前温度
     */
    public String temperature;

    /**
     *教室当前湿度
     */
    public String humidity;

    /**
     * 教室当前人数
     */
    public int currentNumOfStudents;

    /**
     * 教室当前状态
     */
    public int state;

}
