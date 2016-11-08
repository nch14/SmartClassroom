package com.chenh.smartclassroom.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chenh.smartclassroom.R;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                ContentFragment contentFragment=null;
                switch (id) {
                    case R.id.menu_item_classroom:
                        contentFragment = ContentFragment.newInstance(1);
                        break;
                    case R.id.menu_item_module:
                        contentFragment = ContentFragment.newInstance(2);
                        break;
                    case R.id.menu_item_course:
                        contentFragment = ContentFragment.newInstance(3);
                        break;
                    case R.id.menu_item_message:
                        contentFragment = ContentFragment.newInstance(4);
                        break;
                    case R.id.menu_item_center:
                        contentFragment = ContentFragment.newInstance(5);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,contentFragment).commit();
                return false;
            }
        });
    }

}
