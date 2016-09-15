package com.chenh.smartclassroom.vo;

import java.util.ArrayList;

/**
 * Created by chenh on 2016/9/9.
 */
public class CourseTool {

    public static ArrayList<CourseTimeAndClassroom> parseDateAndClassroom(String source){
        ArrayList<CourseTimeAndClassroom> courseTimeAndClassrooms=new ArrayList<>();
        System.out.println(source);
        String[] segments =parseItems(source);




        for (int i = 0;i<segments.length;i++){
            CourseTimeAndClassroom courseTimeAndClassroom = new CourseTimeAndClassroom();
            String[] pieces=segments[i].split(" ");
            if (pieces.length<3){
                // TODO: 2016/9/9
            }else {
                //解读周几上课
                courseTimeAndClassroom.courseDate=pieces[0];
                //解读在哪个教室
                courseTimeAndClassroom.courseClassroom=pieces[pieces.length-1];
                //解读第几节课
                courseTimeAndClassroom.courseTime=pieces[1];
                //解读哪几周上课
                if (pieces[pieces.length-2].equals("单周")){
                    courseTimeAndClassroom.weeks=new int[]{1,3,5,7,9,11,13,15,17,19,21};
                }else if (pieces[pieces.length-2].equals("双周")){
                    courseTimeAndClassroom.weeks=new int[]{2,4,6,8,10,12,14,16,18,20,22};
                }else if (pieces[pieces.length-2].contains("-")){
                    pieces[pieces.length-2]=pieces[pieces.length-2].replace("周","");
                    String[] weeks=pieces[pieces.length-2].split("-");
                    int start= Integer.parseInt(weeks[0]);
                    int end= Integer.parseInt(weeks[1]);
                    courseTimeAndClassroom.weeks=new int[end-start+1];
                    for (int j=start;j<=end;j++){
                        courseTimeAndClassroom.weeks[j-start]=j;
                    }
                }else {
                    courseTimeAndClassroom.weeks=new int[pieces.length-3];
                    for (int j=2;j<pieces.length-1;j++){
                        pieces[j]=pieces[j].replace("第","");
                        pieces[j]=pieces[j].replace("周","");
                        int ii=Integer.parseInt(pieces[j]);
                        courseTimeAndClassroom.weeks[j-2]= Integer.parseInt(pieces[j]);
                    }
                }

            }
            courseTimeAndClassrooms.add(courseTimeAndClassroom);
        }
        return courseTimeAndClassrooms;
    }


    private static String[] parseItems(String source){
        char[] chars=source.toCharArray();
        boolean bool=false;
        int delt=0;
        for (int i=1;i<chars.length;i++){
            if (bool){
                if (chars[i]=='一'||chars[i]=='二'||chars[i]=='三'||chars[i]=='四'||chars[i]=='五'){
                    String a=source.substring(0,i-1);
                    String b=source.substring(i-1);
                    source=source.substring(0,i+delt-1)+"PDD"+source.substring(i+delt-1);
                    System.out.println(source);
                    delt+=3;
                }
            }else {
                if (chars[i]=='周'){
                    bool=true;
                }
            }
        }

        String[] results=source.split("PDD");
        return results;
    }

}


