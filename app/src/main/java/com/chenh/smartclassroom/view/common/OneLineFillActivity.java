package com.chenh.smartclassroom.view.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.view.ConfigActivity;

public class OneLineFillActivity extends ConfigActivity {
    protected EditText mInputView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_one_line_fill);
        super.onCreate(savedInstanceState);

        mInputView= (EditText) findViewById(R.id.item_value);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_commit) {
            Intent intent=new Intent();
            intent.putExtra("ITEM_VALUE",mInputView.getText().toString());
            setResult(RESULT_OK,intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
