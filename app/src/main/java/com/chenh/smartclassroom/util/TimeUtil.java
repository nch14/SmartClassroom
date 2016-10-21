package com.chenh.smartclassroom.util;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenh on 2016/8/1.
 */
public class TimeUtil {

    /**
     * 将Date对象转化为字符串
     * @param date 日期对象
     * @return 将时间对象的date转化为对应的年月日格式的字符串
     */
    public static String getViewDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    public static String getViewTime(Date date){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    public static String getTotalDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    public static boolean aBeforeB(String a,String b){
        String[] A=a.split(":");
        int numA=Integer.parseInt(A[0])*60+Integer.parseInt(A[1]);

        String[] B=b.split(":");
        int numB=Integer.parseInt(B[0])*60+Integer.parseInt(B[1]);

        if (numA<numB)
            return true;
        return false;
    }

    public static Date getDate(String s){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
        try {
            Date d=dateFormat.parse(s);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTime(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
        String dateStr = dateFormat.format(date);
        return dateStr;
    }


    public static int maxDay(int month){
        int[] b=new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
        return b[month-1];
    }

    public static int getWeek(){
        String start="2016-08-29 0:0:0";
        Date startDate=getDate(start);

        Date date=new Date();
        int result =  (int)((date.getTime()-startDate.getTime())/1000/3600/24/7)+1;

        return result;
    }

}
