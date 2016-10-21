package com.chenh.smartclassroom.view.classroom;

import java.util.ArrayList;

/**
 * Created by carlos on 2016/10/18.
 */

public class LocalItem {

    private static LocalItem localItem;

    private ArrayList<String> items;

    public static ArrayList<String> getItems(){
        if (localItem == null)
            localItem = new LocalItem();
        return localItem.items;
    }

    public static void addItem(String s){
        if (localItem == null)
            localItem = new LocalItem();
        localItem.items.add(s);
    }

    private LocalItem(){
        items = new ArrayList<>();
        items.add("今天（17号）上午在教室丢了一串钥匙……有见到的求联系188 8888 8888");
    }


}
