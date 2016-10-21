package com.chenh.smartclassroom.view.course;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.view.blog.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by carlos on 2016/10/17.
 */

public class GetMoreCourseFragment extends CourseOrPlanActivity.PlaceholderFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get_more_course, container, false);







        return rootView;
    }

}
