package com.chenh.smartclassroom.view.blog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalMessage;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.net.Client;
import com.chenh.smartclassroom.net.NetController;
import com.chenh.smartclassroom.util.HeadUtil;
import com.chenh.smartclassroom.util.TimeUtil;
import com.chenh.smartclassroom.view.ContentFragment;
import com.chenh.smartclassroom.view.LoadingDiaolog;
import com.chenh.smartclassroom.view.UserInfoActivity;
import com.chenh.smartclassroom.view.listView.view.WaterDropListView;
import com.chenh.smartclassroom.vo.BlogMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chenh on 2016/8/1.
 */
public class BlogFragment extends ContentFragment {

    public static final int LOAD_MORE=1;
    public static final int LOAD_MORE_FINISHED=2;

    public static final int REFRESH=3;
    public static final int REFRESH_FINISHED=4;

    public static final int LOAD_FINISHED=5;

    public static final int NOTIFY=6;

    private WaterDropListView blogs;
    private BlogAdapter mAdpater;
    private ArrayList<BlogMessage> data;

    private Handler mHandler;

    private boolean loadMore;
    private boolean refresh;

    LoadingDiaolog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blog, container, false);

        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int what=msg.what;
                String message = msg.obj.toString();

                switch (what){
                    case LOAD_FINISHED:
                        if (loadMore) {
                            blogs.stopLoadMore();
                            loadMore=false;
                        }
                        if (refresh){
                            blogs.stopRefresh();
                            refresh=false;
                        }
                        mAdpater.notifyDataSetChanged();
                        break;
                    case NOTIFY:
                        mAdpater.notifyDataSetChanged();
                        break;
                }
            }
        };
        LocalMessage.getLocalMessage().addHandler(mHandler);

        data= LocalMessage.getLocalMessage().getBlogMessages();
        mAdpater=new BlogAdapter(data);

        blogs = (WaterDropListView) rootView.findViewById(R.id.listView);
        blogs.setWaterDropListViewListener(new DropListViewListener());
        blogs.setPullLoadEnable(true);
        blogs.setAdapter(mAdpater);

        //showLoadingDialog();
        return rootView;
    }

    private class DropListViewListener implements WaterDropListView.IWaterDropListViewListener{

        @Override
        public void onRefresh() {
            if (!loadMore&&!refresh) {
                refresh=true;
                LocalMessage.getLocalMessage().refresh();
            }
        }
        @Override
        public void onLoadMore() {
            if (!loadMore&&!refresh){
                loadMore=true;
                LocalMessage.getLocalMessage().loadMore();
            }

        }
    }




    class BlogAdapter extends ArrayAdapter<BlogMessage> {

        public BlogAdapter(ArrayList<BlogMessage> items) {
            super(getActivity(), 0, items);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //如果没有，就inflate一个
            if (convertView==null)
                convertView=getActivity().getLayoutInflater().inflate(R.layout.list_item_blog_message,null);
            final BlogMessage blogMessage=data.get(position);


            ImageView head=(ImageView)convertView.findViewById(R.id.head);
            head.setImageResource(HeadUtil.getHeadId(blogMessage.author.id));
            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(), UserInfoActivity.class);
                    intent.putExtra("user",blogMessage.author);
                    startActivity(intent);
                }
            });

            TextView tagView= (TextView) convertView.findViewById(R.id.tag);
            tagView.setText("标签："+blogMessage.tag);

            TextView nameView= (TextView) convertView.findViewById(R.id.nick_name);
            nameView.setText(blogMessage.author.nickName);

            TextView mottoView= (TextView) convertView.findViewById(R.id.motto);
            mottoView.setText(blogMessage.author.motto);

            TextView contextView= (TextView) convertView.findViewById(R.id.text);
            contextView.setText(blogMessage.text);

            TextView timeView=(TextView)convertView.findViewById(R.id.send_time);
            timeView.setText(TimeUtil.getTotalDate(blogMessage.sendTime));

            final ImageView like= (ImageView) convertView.findViewById(R.id.like);

            if (blogMessage.isLike!=0) {
                like.setImageResource(R.drawable.ic_love_y);
            }
            else {
                like.setImageResource(R.drawable.ic_love_b);
            }

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject jsonObject=new JSONObject();
                    try {
                        if (blogMessage.isLike!=0){
                            jsonObject.put("op", NetController.CANCEL_BLOG_MESSAGE);
                            jsonObject.put("sheetId",blogMessage.id);
                            jsonObject.put("id",blogMessage.isLike);
                            like.setImageResource(R.drawable.ic_love_b);
                        }else {
                            jsonObject.put("op", NetController.LIKE_BLOG_MESSAGE);
                            jsonObject.put("sheetId",blogMessage.id);
                            jsonObject.put("userId", LocalUser.getLocalUser().getUserId());
                            jsonObject.put("attitude",true);
                            like.setImageResource(R.drawable.ic_love_y);
                        }
                        String message=jsonObject.toString();
                        NetController.getNetController().addTask(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            final ImageView dislike= (ImageView) convertView.findViewById(R.id.dislike);
            if (blogMessage.isDislike!=0) {
                dislike.setImageResource(R.drawable.ic_dislike_y);
            }
            else {
                dislike.setImageResource(R.drawable.ic_dislike_b);
            }

            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject jsonObject=new JSONObject();
                    try {
                        if (blogMessage.isDislike!=0){
                            jsonObject.put("op", NetController.CANCEL_BLOG_MESSAGE);
                            jsonObject.put("sheetId",blogMessage.id);
                            jsonObject.put("id",blogMessage.isDislike);
                            dislike.setImageResource(R.drawable.ic_dislike_b);
                        }else {
                            jsonObject.put("op", NetController.LIKE_BLOG_MESSAGE);
                            jsonObject.put("sheetId",blogMessage.id);
                            jsonObject.put("userId",LocalUser.getLocalUser().getUserId());
                            jsonObject.put("attitude",false);
                            dislike.setImageResource(R.drawable.ic_dislike_y);
                        }
                        String message=jsonObject.toString();
                        NetController.getNetController().addTask(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            ImageView comment= (ImageView) convertView.findViewById(R.id.comment);
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(),BlogMessageActivity.class);
                    intent.putExtra(BlogMessageActivity.SHEET_ID,""+blogMessage.id);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
    }
