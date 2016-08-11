package com.chenh.smartclassroom.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chenh.smartclassroom.R;

/**
 * Created by chenh on 2016/8/4.
 */
public class MyMessageFragment extends ContentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_message, container, false);

        return rootView;
    }
}
