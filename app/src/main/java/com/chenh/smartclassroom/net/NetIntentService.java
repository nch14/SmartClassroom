package com.chenh.smartclassroom.net;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NetIntentService extends IntentService {
    // TODO: Rename parameters
    private static final String USER_ID = "com.chenh.smartclassroom.net.extra.USER_ID";
    private static final String USER_PASSWORD = "com.chenh.smartclassroom.net.extra.USER_PASSWORD";

    public NetIntentService() {
        super("NetIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String userID, String userPWD) {
        Intent intent = new Intent(context, NetIntentService.class);
        intent.putExtra(USER_ID, userID);
        intent.putExtra(USER_PASSWORD, userPWD);
        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        NetController.createNetController(intent.getStringExtra(USER_ID),intent.getStringExtra(USER_PASSWORD));
        while (true){

        }
    }

    public void makeToast(){
        Toast.makeText(this,"网络无响应",Toast.LENGTH_SHORT).show();
    }
}

