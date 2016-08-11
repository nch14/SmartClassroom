package com.chenh.smartclassroom.view.blog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.chenh.smartclassroom.R;

import java.util.ArrayList;

/**
 * Created by chenh on 2016/8/1.
 */
public class TopicFillDiaolog extends DialogFragment {
    private EditText editText;

    private ListView mListView;
    private ArrayList<String> topics;
    private ArrayAdapter adapter;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_topic,null);

        editText= (EditText) v.findViewById(R.id.editText3);

        topics=new ArrayList<>();
        initTopics();
        mListView= (ListView) v.findViewById(R.id.listView);
        adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,topics);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editText.setText(topics.get(i));
            }
        });



        Dialog dialog=new AlertDialog.Builder(getActivity()).setView(v).setTitle("选择一个话题吧").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (editText.getText()!=null){
                    ((SendBlogActivity)getActivity()).setTopic(editText.getText().toString());
                }
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private void initTopics(){
        topics.add("约自习");
        topics.add("约夜跑");
        topics.add("每日打卡");
        topics.add("C++也大神");
    }
}
