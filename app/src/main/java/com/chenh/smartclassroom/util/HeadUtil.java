package com.chenh.smartclassroom.util;

import com.chenh.smartclassroom.R;

/**
 * Created by chenh on 2016/8/4.
 */
public class HeadUtil {



    public static int getHeadId(String userId){
        switch (userId){
            case "000000000":
                return R.drawable.head;
            case "141250094":
                return R.drawable.head2;
            case "141250095":
                return R.drawable.head3;
            case "141250096":
                return R.drawable.head4;
            case "141250097":
                return R.drawable.head;
            case "141250098":
                return R.drawable.head5;
            case "141250099":
                return R.drawable.head6;
            case "141250100":
                return R.drawable.head4;
            case "141250001":
                return R.drawable.head2;
            default:
                return R.drawable.head;
        }
    }
}
