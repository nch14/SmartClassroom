package com.chenh.smartclassroom.view.classroom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.chenh.smartclassroom.R;

import com.chenh.smartclassroom.view.common.DialogCallBack;

/**
 * Created by carlos on 2016/10/12.
 */

public class DateSelectDialog extends DialogFragment {

    private DatePicker mDatePicker;

    private DialogCallBack dialogCallBack;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_select_date,null);

        mDatePicker = (DatePicker) v.findViewById(R.id.datePicker);



        Dialog dialog=new AlertDialog.Builder(getActivity()).setView(v).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int day = mDatePicker.getDayOfMonth();
                int month = mDatePicker.getMonth();
                int year = mDatePicker.getYear();
                dialogCallBack.callBack(""+year+"/"+month+"/"+day);
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

    public static DateSelectDialog getDialog(DialogCallBack dialogCallBack){
        DateSelectDialog dateSelectDialog =new DateSelectDialog();
        dateSelectDialog.dialogCallBack = dialogCallBack;
        return dateSelectDialog;
    }

}
