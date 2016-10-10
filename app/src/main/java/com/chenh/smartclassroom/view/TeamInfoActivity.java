package com.chenh.smartclassroom.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chenh.smartclassroom.R;

public class TeamInfoActivity extends AppCompatActivity {
    private TextView guessName;
    private TextView guessButton;
    private TextView egg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("关于");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        guessButton= (TextView) findViewById(R.id.egg_guess);
        guessName= (TextView) findViewById(R.id.guess_name);
        egg= (TextView) findViewById(R.id.eggs);

        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guessName.getText()!=null){
                    String input =guessName.getText().toString();
                    if(input.equals("飞燕小学妹")){
                        guessName.setText("");
                        egg.setText("感谢超级可爱热情的小学妹带来的青春活力~");
                    }else if (input.contains("飞燕")){
                        egg.setText("");
                        Toast.makeText(TeamInfoActivity.this,"鸿雁长飞光不度，鱼龙潜跃水成文~",Toast.LENGTH_SHORT).show();
                    }else if (input.contains("学妹")){
                        egg.setText("");
                        Toast.makeText(TeamInfoActivity.this,"世界上有那么多学妹、却有你们这么多怪学长！",Toast.LENGTH_SHORT).show();
                    }else {
                        egg.setText("");
                        Toast.makeText(TeamInfoActivity.this,"不知道你在说什么……",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
