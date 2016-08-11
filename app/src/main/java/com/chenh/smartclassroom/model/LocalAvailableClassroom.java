package com.chenh.smartclassroom.model;

import android.os.Handler;

import com.chenh.smartclassroom.view.classroom.ApplyForClassroomActivity;

import java.util.ArrayList;

/**
 * Created by chenh on 2016/8/3.
 */
public class LocalAvailableClassroom {

    private ArrayList<String> classrooms;

    private Handler mHandler;

    private static LocalAvailableClassroom localAvailableClassroom;

    public static LocalAvailableClassroom getLocalClassroom(){
        if (localAvailableClassroom==null)
            localAvailableClassroom=new LocalAvailableClassroom();
        return localAvailableClassroom;
    }

    private LocalAvailableClassroom(){
        classrooms=new ArrayList<>();
    }

    public void addClassroom(ArrayList<String> arrayList){
        classrooms.clear();
        for (String s:arrayList){
            classrooms.add(s);
        }
        mHandler.sendMessage(mHandler.obtainMessage(ApplyForClassroomActivity.LOAD_CLASSROOM_FINISHED,""));
    }

    public void addHandler(Handler mHandler){
        this.mHandler=mHandler;
    }

    public ArrayList<String> getClassroom(){
        classrooms=new ArrayList<>();
        return classrooms;
    }
}
