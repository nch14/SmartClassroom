package com.chenh.smartclassroom.model;

import android.os.Handler;

import com.chenh.smartclassroom.net.Client;
import com.chenh.smartclassroom.net.NetController;
import com.chenh.smartclassroom.view.blog.BlogFragment;
import com.chenh.smartclassroom.vo.AttitudeVO;
import com.chenh.smartclassroom.vo.BlogMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by chenh on 2016/7/27.
 */
public class LocalMessage {

    private ArrayList<BlogMessage> blogMessages;

    /*private ArrayList<AttitudeVO> attitudeVOs;
*/
    private static LocalMessage localMessage;

    private Handler mHandler;

    private long maxId=-1;

    private long sinceId=-1;

    private int items=10;

    public static LocalMessage getLocalMessage(){
        if (localMessage==null)
            localMessage=new LocalMessage();
        return localMessage;
    }

    public ArrayList<BlogMessage> getBlogMessages(){
        return blogMessages;
    }

    private LocalMessage(){
        blogMessages=new ArrayList<>();
        /*attitudeVOs=new ArrayList<>();*/

        /*refresh();*/
    }

    public void addHandler(Handler handler){
        mHandler=handler;
    }
    /**
     * 从网络获得blogMessage
     * @param blogMessages
     */
    public void addBlogMessages(ArrayList<BlogMessage> blogMessages){
        for (BlogMessage blogMessage:blogMessages){
            this.blogMessages.add(blogMessage);
        }
        sort();
        maxId=this.blogMessages.get(0).id;
        sinceId=this.blogMessages.get(this.blogMessages.size()-1).id;
        if (mHandler!=null)
            mHandler.sendMessage(mHandler.obtainMessage(BlogFragment.LOAD_FINISHED,""));
    }

    public void addAttitudes(long sheetId,ArrayList<AttitudeVO> attitudeVOs){
        BlogMessage blogMessage=getSheet(sheetId);
        blogMessage.like=new ArrayList<>();
        blogMessage.dislike=new ArrayList<>();
        blogMessage.isLike=0;
        blogMessage.isDislike=0;
        for (AttitudeVO attitudeVO:attitudeVOs){
            boolean me= attitudeVO.userId.equals(LocalUser.getLocalUser().getUserId());

            if (attitudeVO.attitude){
                if (me)
                    blogMessage.isLike=attitudeVO.id;
                blogMessage.like.add(attitudeVO);
            }else {
                if (me)
                    blogMessage.isDislike=attitudeVO.id;
                blogMessage.dislike.add(attitudeVO);
            }
        }
            mHandler.sendMessage(mHandler.obtainMessage(BlogFragment.NOTIFY,""));
    }

    public void loadMore() {
        JSONObject json = new JSONObject();
        try {
            json.put("op", NetController.LOAD_BLOG_MESSAGE);
            json.put("sinceId", -1);
            json.put("maxId", sinceId);
            json.put("items", items);
            NetController.getNetController().addTask(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refresh(){
        JSONObject json = new JSONObject();
        try {
            json.put("op", NetController.LOAD_BLOG_MESSAGE);
            json.put("sinceId", maxId);
            json.put("maxId", -1);
            json.put("items", items);
            NetController.getNetController().addTask(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sort(){
        Collections.sort(blogMessages,comparator);
        for(int i=0;i<blogMessages.size()-1;i++){
            if (blogMessages.get(i).id==blogMessages.get(i+1).id){
                blogMessages.remove(i);
                i--;
            }
        }
    }

    Comparator<BlogMessage> comparator = new Comparator<BlogMessage>(){
        @Override
        public int compare(BlogMessage lhs, BlogMessage rhs) {
            return (int)(rhs.sendTime.getTime()-lhs.sendTime.getTime());
        }
    };

   /* private void sortAttitude(){
        Collections.sort(attitudeVOs,comparatorAttitude);
        for (int i=0;i<attitudeVOs.size()-1;i++){
            if (attitudeVOs.get(i).id==attitudeVOs.get(i+1).id) {
                attitudeVOs.remove(attitudeVOs.get(i+1));
                i--;
            }
        }
    }

    Comparator<AttitudeVO> comparatorAttitude = new Comparator<AttitudeVO>(){
        @Override
        public int compare(AttitudeVO lhs, AttitudeVO rhs) {
            return (int)(rhs.id-lhs.id);
        }
    };

    public ArrayList<AttitudeVO> getAttitudeVO(long sheetId,boolean bool){
        for (int i=0;i<attitudeVOs.size();i++){
            AttitudeVO attitudeVO=attitudeVOs.get(i);
            attitudeVO.sheetId
        }
    }


    private ArrayList<AttitudeVO> getSheet(long sheetId){

    }*/

    public BlogMessage getSheet(long sheetId){
        for (int i=0;i<blogMessages.size();i++){
            if (blogMessages.get(i).id==sheetId){
                return blogMessages.get(i);
            }
        }
        return null;
    }

    public long getMaxId(){
        return maxId;
    }

}
