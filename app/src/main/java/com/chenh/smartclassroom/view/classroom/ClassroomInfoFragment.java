package com.chenh.smartclassroom.view.classroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.view.blog.DividerItemDecoration;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by carlos on 2016/10/17.
 */

public class ClassroomInfoFragment extends ClassroomDetailActivity.PlaceholderFragment {
    private RecyclerView mTimeTable;
    private ClassroomArrangementAdapter mAdpater;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_classroom_info, container, false);



        ArrayList<String> datas= new ArrayList<>();
        datas.add("1-3节 陈道蓄-离散数学结构");
        datas.add("5-7节 王浩然-计算系统基础");
        datas.add("9-10节 E创社-例会");


        mTimeTable = (RecyclerView) rootView.findViewById(R.id.arrangement);
        // Create adapter passing in the sample user data
        mAdpater = new ClassroomArrangementAdapter(getActivity(), datas);
        // Attach the adapter to the recyclerview to populate items
        mTimeTable.setAdapter(mAdpater);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mTimeTable.setLayoutManager(layoutManager);
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
        mTimeTable.addItemDecoration(itemDecoration);


        return rootView;



    }

    // Create the basic adapter extending from RecyclerView.Adapter
    // Note that we specify the custom ViewHolder which gives us access to our views
    private class ClassroomArrangementAdapter extends
            RecyclerView.Adapter<ClassroomArrangementAdapter.ViewHolder> {

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row

            //public ImageView logo;

            public TextView nameView;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                //logo=(ImageView) itemView.findViewById(R.id.block_image);

                nameView= (TextView) itemView.findViewById(R.id.name_view);

            }
        }

        // Store a member variable for the contacts
        private List<String> mTimeTable;
        // Store the context for easy access
        private Context mContext;

        // Pass in the contact array into the constructor
        public ClassroomArrangementAdapter(Context context, List<String> contacts) {
            mTimeTable = contacts;
            mContext = context;
        }

        // Easy access to the context object in the recyclerview
        private Context getContext() {
            return mContext;
        }

        // Usually involves inflating a layout from XML and returning the holder
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.list_item_time, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            TextView nameView= holder.nameView;
            nameView.setText(mTimeTable.get(position));

        }

        // Returns the total count of items in the list
        @Override
        public int getItemCount() {
            return mTimeTable.size();
        }
    }



}
