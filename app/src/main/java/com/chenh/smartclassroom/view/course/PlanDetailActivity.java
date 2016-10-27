package com.chenh.smartclassroom.view.course;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.vo.TimeTableCourse;

public class PlanDetailActivity extends AppCompatActivity {

    private TextView planName;
    private TextView planLocation;
    private TextView planNote;
    private TextView courseSections;
    private TextView courseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

        Intent intent = getIntent();
        TimeTableCourse t = (TimeTableCourse) intent.getSerializableExtra("course");

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(t.courseName);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        planName = (TextView) findViewById(R.id.plan_name);
        planName.setText(t.courseName);
        planLocation = (TextView) findViewById(R.id.plan_location);
        planLocation.setText(t.campus+","+t.courseClassroom);
        planNote= (TextView) findViewById(R.id.plan_note);
        planNote.setText(t.note);
        courseSections= (TextView) findViewById(R.id.course_sections);
        courseSections.setText(t.startSection+"-"+(t.startSection+t.lastSection-1)+"节");
        courseType= (TextView) findViewById(R.id.course_type);
        courseType.setText(t.courseType);
    }
}
