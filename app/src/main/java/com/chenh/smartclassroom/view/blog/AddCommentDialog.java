package com.chenh.smartclassroom.view.blog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.model.LocalUser;
import com.chenh.smartclassroom.net.Client;
import com.chenh.smartclassroom.net.NetController;
import com.chenh.smartclassroom.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by chenh on 2016/8/2.
 */
public class AddCommentDialog extends DialogFragment {
    EditText editText;
    Switch aSwitch;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_add_comment,null);

        editText= (EditText) v.findViewById(R.id.editText4);
        aSwitch=(Switch)v.findViewById(R.id.switch2);

        Dialog dialog=new AlertDialog.Builder(getActivity()).setView(v).setTitle("添加评论").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (editText.getText().toString()!=null){
                    //评论
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("op", NetController.COMMENT_BLOG);
                        if (aSwitch.isChecked()){
                            jsonObject.put("userId", "000000000");
                        }else {
                            jsonObject.put("userId", LocalUser.getLocalUser().getUserId());
                        }
                        jsonObject.put("text",editText.getText().toString());
                        long sheetId = ((BlogMessageActivity)getActivity()).getSheetId();
                        jsonObject.put("sheetId",sheetId);
                        jsonObject.put("sendTime", TimeUtil.getTime(new Date()));
                        String  message=jsonObject.toString();
                        NetController.getNetController().addTask(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

}
