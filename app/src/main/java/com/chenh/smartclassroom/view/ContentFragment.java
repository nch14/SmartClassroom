package com.chenh.smartclassroom.view;

/**
 * Created by chenh on 2016/7/2.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.view.blog.BlogFragment;
import com.chenh.smartclassroom.view.classroom.OpenClassroomListFragment;
import com.chenh.smartclassroom.view.course.CourseFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContentFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    public ContentFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ContentFragment newInstance(int sectionNumber) {
        ContentFragment fragment=null;
        switch (sectionNumber){
            case 1:
                fragment = new OpenClassroomListFragment();
                break;
            case 2:
                fragment = new BlogFragment();
                break;
            case 3:
                fragment = new CourseFragment();
                break;
            case 4:
                fragment = new MyMessageFragment();
                break;
            case 5:
                fragment = new UserCenterFragment();
                break;
            default:
                fragment = new ContentFragment();
        }
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}





