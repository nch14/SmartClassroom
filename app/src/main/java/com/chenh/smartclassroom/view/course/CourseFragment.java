package com.chenh.smartclassroom.view.course;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalCourse;
import com.chenh.smartclassroom.util.ColorUtils;
import com.chenh.smartclassroom.view.ContentFragment;
import com.chenh.smartclassroom.view.CornerTextView;
import com.chenh.smartclassroom.vo.TimeTableCourse;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by chenh on 2016/9/9.
 */
public class CourseFragment extends ContentFragment {

    LinearLayout weekNames;

    private int itemHeight;;

    LinearLayout sections;

    List<LinearLayout> mWeekViews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        mWeekViews=new ArrayList<>();
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_1));
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_2));
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_3));
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_4));
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_5));
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_6));
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_7));

        sections= (LinearLayout) rootView.findViewById(R.id.sections);
        weekNames= (LinearLayout) rootView.findViewById(R.id.weekNames);

        itemHeight=getActivity().getResources().getDimensionPixelSize(R.dimen.sectionHeight);

        initWeekNameView();
        initSectionView(11);
        initWeekCourseView();

        return rootView;
    }

    /**
     * 顶部周一到周日的布局
     **/
    private void initWeekNameView() {
        int today = getDate();
        int week = getWeekDay();
        int startDay=today-(week-1);

        for (int i = 0; i < mWeekViews.size() + 1; i++) {
            TextView tvWeekName = new TextView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

            if (i != 0) {
                lp.weight = 1;
                tvWeekName.setText(startDay+"\n"+"周" + intToZH(i));
                startDay++;
                if (i == getWeekDay()) {
                    tvWeekName.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    tvWeekName.setTextColor(Color.parseColor("#4A4A4A"));
                }
            } else {
                lp.weight = 0.8f;
                tvWeekName.setText(getMonth() + "月");
            }
            tvWeekName.setGravity(Gravity.CENTER_HORIZONTAL);
            tvWeekName.setLayoutParams(lp);
            weekNames.addView(tvWeekName);
        }
    }

    /**
     * 左边节次布局，设定每天最多maxSection节课
     * @param maxSection
     */
    private void initSectionView(int maxSection) {
        for (int i = 1; i <= maxSection; i++) {
            TextView tvSection = new TextView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, itemHeight);
            lp.gravity = Gravity.CENTER;
            tvSection.setGravity(Gravity.CENTER);
            tvSection.setText(String.valueOf(i));
            tvSection.setLayoutParams(lp);
            sections.addView(tvSection);
        }
    }


    /**
     * 初始化课程表
     */
    private void initWeekCourseView() {
        for (int i = 0; i < mWeekViews.size(); i++) {
            initWeekPanel(mWeekViews.get(i), getCourses()[i]);
        }
    }

    public void initWeekPanel(LinearLayout ll, List<TimeTableCourse> data) {

        if (ll == null || data == null || data.size() < 1)
            return;
        TimeTableCourse firstCourse = data.get(0);
        for (int i = 0; i < data.size(); i++) {
            final TimeTableCourse course = data.get(i);
            if (course.startSection == 0 || course.lastSection == 0)
                return;
            FrameLayout frameLayout = new FrameLayout(getActivity());

            CornerTextView tv = new CornerTextView(getActivity(),
                    ColorUtils.getCourseBgColor((int) (Math.random() * 10)),
                    dip2px(getActivity(), 3));
            LinearLayout.LayoutParams frameLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    itemHeight * course.lastSection);
            LinearLayout.LayoutParams tvLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            if (i == 0) {
                frameLp.setMargins(0, (course.startSection - 1) * itemHeight, 0, 0);
            } else {
                frameLp.setMargins(0, (course.startSection - (firstCourse.startSection + firstCourse.lastSection)) * itemHeight, 0, 0);
            }
            tv.setLayoutParams(tvLp);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(12);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setText(course.courseName + "\n @" + course.courseClassroom);

            frameLayout.setLayoutParams(frameLp);
            frameLayout.addView(tv);
            frameLayout.setPadding(2, 2, 2, 2);
            ll.addView(frameLayout);
            firstCourse = course;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast("由于服务器端的调整，本页面功能暂停使用。我们承诺提供一个更好用的课程表与课程管理功能。请静待更新");
                }
            });
        }
    }


    /**
     * 数字转换中文
     */
    public static String intToZH(int i) {
        String[] zh = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] unit = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十"};

        String str = "";
        StringBuffer sb = new StringBuffer(String.valueOf(i));
        sb = sb.reverse();
        int r = 0;
        int l = 0;
        for (int j = 0; j < sb.length(); j++) {
            r = Integer.valueOf(sb.substring(j, j + 1));
            if (j != 0)
                l = Integer.valueOf(sb.substring(j - 1, j));
            if (j == 0) {
                if (r != 0 || sb.length() == 1)
                    str = zh[r];
                continue;
            }
            if (j == 1 || j == 2 || j == 3 || j == 5 || j == 6 || j == 7 || j == 9) {
                if (r != 0)
                    str = zh[r] + unit[j] + str;
                else if (l != 0)
                    str = zh[r] + str;
                continue;
            }
            if (j == 4 || j == 8) {
                str = unit[j] + str;
                if ((l != 0 && r == 0) || r != 0)
                    str = zh[r] + str;
                continue;
            }
        }
        if (str.equals("七"))
            str = "日";
        return str;
    }


    /**
     * 当前星期
     */
    private int getWeekDay() {
        int w = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        if (w <= 0) {
            w = 7;
        }
        return w;
    }

    private int getDate(){
        int d = Calendar.getInstance().get(Calendar.DATE);
        return d;
    }

    /**
     * 当前月份
     */
    private int getMonth() {
        int w = Calendar.getInstance().get(Calendar.MONTH) + 1;
        return w;
    }

    private List<TimeTableCourse>[] getCourses(){
        ArrayList<TimeTableCourse> localCourse= LocalCourse.courses;
        List<TimeTableCourse> TimeTableCourses[] = new ArrayList[7];

        for (int i = 0; i < TimeTableCourses.length; i++) {
            TimeTableCourses[i] = new ArrayList<>();
        }

        for (TimeTableCourse t:localCourse){
            TimeTableCourses[getNumFromCh(t.courseDate)].add(t);
        }

        return TimeTableCourses;
    }

    private int getNumFromCh(String s){
        ArrayList<String> ch=new ArrayList<>();
        ch.add("周一");
        ch.add("周二");
        ch.add("周三");
        ch.add("周四");
        ch.add("周五");
        ch.add("周六");
        ch.add("周日");
        return ch.indexOf(s);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Toast
     */
    private void showToast(String msg) {
        if (TextUtils.isEmpty(msg))
            return;
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

}
