package com.chenh.smartclassroom.view.course;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalCourse;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.net.NetController;
import com.chenh.smartclassroom.util.Int2ZHUtil;
import com.chenh.smartclassroom.util.TimeUtil;
import com.chenh.smartclassroom.util.json.JsonUtil;
import com.chenh.smartclassroom.view.UserInfoActivity;
import com.chenh.smartclassroom.view.blog.DividerItemDecoration;
import com.chenh.smartclassroom.view.common.OneAreaFillActivity;
import com.chenh.smartclassroom.view.common.OneLineFillActivity;
import com.chenh.smartclassroom.vo.TimeTableCourse;
import com.chenh.smartclassroom.vo.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by carlos on 2016/10/12.
 */

public class MakePlanFragment extends CourseOrPlanActivity.PlaceholderFragment {

    private RecyclerView mTimeTable;
    private TimeTableAdapter mAdpater;

    private String details="";

    private EditText titleView;
    private EditText locationView;
    private ArrayList<TimeTable> times;

    public static final int DETAILS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_make_plan, container, false);

        ArrayList<Point> points = ((CourseOrPlanActivity)getActivity()).getPoints();
        Collections.sort(points, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return o1.x*11+o1.y-(o2.x*11+o2.y);
            }
        });

        times = new ArrayList<>();
        Point lastPoint = points.get(0);
        times.add(new TimeTable(lastPoint.x,lastPoint.y));

        if (points.size()>1){
            Point thisPoint;
            for (int i =1;i<points.size();i++){
                lastPoint = points.get(i-1);
                thisPoint = points.get(i);

                if (thisPoint.x==lastPoint.x&&(thisPoint.y-lastPoint.y==1)){
                    times.get(times.size()-1).addSection(thisPoint.y);
                }else {
                    times.add(new TimeTable(thisPoint.x,thisPoint.y));
                }
            }
        }

        mTimeTable = (RecyclerView) rootView.findViewById(R.id.time_table);
        // Create adapter passing in the sample user data
        mAdpater = new TimeTableAdapter(getActivity(), times);
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

        rootView.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), OneAreaFillActivity.class);
                intent.putExtra("ACTIVITY_NAME","描述");
                startActivityForResult(intent,DETAILS);
            }
        });

        titleView = (EditText) rootView.findViewById(R.id.edit_title);
        locationView = (EditText) rootView.findViewById(R.id.edit_location);

        ((CourseOrPlanActivity) getActivity()).fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleView.getText().toString()==null||locationView.getText().toString()==null){
                    Toast.makeText(getActivity(),"标题和位置不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<TimeTableCourse> cs = new ArrayList<>();
                for (TimeTable t:times){
                    TimeTableCourse timeTableCourse = new TimeTableCourse();
                    timeTableCourse.courseClassroom=locationView.getText().toString();
                    timeTableCourse.courseName=titleView.getText().toString();
                    timeTableCourse.startSection=t.sections.get(0);
                    timeTableCourse.lastSection=t.sections.size();
                    timeTableCourse.courseDate="周"+Int2ZHUtil.intToZH(t.weekDay+1);
                    timeTableCourse.week= TimeUtil.getWeek();
                    timeTableCourse.userKey=LocalUser.getLocalUser().getUserId();
                    timeTableCourse.courseTeacher="";
                    timeTableCourse.campus="仙林";
                    timeTableCourse.note=details;
                    timeTableCourse.term="未定义";
                    timeTableCourse.courseType="自定义";
                    cs.add(timeTableCourse);
                }
                LocalCourse.addCourse(cs);

                JSONObject jsonObject=new JSONObject();
                try {
                    for (int i=0;i<cs.size();i++){
                        jsonObject.put(""+i, JsonUtil.pack(cs.get(i)));
                    }
                    jsonObject.put("num",cs.size());
                    jsonObject.put("op", NetController.ADD_USER_COURSE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String message=jsonObject.toString();
                NetController.getNetController().addTask(message);

                Intent intent=new Intent();
                getActivity().setResult(RESULT_OK,intent);
                getActivity().finish();
            }
        });

        return rootView;
    }


    class TimeTable{
        int weekDay;
        ArrayList<Integer> sections;
        public TimeTable(int weekDay,Integer section){
            this.weekDay = weekDay;
            sections=new ArrayList<>();
            sections.add(section);
        }

        public void addSection(int i){
            sections.add(i);
        }

        public String getVisibleValue(){
            String value="";
            value="周"+Int2ZHUtil.intToZH(weekDay+1)+"第"+sections.get(0);
            if (sections.size()>1)
                return value+="-"+sections.get(sections.size()-1)+"节";
            else
                return value+="节";
        }
    }


    // Create the basic adapter extending from RecyclerView.Adapter
    // Note that we specify the custom ViewHolder which gives us access to our views
    private class TimeTableAdapter extends
            RecyclerView.Adapter<TimeTableAdapter.ViewHolder> {

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
        private List<TimeTable> mTimeTable;
        // Store the context for easy access
        private Context mContext;

        // Pass in the contact array into the constructor
        public TimeTableAdapter(Context context, List<TimeTable> contacts) {
            mTimeTable = contacts;
            mContext = context;
        }

        // Easy access to the context object in the recyclerview
        private Context getContext() {
            return mContext;
        }

        // Usually involves inflating a layout from XML and returning the holder
        @Override
        public TimeTableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.list_item_time, parent, false);

            // Return a new holder instance
            TimeTableAdapter.ViewHolder viewHolder = new TimeTableAdapter.ViewHolder(contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(TimeTableAdapter.ViewHolder holder, int position) {

            // Set item views based on your views and data model
         /*   ImageView head=holder.logo;
            HeadUtil.setHeadView(head,"141250096");*/

            TextView nameView= holder.nameView;
            nameView.setText(mTimeTable.get(position).getVisibleValue());

        }

        // Returns the total count of items in the list
        @Override
        public int getItemCount() {
            return mTimeTable.size();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_CANCELED)
            return;
        String itemValue=data.getStringExtra("ITEM_VALUE");

        switch (requestCode){
            case DETAILS:
                details=itemValue;
                break;
        }
    }
}
