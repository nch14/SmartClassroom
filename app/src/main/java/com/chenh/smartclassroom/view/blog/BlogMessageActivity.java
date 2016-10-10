package com.chenh.smartclassroom.view.blog;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class BlogMessageActivity extends AppCompatActivity {

    public static final String SHEET_ID="sheetId";
    public static final int NOTIFY=1;

    private RecyclerView blogs;
    private CommentAdapter mAdpater;
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
        //mAdpater=new CommmentAdapter(data);


        // Lookup the recyclerview in activity layout
        blogs = (RecyclerView) findViewById(R.id.rvItems);
        // Create adapter passing in the sample user data
        mAdpater = new CommentAdapter(this, data);
        // Attach the adapter to the recyclerview to populate items
        blogs.setAdapter(mAdpater);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        blogs.setLayoutManager(layoutManager);
        // Add the scroll listener
        blogs.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        blogs.addItemDecoration(itemDecoration);

        //blogs.setItemAnimator(new SlideInUpAnimator());
        // That's all!

        setRawMessage();
    }


    private void setRawMessage(){
        ImageView head=(ImageView)findViewById(R.id.head);
        HeadUtil.setHeadView(head,blogMessage.author.id);
       // head.setImageResource(HeadUtil.getHeadId(blogMessage.author.id));

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


    // Append more data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int page) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        //  --> Deserialize API response and then construct new objects to append to the adapter
        //  --> Notify the adapter of the changes
    }


    // Create the basic adapter extending from RecyclerView.Adapter
    // Note that we specify the custom ViewHolder which gives us access to our views
    private class CommentAdapter extends
            RecyclerView.Adapter<CommentAdapter.ViewHolder> {

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row

            public ImageView head;

            public TextView nameView;

            public TextView mottoView;

            public TextView contextView;

            public TextView floorView;

            public TextView timeView;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                head=(ImageView) itemView.findViewById(R.id.head);

                nameView= (TextView) itemView.findViewById(R.id.nick_name);

                mottoView= (TextView) itemView.findViewById(R.id.motto);

                contextView= (TextView) itemView.findViewById(R.id.text);

                floorView= (TextView) itemView.findViewById(R.id.floor);

                timeView=(TextView) itemView.findViewById(R.id.send_time);


            }
        }


        // Store a member variable for the contacts
        private List<BlogComments> mContacts;
        // Store the context for easy access
        private Context mContext;

        // Pass in the contact array into the constructor
        public CommentAdapter(Context context, List<BlogComments> contacts) {
            mContacts = contacts;
            mContext = context;
        }

        // Easy access to the context object in the recyclerview
        private Context getContext() {
            return mContext;
        }

        // Usually involves inflating a layout from XML and returning the holder
        @Override
        public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.list_item_blog_comment, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;
        }

        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(CommentAdapter.ViewHolder viewHolder, int position) {
            // Get the data model based on position
            BlogComments blogComments = mContacts.get(position);

            // Set item views based on your views and data model
            ImageView head=viewHolder.head;
            HeadUtil.setHeadView(head,blogComments.author.id);
            //head.setImageResource(HeadUtil.getHeadId(blogComments.author.id));

            TextView nameView= viewHolder.nameView;
            nameView.setText(blogComments.author.nickName);

            TextView mottoView= viewHolder.mottoView;
            mottoView.setText(blogComments.author.motto);

            TextView contextView= viewHolder.contextView;
            contextView.setText(blogComments.text);

            TextView floorView= viewHolder.floorView;
            floorView.setText((position+1)+"楼");

            TextView timeView=viewHolder.timeView;
            timeView.setText(TimeUtil.getTotalDate(blogComments.sendTime));
        }

        // Returns the total count of items in the list
        @Override
        public int getItemCount() {
            return mContacts.size();
        }


    }

}
