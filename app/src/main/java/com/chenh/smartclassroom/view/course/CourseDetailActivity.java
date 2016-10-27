package com.chenh.smartclassroom.view.course;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.vo.TimeTableCourse;

public class CourseDetailActivity extends AppCompatActivity {
    private TextView courseName;
    private TextView courseLocation;
    private TextView courseTeacher;
    private TextView courseSections;
    private TextView courseType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

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

        courseName = (TextView) findViewById(R.id.course_name);
        courseName.setText(t.courseName);
        courseLocation = (TextView) findViewById(R.id.course_location);
        courseLocation.setText(t.campus+","+t.courseClassroom);
        courseTeacher= (TextView) findViewById(R.id.course_teacher);
        courseTeacher.setText(t.courseTeacher);
        courseSections= (TextView) findViewById(R.id.course_sections);
        courseSections.setText(t.startSection+"-"+(t.startSection+t.lastSection-1)+"节");
        courseType= (TextView) findViewById(R.id.course_type);
        courseType.setText(t.courseType);
    }
}
