package com.chenh.smartclassroom.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chenh.smartclassroom.R;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by carlos on 2016/10/6.
 */

public class HeadUpdateActivity extends TakePhotoActivity implements View.OnClickListener {
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View contentView= LayoutInflater.from(this).inflate(R.layout.activity_head_update,null);
        setContentView(R.layout.activity_head_update);

        id=getIntent().getStringExtra("id");

        findViewById(R.id.button_choose_photo).setOnClickListener(this);

        findViewById(R.id.button_take_photo).setOnClickListener(this);
    }

    public void onClick(View view) {
        TakePhoto takePhoto=getTakePhoto();

        File file=new File(Environment.getExternalStorageDirectory(), "/head/"+id + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
            Uri imageUri = Uri.fromFile(file);

        configCompress(takePhoto);
        switch (view.getId()){
            case R.id.button_choose_photo:
                takePhoto.onPickFromGalleryWithCrop(imageUri,getCropOptions());
                break;
            case R.id.button_take_photo:
                takePhoto.onPickFromCaptureWithCrop(imageUri,getCropOptions());
                break;
            default:
                break;
        }
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        //showImg(result.getImages());
        Intent intent=new Intent();
        intent.putExtra("ITEM_VALUE","");
        setResult(RESULT_OK,intent);
        finish();
    }

    private void showImg(ArrayList<TImage> images) {
        /*Intent intent=new Intent(this,ResultActivity.class);
        intent.putExtra("images",images);
        startActivity(intent);*/
        Log.d("success","output image successfully!");
        Glide.with(this).load(new File(images.get(0).getPath())).into((ImageView)findViewById(R.id.imageView7));

    }




    //压缩
    private void configCompress(TakePhoto takePhoto){
        takePhoto.onEnableCompress(null,false);
        int maxSize= Integer.parseInt("102400");
        boolean showProgressBar=true;
        CompressConfig config= new CompressConfig.Builder().setMaxSize(maxSize).create();
        takePhoto.onEnableCompress(config,showProgressBar);
    }
    //裁剪
    private CropOptions getCropOptions(){
        //配置图片尺寸
        int height= 200;
        int width= 200;
        //使用第三方裁剪工具
        boolean withWonCrop=true;

        CropOptions.Builder builder=new CropOptions.Builder();
        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(withWonCrop);

        return builder.create();
    }




    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }


}
