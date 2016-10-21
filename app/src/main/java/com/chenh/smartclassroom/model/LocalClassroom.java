package com.chenh.smartclassroom.model;

import android.os.Handler;

import com.chenh.smartclassroom.net.NetController;
import com.chenh.smartclassroom.view.classroom.OpenClassroomListFragment;
import com.chenh.smartclassroom.vo.Classroom;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 本地的教室模型
 * Created by chenh on 2016/7/27.
 */
public class LocalClassroom {
    /**
     * 教室房间号——教室
     */
    private ArrayList<Classroom> mClassrooms;


    private static LocalClassroom localClassroom;

    private Handler mHandler;


    public static LocalClassroom getLocalClassroom(){
        if (localClassroom==null)
            localClassroom=new LocalClassroom();
        return localClassroom;
    }

    private LocalClassroom(){
        mClassrooms=new ArrayList<>();
        Classroom c=new Classroom();
        c.currentNumOfStudents=16;
        c.humidity=""+30;
        c.name="仙Ⅱ101";
        c.temperature="26℃";
        c.state=Classroom.OPEN;
        mClassrooms.add(c);
        c=new Classroom();
        c.currentNumOfStudents=4;
        c.humidity=""+38;
        c.name="仙Ⅱ102";
        c.temperature="22℃";
        c.state=Classroom.OPEN;
        mClassrooms.add(c);
        c=new Classroom();
        c.currentNumOfStudents=4;
        c.humidity=""+38;
        c.name="仙Ⅱ103";
        c.temperature="22℃";
        c.state=Classroom.OPEN;
        mClassrooms.add(c);
        c=new Classroom();
        c.currentNumOfStudents=4;
        c.humidity=""+38;
        c.name="仙Ⅱ104";
        c.temperature="22℃";
        c.state=Classroom.OPEN;
        mClassrooms.add(c);
        c=new Classroom();
        c.currentNumOfStudents=4;
        c.humidity=""+38;
        c.name="仙Ⅱ105";
        c.temperature="22℃";
        c.state=Classroom.OPEN;
        mClassrooms.add(c);
    }

    /**
     * 该方法由net层调用
     * 即刷新当前教室状态
     * @param classrooms
     */
    public void refreshClassroom(ArrayList<Classroom> classrooms){
        mClassrooms.clear();
        for (int i=0;i<classrooms.size();i++){
            Classroom classroom=classrooms.get(i);
            mClassrooms.add(classroom);
        }
        if (mHandler!=null)
            mHandler.sendMessage(mHandler.obtainMessage(OpenClassroomListFragment.NOTIFY,""));
    }

    public ArrayList<Classroom> getClassrooms(){
        //requestRefresh();
        return mClassrooms;
    }

    public void addHandler(Handler mHandler){
        this.mHandler=mHandler;
    }


    public void requestRefresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("op", NetController.SHOW_OPEN_CLASSES);
                        String message= jsonObject.toString();
                        NetController.getNetController().addTask(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
