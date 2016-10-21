package com.chenh.smartclassroom.view.classroom;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.view.common.DialogCallBack;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class ApplyForClassroomActivity extends AppCompatActivity {

    private ImageView startTimeButton;

    private Spinner mCampusSelect;
    private Spinner mStartSection;
    private Spinner mLastSection;
    private Spinner mTutor;

    private TextView dateSelect;

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
                Toast.makeText(ApplyForClassroomActivity.this,"操作取消",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        startTimeButton = (ImageView) findViewById(R.id.imageView_start_time);
        startTimeButton.setOnClickListener(new InnerListener());

        ArrayAdapter<String> campusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,new String[]{"仙林","鼓楼"});
        campusAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        mCampusSelect= (Spinner) findViewById(R.id.campus_select);
        mCampusSelect.setAdapter(campusAdapter);

        ArrayAdapter<String> startSectionAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"1（8：00-）","2（9：00-）","3（10：10-）","4（11：10-）","5（14：00-）",
                        "6（15：00-）","7（16：10-）","8（17：10-）", "9（18：30-）","10（19：30-）",
                        "11（20：30-）","12（21：30-）"});
        campusAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        mStartSection= (Spinner) findViewById(R.id.start_section_select);
        mStartSection.setAdapter(startSectionAdapter);

        ArrayAdapter lastSection = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12});
        campusAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        mLastSection= (Spinner) findViewById(R.id.last_section_select);
        mLastSection.setAdapter(lastSection);

        ArrayAdapter<String> tutorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"院系辅导员","社团联合会","就业指导中心","国际合作与交流处","学生工作处","校团委"});
        campusAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        mTutor= (Spinner) findViewById(R.id.tutor_department);
        mTutor.setAdapter(tutorAdapter);

        dateSelect = (TextView) findViewById(R.id.start_time_view);
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
            Toast.makeText(ApplyForClassroomActivity.this,"申请已提交，请等待处理结果",Toast.LENGTH_SHORT).show();
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
            DateSelectDialog dialog;
            switch (id) {
                case R.id.imageView_start_time:
                    fm = getFragmentManager();
                    dialog = DateSelectDialog.getDialog(new DialogCallBack(){
                        @Override
                        public void callBack(String s) {
                            dateSelect.setText(s);
                        }
                    });
                    dialog.show(fm, "");
                    break;
            }
        }
    }

    private void push(){
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String,String> data=new HashMap<>();
                data.put("userName",mID.getText().toString());
                data.put("password",mPWD.getText().toString());

                try {
                    Connection.Response index = Jsoup.connect("http://jw.nju.edu.cn:8080/jiaowu/login.do").data(data).timeout(1000).execute();
                    Document doc = index.parse();
                    Elements lb=doc.select("label");
                    if (lb.size()!=0) {
                        String text = lb.get(0).text();
                        if (text.equals("用户名或密码错误！")){
                            passwordIsValid=2;
                        }
                    }else
                        passwordIsValid=1;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/




    }

}
