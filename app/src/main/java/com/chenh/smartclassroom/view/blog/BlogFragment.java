package com.chenh.smartclassroom.view.blog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.util.HeadUtil;
import com.chenh.smartclassroom.view.ContentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenh on 2016/8/1.
 */
public class BlogFragment extends ContentFragment {

    private ImageView mNotificationBlogModule;

    private ImageView mFindMyCardBlogModule;

    private RecyclerView mBlogList;

    private BlockAdapter mAdpater;



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blog, container, false);


        mNotificationBlogModule = (ImageView) rootView.findViewById(R.id.notification_block);

        mFindMyCardBlogModule = (ImageView) rootView.findViewById(R.id.lostSomething_block);

        mNotificationBlogModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BlogActivity.class);
                intent.putExtra("title","公告通知");
                intent.putExtra("code","公告通知");
                startActivity(intent);
            }
        });

        mFindMyCardBlogModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BlogActivity.class);
                intent.putExtra("title","寻物启事");
                intent.putExtra("code","寻物启事");
                startActivity(intent);
            }
        });

        mBlogList = (RecyclerView) rootView.findViewById(R.id.rvItems);
        configModuleList();

        //showLoadingDialog();
        return rootView;
    }



    private void configModuleList(){

        List<String> data = new ArrayList<>();
        data.add("下课聊");
        data.add("约自习");
        data.add("社团活动");
        data.add("PHP是最好的语言");


        // Create adapter passing in the sample user data
        mAdpater = new BlockAdapter(getActivity(), data);
        // Attach the adapter to the recyclerview to populate items
        mBlogList.setAdapter(mAdpater);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mBlogList.setLayoutManager(layoutManager);
        /*// Add the scroll listener
        mBlogList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });*/

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mBlogList.addItemDecoration(itemDecoration);
    }


    // Create the basic adapter extending from RecyclerView.Adapter
    // Note that we specify the custom ViewHolder which gives us access to our views
     private class BlockAdapter extends
            RecyclerView.Adapter<BlockAdapter.ViewHolder> {

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row

            public ImageView logo;

            public TextView nameView;


            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                logo=(ImageView) itemView.findViewById(R.id.block_image);

                nameView= (TextView) itemView.findViewById(R.id.block_name);



            }
        }


        // Store a member variable for the contacts
        private List<String> mBlogs;
        // Store the context for easy access
        private Context mContext;

        // Pass in the contact array into the constructor
        public BlockAdapter(Context context, List<String> contacts) {
            mBlogs = contacts;
            mContext = context;
        }

        // Easy access to the context object in the recyclerview
        private Context getContext() {
            return mContext;
        }

        // Usually involves inflating a layout from XML and returning the holder
        @Override
        public BlockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.list_item_block, parent, false);

            // Return a new holder instance
            BlockAdapter.ViewHolder viewHolder = new BlockAdapter.ViewHolder(contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(BlockAdapter.ViewHolder holder, int position) {

            // Set item views based on your views and data model
            ImageView head=holder.logo;
            HeadUtil.setHeadView(head,"141250096");

            TextView nameView= holder.nameView;
            nameView.setText(mBlogs.get(position));

        }


        // Returns the total count of items in the list
        @Override
        public int getItemCount() {
            return mBlogs.size();
        }


    }

}
