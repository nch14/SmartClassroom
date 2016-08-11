package com.chenh.smartclassroom.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.chenh.smartclassroom.R;

/**
 * Created by chenh on 2016/8/4.
 */
public class UpdateUserDialog extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_loading,null);
        Dialog dialog=new AlertDialog.Builder(getActivity()).setView(v).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
