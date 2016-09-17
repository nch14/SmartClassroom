package com.chenh.smartclassroom.model;

import android.os.Handler;

import com.chenh.smartclassroom.net.Client;
import com.chenh.smartclassroom.net.NetController;
import com.chenh.smartclassroom.view.blog.BlogMessageActivity;
import com.chenh.smartclassroom.vo.BlogComments;
import com.chenh.smartclassroom.vo.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by chenh on 2016/8/2.
 */
public class LocalComment {

    private HashMap<Long,ArrayList<BlogComments>> comments;
    //private ArrayList<BlogComments> comments;

    private static LocalComment localComment;

    private static Handler mHandler;

    public static LocalComment getLocalComment(){
        if (localComment==null)
            localComment=new LocalComment();
        return localComment;
    }

    private LocalComment(){
        comments=new HashMap<>();

        User user=new User();
        user.nickName="东北狼探路女儿国";
        user.motto="我的剑就是你的剑！";

        BlogComments blogComments=new BlogComments();
        blogComments.author=user;
        blogComments.sendTime=new Date();
        blogComments.text="你这个菜鸡";
    }

    public ArrayList<BlogComments> getComments(long sheetId) {
        boolean has=comments.containsKey(sheetId);
        if (has){
            return comments.get(sheetId);
        }else {
            ArrayList<BlogComments> arrayList=new ArrayList<>();
            comments.put(sheetId,arrayList);
            requestComments(sheetId);
            return arrayList;
        }
    }

    public void addHandler(Handler handler){
        mHandler=handler;
    }

    public void addComments(long sheetId,ArrayList<BlogComments> commentses){
        ArrayList arrayList=comments.get(sheetId);
        arrayList.clear();
        for (BlogComments blogComments:commentses){
            arrayList.add(blogComments);
        }
        mHandler.sendMessage(mHandler.obtainMessage(BlogMessageActivity.NOTIFY,""));
    }

    public void addComment(long sheetId,BlogComments commentses){
        ArrayList arrayList=comments.get(sheetId);
        arrayList.add(commentses);
        mHandler.sendMessage(mHandler.obtainMessage(BlogMessageActivity.NOTIFY,""));
    }

    private void requestComments(long sheetId){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("op", NetController.REQUEST_BLOG_COMMENT);
            jsonObject.put("sheetId",sheetId);
            String message=jsonObject.toString();
            NetController.getNetController().addTask(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
