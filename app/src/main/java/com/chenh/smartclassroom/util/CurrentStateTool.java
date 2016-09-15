package com.chenh.smartclassroom.util;

import android.app.Activity;
import android.os.Handler;

/**
 * Created by chenh on 2016/9/14.
 */
public class CurrentStateTool {
    /**
     * 当前活跃界面的handler
     */
    private static Handler currentHandler;

    private static Activity currentActivity;

    public static Handler getCurrentHandler() {
        return currentHandler;
    }

    public static void setCurrentHandler(Handler currentHandler) {
        CurrentStateTool.currentHandler = currentHandler;
    }
}
