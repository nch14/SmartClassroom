package com.chenh.smartclassroom.view.blog;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalMessage;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.net.Client;
import com.chenh.smartclassroom.net.NetController;
import com.chenh.smartclassroom.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class SendBlogActivity extends AppCompatActivity {
    private EditText content;
    private ImageView topicView;
    private Switch aSwitch;

    private String topic="暂无主题";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_blog);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("发推文");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        content= (EditText) findViewById(R.id.content);

        topicView= (ImageView) findViewById(R.id.image_view_topic);

        aSwitch= (Switch) findViewById(R.id.switch1);

        topicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm=getFragmentManager();
                TopicFillDiaolog dialog= new TopicFillDiaolog();
                dialog.show(fm,"");
            }
        });
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
            if (content.getText().toString()!=null){
                String contentText=content.getText().toString();
                contentText.replace(topic,"");
                contentText.replace("#","");
                JSONObject json=new JSONObject();
                try {
                    json.put("op", NetController.CREATE_BLOG_MESSAGE);
                    json.put("text",contentText);
                    json.put("sendTime", TimeUtil.getTime(new Date()));
                    json.put("tag",topic);
                    json.put("maxId", LocalMessage.getLocalMessage().getMaxId());
                    if (aSwitch.isChecked()){
                        json.put("author", "000000000");
                    }else {
                        json.put("author", LocalUser.getLocalUser().getUserId());
                    }
                    NetController.getNetController().addTask(json.toString());
                    Toast.makeText(SendBlogActivity.this,"已发布",Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(SendBlogActivity.this,"发送内容不能为空",Toast.LENGTH_SHORT).show();
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setTopic(String s){
        String orignal=content.getText().toString();
        orignal= orignal.replace(topic,"");
        orignal= orignal.replace("#","");
        content.setText("#"+s+"#"+orignal);
        topic=s;
    }
}
