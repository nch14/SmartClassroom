package com.chenh.smartclassroom.net;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by chenh on 2016/9/14.
 */
public class NetCheckTool {

    public static boolean checkNet(){
        try {
           Document index = Jsoup.connect("https://www.baidu.com/").timeout(500).get();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Document index = Jsoup.connect("http://www.qq.com/").timeout(500).get();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Document index = Jsoup.connect("https://www.google.com.hk/").timeout(500).get();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
