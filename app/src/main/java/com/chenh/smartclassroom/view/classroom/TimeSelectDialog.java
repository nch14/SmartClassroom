package com.chenh.smartclassroom.view.classroom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.util.TimeUtil;

import java.util.Calendar;

/**
 * Created by chenh on 2016/8/1.
 */
public class TimeSelectDialog extends DialogFragment {
    NumberPicker monthPicker;
    NumberPicker dayPicker;
    NumberPicker hourPicker;
    NumberPicker minPicker;

    int operation;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_select_time,null);

        Calendar calendar = Calendar.getInstance();

        monthPicker = (NumberPicker) v.findViewById(R.id.month);
        monthPicker.setMaxValue(12);
        monthPicker.setMinValue(1);
        monthPicker.setValue(calendar.get(Calendar.MONTH)+1);
        monthPicker.setWrapSelectorWheel(false);

        dayPicker = (NumberPicker) v.findViewById(R.id.day);
        dayPicker.setMaxValue(31);
        dayPicker.setMinValue(1);
        dayPicker.setValue(calendar.get(Calendar.DATE));
        dayPicker.setWrapSelectorWheel(false);

        hourPicker = (NumberPicker) v.findViewById(R.id.hour);
        hourPicker.setMaxValue(22);
        hourPicker.setMinValue(8);
        hourPicker.setValue(calendar.get(Calendar.HOUR));
        hourPicker.setWrapSelectorWheel(false);

        minPicker = (NumberPicker) v.findViewById(R.id.minite);
        minPicker.setMaxValue(59);
        minPicker.setMinValue(0);
        minPicker.setValue(Calendar.MINUTE);
        minPicker.setWrapSelectorWheel(false);

        dayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int maxDay= TimeUtil.maxDay(monthPicker.getValue());
                if (maxDay<numberPicker.getValue()){
                    Toast.makeText(getActivity(),"请检查：这个月没有这一天",Toast.LENGTH_SHORT).show();
                    numberPicker.setValue(i1);
                }
            }
        });



        Dialog dialog=new AlertDialog.Builder(getActivity()).setView(v).setTitle("请选择时间").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int month=monthPicker.getValue();
                int day=dayPicker.getValue();
                int hour=hourPicker.getValue();
                int min=minPicker.getValue();
               // ((ApplyForClassroomActivity)getActivity()).setTime(operation,Calendar.getInstance().get(Calendar.YEAR)+"-"+((month>9)?month:"0"+month)+"-"+((day>9)?day:"0"+day)+" "+((hour>9)?hour:"0"+hour)+":"+((min>9)?min:"0"+min));
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

    public static TimeSelectDialog getTimeSelectDialog(int i){
        TimeSelectDialog timeSelectDialog=new TimeSelectDialog();
        timeSelectDialog.operation=i;
        return timeSelectDialog;
    }
}
