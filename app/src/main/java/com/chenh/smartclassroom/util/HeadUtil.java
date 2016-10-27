package com.chenh.smartclassroom.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.chenh.smartclassroom.R;
import com.chenh.smartclassroom.net.NetController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by chenh on 2016/8/4.
 */
public class HeadUtil {


    public static int getHeadId(String userId) {
        switch (userId) {
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

    public static void setHeadView(ImageView headView, String id) {
        Bitmap head = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/head/" + id + ".jpg");
        if (head == null) {
            headView.setImageResource(R.drawable.head);
            prepareHeads(id);
            return;
        }else {
            //update head

        }
        headView.setImageBitmap(head);
    }


    private static void prepareHeads(final String id) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    json.put("op", NetController.GET_HEAD);
                    json.put("id", id);

                    final String message = json.toString();

                    String result = NetController.getNetController().callInstantNetService(message);
                    JSONObject back = new JSONObject(result);
                    boolean success = back.getBoolean("status");
                    if (success) {
                        String s = back.getString("image");
                        Log.e("IMAGE",s);
                        File file=new File(Environment.getExternalStorageDirectory(), "/head/"+id + ".jpg");

                        if (!file.getParentFile().exists())
                            file.getParentFile().mkdirs();
                        Base64Image.getImage(s, Environment.getExternalStorageDirectory().getPath() + "/head/" + id + ".jpg");
                    } else {
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
