package com.chenh.smartclassroom.view.classroom;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.view.blog.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlos on 2016/10/17.
 */

public class ClassroomGuestbook extends ClassroomDetailActivity.PlaceholderFragment {
    private RecyclerView items;
    private ItemAdapter mAdpater;

    private EditText mEditText;
    private Button mButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_classroom_guestbook, container, false);




        items = (RecyclerView) rootView.findViewById(R.id.rvItems);
        // Create adapter passing in the sample user data
        mAdpater = new ItemAdapter(getActivity(), LocalItem.getItems());
        // Attach the adapter to the recyclerview to populate items
        items.setAdapter(mAdpater);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        items.setLayoutManager(layoutManager);
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
        items.addItemDecoration(itemDecoration);


        mEditText = (EditText) rootView.findViewById(R.id.add_input);
        mButton = (Button) rootView.findViewById(R.id.add_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText().toString()==null)
                    return;
                if (mEditText.getText().toString().equals(""))
                    return;
                String s = mEditText.getText().toString();
                LocalItem.addItem(s);
                mAdpater.notifyDataSetChanged();
                mEditText.setText("");

            }
        });

        return rootView;
    }




    // Create the basic adapter extending from RecyclerView.Adapter
    // Note that we specify the custom ViewHolder which gives us access to our views
    private class ItemAdapter extends
            RecyclerView.Adapter<ItemAdapter.ViewHolder> {

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
        public ItemAdapter(Context context, List<String> contacts) {
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
