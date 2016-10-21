package com.chenh.smartclassroom.view.classroom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalClassroom;
import com.chenh.smartclassroom.view.ContentFragment;
import com.chenh.smartclassroom.vo.Classroom;

import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Created by chenh on 2016/8/1.
 */
public class OpenClassroomListFragment extends ContentFragment {

    public static final int NOTIFY=6;

    private ListView mClassrooms;
    private ClassroomAdapter mAdpater;
    private ArrayList<Classroom> data;

    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_open_class_list, container, false);

        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int what=msg.what;
                String m= (String) msg.obj;
                switch (what){
                    case NOTIFY:
                        mAdpater.notifyDataSetChanged();
                        break;
                }
            }
        };

        LocalClassroom.getLocalClassroom().addHandler(mHandler);

        data= LocalClassroom.getLocalClassroom().getClassrooms();
        mAdpater=new ClassroomAdapter(data);


        mClassrooms = (ListView) rootView.findViewById(R.id.listView);
        mClassrooms.setAdapter(mAdpater);
        mClassrooms.setEmptyView(rootView.findViewById(R.id.emptyView));
        mClassrooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),ClassroomDetailActivity.class);
                startActivity(intent);
            }
        });



        return rootView;
    }







    class ClassroomAdapter extends ArrayAdapter<Classroom> {

        public ClassroomAdapter(ArrayList<Classroom> items) {
            super(getActivity(), 0, items);

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //如果没有，就inflate一个
            if (convertView==null)
                convertView=getActivity().getLayoutInflater().inflate(R.layout.list_item_classroom,null);
            Classroom classroom=data.get(position);

            TextView classroomName= (TextView) convertView.findViewById(R.id.classroom_name);
            classroomName.setText(classroom.name);

            TextView classroomState= (TextView) convertView.findViewById(R.id.t_h);
            classroomState.setText("当前"+classroom.temperature+"，湿度："+classroom.humidity);

            TextView classroomNum= (TextView) convertView.findViewById(R.id.num_of_people);
            classroomNum.setText("当前："+classroom.currentNumOfStudents+"人");

            return convertView;
        }
    }

}
