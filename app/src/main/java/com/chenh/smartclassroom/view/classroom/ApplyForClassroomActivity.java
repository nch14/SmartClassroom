package com.chenh.smartclassroom.view.classroom;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalAvailableClassroom;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.net.Client;
import com.chenh.smartclassroom.util.TimeUtil;
import com.chenh.smartclassroom.view.LoadingDiaolog;
import com.chenh.smartclassroom.view.listView.view.WaterDropListView;
import com.chenh.smartclassroom.vo.BlogMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ApplyForClassroomActivity extends AppCompatActivity {

    private ImageView startTime;
    private ImageView endTime;

    private TextView startTimeView;
    private TextView endTimeView;

    private TextView classroomView;

    private boolean setStart;
    private boolean setEnd;

    private Handler mHandler;
    private ListView listView;
    private ArrayAdapter mAdpater;
    private ArrayList<String> data;

    public static final int START_TIME = 1;
    public static final int END_TIME = 2;
    public static final int LOAD_CLASSROOM_FINISHED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_classroom);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("借教室");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int what = msg.what;
                String message = msg.obj.toString();

                switch (what) {
                    case LOAD_CLASSROOM_FINISHED:
                        mAdpater.notifyDataSetChanged();
                        break;
                }
            }
        };

        /*//透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/

        classroomView= (TextView) findViewById(R.id.textView6);

        LocalAvailableClassroom.getLocalClassroom().addHandler(mHandler);
        data = LocalAvailableClassroom.getLocalClassroom().getClassroom();
        listView = (ListView) findViewById(R.id.listView);
        mAdpater = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(mAdpater);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s=data.get(i);
                classroomView.setText(s);
            }
        });


        startTime = (ImageView) findViewById(R.id.imageView_start_time);
        startTime.setOnClickListener(new InnerListener());

        endTime = (ImageView) findViewById(R.id.imageView_end_time);
        endTime.setOnClickListener(new InnerListener());

        startTimeView = (TextView) findViewById(R.id.start_time_view);
        startTimeView.setText("");
        endTimeView = (TextView) findViewById(R.id.end_time_view);
        endTimeView.setText("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_normal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_commit) {
            Toast.makeText(ApplyForClassroomActivity.this,"请求已发出",Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class InnerListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int id = view.getId();
            FragmentManager fm;
            TimeSelectDialog dialog;
            switch (id) {
                case R.id.imageView_start_time:
                    fm = getFragmentManager();
                    dialog = TimeSelectDialog.getTimeSelectDialog(START_TIME);
                    dialog.show(fm, "");
                    break;
                case R.id.imageView_end_time:
                    fm = getFragmentManager();
                    dialog = TimeSelectDialog.getTimeSelectDialog(END_TIME);
                    dialog.show(fm, "");
                    break;
            }
        }
    }


    private void askForClassroom() {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    public void setTime(int operation, String s) {
        switch (operation) {
            case START_TIME:
                startTimeView.setText(s);
                setStart = true;
                break;
            case END_TIME:
                endTimeView.setText(s);
                setEnd = true;
                break;
        }

        if (setEnd && setStart) {
            Date start = TimeUtil.getDate(startTimeView.getText().toString() + ":000");
            Date end = TimeUtil.getDate(endTimeView.getText().toString() + ":000");
            if (start.getTime() > end.getTime()) {
                Toast.makeText(this, "开始时间必须在结束时间之前", Toast.LENGTH_SHORT).show();
                switch (operation) {
                    case START_TIME:
                        startTimeView.setText("");
                        setStart = false;
                        break;
                    case END_TIME:
                        endTimeView.setText("");
                        setEnd = false;
                        break;
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("op", Client.ASK_FOR_AVAILABLE_CLASSROOM);
                    jsonObject.put("startTime", TimeUtil.getTime(start));
                    jsonObject.put("endTime", TimeUtil.getTime(end));
                    String message = jsonObject.toString();
                    Client.getClient().addMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
