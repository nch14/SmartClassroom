package com.chenh.smartclassroom.view.blog;

import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalComment;
import com.chenh.smartclassroom.model.LocalMessage;
import com.chenh.smartclassroom.util.HeadUtil;
import com.chenh.smartclassroom.util.TimeUtil;
import com.chenh.smartclassroom.view.LoadingDiaolog;
import com.chenh.smartclassroom.vo.BlogComments;
import com.chenh.smartclassroom.vo.BlogMessage;

import java.util.ArrayList;

public class BlogMessageActivity extends AppCompatActivity {

    public static final String SHEET_ID="sheetId";
    public static final int NOTIFY=1;

    private ListView blogs;
    private CommmentAdapter mAdpater;
    private ArrayList<BlogComments> data;

    private Handler mHandler;

    private long sheetId;

    private BlogMessage blogMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm=getFragmentManager();
                AddCommentDialog dialog= new AddCommentDialog();
                dialog.show(fm,"");
            }
        });

        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int what=msg.what;
                String s= (String) msg.obj;
                switch (what){
                    case NOTIFY:
                        mAdpater.notifyDataSetChanged();
                        TextView comment= (TextView) findViewById(R.id.comments_num);
                        comment.setText("评论："+data.size());
                        break;
                }
            }
        };

        LocalComment.getLocalComment().addHandler(mHandler);

        sheetId = Long.parseLong(getIntent().getStringExtra(SHEET_ID));
        blogMessage= LocalMessage.getLocalMessage().getSheet(sheetId);


        data= LocalComment.getLocalComment().getComments(sheetId);
        mAdpater=new CommmentAdapter(data);

        blogs = (ListView) findViewById(R.id.listView);

        //blogs.setPullLoadEnable(true);
        blogs.setAdapter(mAdpater);
        blogs.setEmptyView(findViewById(R.id.emptyView));

        setRawMessage();
    }





    class CommmentAdapter extends ArrayAdapter<BlogComments> {

        public CommmentAdapter(ArrayList<BlogComments> items) {
            super(BlogMessageActivity.this, 0, items);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //如果没有，就inflate一个
            if (convertView==null)
                convertView=getLayoutInflater().inflate(R.layout.list_item_blog_comment,null);
            BlogComments blogComments=data.get(position);


            ImageView head=(ImageView)convertView.findViewById(R.id.head);
            head.setImageResource(HeadUtil.getHeadId(blogComments.author.id));

            TextView nameView= (TextView) convertView.findViewById(R.id.nick_name);
            nameView.setText(blogComments.author.nickName);

            TextView mottoView= (TextView) convertView.findViewById(R.id.motto);
            mottoView.setText(blogComments.author.motto);

            TextView contextView= (TextView) convertView.findViewById(R.id.text);
            contextView.setText(blogComments.text);

            TextView floorView= (TextView) convertView.findViewById(R.id.floor);
            floorView.setText((position+1)+"楼");

            TextView timeView=(TextView)convertView.findViewById(R.id.send_time);
            timeView.setText(TimeUtil.getTotalDate(blogComments.sendTime));

            return convertView;
        }
    }


    private void setRawMessage(){
        ImageView head=(ImageView)findViewById(R.id.head);
        head.setImageResource(HeadUtil.getHeadId(blogMessage.author.id));

        TextView tagView= (TextView) findViewById(R.id.tag);
        tagView.setText("标签："+blogMessage.tag);

        TextView nameView= (TextView) findViewById(R.id.nick_name);
        nameView.setText(blogMessage.author.nickName);

        TextView mottoView= (TextView) findViewById(R.id.motto);
        mottoView.setText(blogMessage.author.motto);

        TextView contextView= (TextView) findViewById(R.id.text);
        contextView.setText(blogMessage.text);

        TextView timeView=(TextView)findViewById(R.id.send_time);
        timeView.setText(TimeUtil.getTotalDate(blogMessage.sendTime));

        TextView like= (TextView) findViewById(R.id.like_num);
        like.setText("赞："+blogMessage.like.size());

        TextView dislike= (TextView) findViewById(R.id.dislike_num);
        dislike.setText("砸："+blogMessage.dislike.size());

        TextView comment= (TextView) findViewById(R.id.comments_num);
        comment.setText("评论："+data.size());
    }


    public long getSheetId(){
        return sheetId;
    }

}
