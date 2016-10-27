package com.chenh.smartclassroom.view.course;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.util.ColorUtils;
import com.chenh.smartclassroom.util.Int2ZHUtil;
import com.chenh.smartclassroom.view.ContentFragment;
import com.chenh.smartclassroom.vo.TimeTableCourse;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by chenh on 2016/9/9.
 */
public class CourseFragment extends ContentFragment {

    LinearLayout weekNames;

    private int itemHeight;

    LinearLayout sections;

    List<LinearLayout> mWeekViews;

    private Handler mHandler;

    private ArrayList<Point> choosedPoints;
    private ArrayList<EmptyTimeTextView> emptyTimeTextViews;

    public static final int ADD_COURSE_OR_PLAN = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        mWeekViews = new ArrayList<>();
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_1));
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_2));
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_3));
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_4));
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_5));
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_6));
        mWeekViews.add((LinearLayout) rootView.findViewById(R.id.weekPanel_7));

        sections = (LinearLayout) rootView.findViewById(R.id.sections);
        weekNames = (LinearLayout) rootView.findViewById(R.id.weekNames);

        choosedPoints = new ArrayList<>();
        emptyTimeTextViews = new ArrayList<>();

        itemHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.sectionHeight);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int what = msg.what;
                switch (what) {
                    case 1:
                        initWeekCourseView();
                }
            }
        };

        if (LocalCourse.courses == null) {
            LocalCourse.courses = new ArrayList<>();
            LocalCourse.getCourse(mHandler);

        } else if (LocalUser.getLocalUser().getUser().onLine && LocalCourse.courses != null) {
            initWeekCourseView();
        }
        initWeekNameView();
        initSectionView(11);
        return rootView;
    }

    /**
     * 顶部周一到周日的布局
     **/
    private void initWeekNameView() {
        int today = getDate();
        int week = getWeekDay();
        int startDay = today - (week - 1);

        for (int i = 0; i < mWeekViews.size() + 1; i++) {
            TextView tvWeekName = new TextView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

            if (i != 0) {
                lp.weight = 1;
                tvWeekName.setText(startDay + "\n" + "周" + Int2ZHUtil.intToZH(i));
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
     *
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
            mWeekViews.get(i).removeAllViews();
            initOneDayCourseTable(mWeekViews.get(i), getCourses()[i], i);
        }
    }

    public void initOneDayCourseTable(LinearLayout ll, List<TimeTableCourse> data, int weekDayIndex) {
        if (ll == null || data == null || data.size() < 1)
            return;
        int[] emptyTimeTag = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        Collections.sort(data, new Comparator<TimeTableCourse>() {
            @Override
            public int compare(TimeTableCourse o1, TimeTableCourse o2) {
                return o1.startSection - o2.startSection;
            }
        });

        TimeTableCourse firstCourse = data.get(0);
        for (int i = 0; i < data.size(); i++) {
            final TimeTableCourse course = data.get(i);
            if (course.startSection == 0 || course.lastSection == 0)
                return;

            //--------------修改标志位-----------------
            for (int courseTagCounter = course.startSection; courseTagCounter < course.startSection + course.lastSection; courseTagCounter++)
                emptyTimeTag[courseTagCounter - 1] = 1;
            //---------------修改完毕------------------

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
                for (int j = 0; j < course.startSection - 1; j++)
                    addEmptyTimeViewToHelpInit(ll, weekDayIndex, j);
            } else {
                for (int j = (firstCourse.startSection + firstCourse.lastSection); j < course.startSection; j++)
                    addEmptyTimeViewToHelpInit(ll, weekDayIndex, j);
            }
            frameLp.setMargins(0, 0, 0, 0);

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
                    Intent intent;
                    if (course.courseType.equals("自定义"))
                        intent = new Intent(getActivity(),PlanDetailActivity.class);
                    else
                        intent = new Intent(getActivity(),CourseDetailActivity.class);
                    intent.putExtra("course",course);
                    startActivity(intent);
                }
            });
        }

        addEmptyTimeViewAfterTheLast(emptyTimeTag, ll, weekDayIndex);

    }

    private void addEmptyTimeViewToHelpInit(LinearLayout ll, final int weekDayIndex, final int sectionIndex) {
        final CornerTextView tv = new EmptyTimeTextView(getActivity(),
                ColorUtils.getCourseBgColor(100),
                dip2px(getActivity(), 3));

        FrameLayout emptyLayout = new FrameLayout(getActivity());
        LinearLayout.LayoutParams emptyLayoutLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                itemHeight);
        LinearLayout.LayoutParams tvLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        emptyLayoutLp.setMargins(0, 0, 0, 0);

        tv.setLayoutParams(tvLp);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(12);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setText("");


        emptyLayout.setLayoutParams(emptyLayoutLp);
        emptyLayout.setPadding(2, 2, 2, 2);
        emptyLayout.addView(tv);
        ll.addView(emptyLayout);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ((EmptyTimeTextView) v).getText().toString();
                if (s.equals("")) {
                    ((EmptyTimeTextView) v).setText("+");
                    choosedPoints.add(new Point(weekDayIndex, sectionIndex));
                    emptyTimeTextViews.add((EmptyTimeTextView) v);
                } else if (s.equals("+")) {
                    ((EmptyTimeTextView) v).setText("");
                    clearSingleItem(weekDayIndex, sectionIndex);
                }
            }
        });

        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String s = ((EmptyTimeTextView) v).getText().toString();
                if (s.equals(""))
                    return false;
                else if (s.contains("+")) {
                    addCourseOrPlans();
                }
                return true;
            }
        });

    }

    private void addEmptyTimeViewAfterTheLast(int[] emptyTimeTag, LinearLayout ll, int weekDayIndex) {
        int lastIndexOfUsed = -1;
        for (int i = emptyTimeTag.length - 1; i >= 0; i--) {
            if (emptyTimeTag[i] == 1) {
                lastIndexOfUsed = i;
                break;
            }
        }
        for (int j = lastIndexOfUsed + 1; j < emptyTimeTag.length; j++)
            addEmptyTimeViewToHelpInit(ll, weekDayIndex, j + 1);
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

    private int getDate() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * 当前月份
     */
    private int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    private List<TimeTableCourse>[] getCourses() {
        ArrayList<TimeTableCourse> localCourse = LocalCourse.courses;
        List<TimeTableCourse>[] timeTableCourses = new ArrayList[7];

        for (int i = 0; i < timeTableCourses.length; i++)
            timeTableCourses[i] = new ArrayList<>();

        for (TimeTableCourse t : localCourse)
            timeTableCourses[getNumFromCh(t.courseDate)].add(t);

        courseTest(timeTableCourses);
        return timeTableCourses;
    }

    /**
     * 对传入的TimeTableCourse进行数据检查
     *
     * @param t
     */
    private void courseTest(List<TimeTableCourse>[] t) {
        //排序并去重
        for (int i = 0; i < t.length; i++) {
            List<TimeTableCourse> oneDayList = t[i];
            Collections.sort(oneDayList, new Comparator<TimeTableCourse>() {
                @Override
                public int compare(TimeTableCourse t1, TimeTableCourse t2) {
                    return t1.startSection - t2.startSection;
                }
            });
            for (int j = 0; j + 1 < oneDayList.size(); j++) {
                TimeTableCourse t1 = oneDayList.get(j);
                TimeTableCourse t2 = oneDayList.get(j + 1);
                if (t1.startSection == t2.startSection) {
                    oneDayList.remove(t2);
                    j--;
                }
            }
        }
        System.out.print("hhh");
    }

    private int getNumFromCh(String s) {
        ArrayList<String> ch = new ArrayList<>();
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

    private void addCourseOrPlans() {
        Intent intent = new Intent(getActivity(), CourseOrPlanActivity.class);
        intent.putExtra("time", choosedPoints);
        startActivityForResult(intent, ADD_COURSE_OR_PLAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            clearRecord();
            return;
        }
        clearRecord();
        mHandler.sendMessage(mHandler.obtainMessage(1, ""));

    }

    /**
     * 撤销选中
     */
    private void clearRecord() {
        choosedPoints.clear();
        for (EmptyTimeTextView emptyTimeTextView : emptyTimeTextViews) {
            emptyTimeTextView.setText("");
        }
        emptyTimeTextViews.clear();
    }

    private void clearSingleItem(int x, int y) {
        int index = -1;
        for (Point p : choosedPoints) {
            if (p.x == x && p.y == y) {
                index = choosedPoints.indexOf(p);
                break;
            }
        }
        if (index != -1) {
            choosedPoints.remove(index);
            emptyTimeTextViews.remove(index);
        }

    }
}
