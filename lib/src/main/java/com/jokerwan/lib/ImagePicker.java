package com.jokerwan.lib;

import android.app.Activity;
import android.content.Intent;

import com.jokerwan.lib.activity.ImagePickerActivity;
import com.jokerwan.lib.bean.MediaFile;
import com.jokerwan.lib.manager.ConfigManager;
import com.jokerwan.lib.utils.ImageLoader;

import java.util.ArrayList;


public class ImagePicker {

    public static final String EXTRA_SELECT_IMAGES = "selectItems";
    private static volatile ImagePicker mImagePicker;


    public static ImagePicker getInstance() {
        if (mImagePicker == null) {
            synchronized (ImagePicker.class) {
                if (mImagePicker == null) {
                    mImagePicker = new ImagePicker();
                }
            }
        }
        return mImagePicker;
    }


    //设置标题
    public ImagePicker setTitle(String title) {
        ConfigManager.getInstance().setTitle(title);
        return mImagePicker;
    }

    //设置确定按钮上的文本
    public ImagePicker setConfirmBtnText(String btnText) {
        ConfigManager.getInstance().setBtnText(btnText);
        return mImagePicker;
    }

    //是否展示图片
    public ImagePicker showImage(boolean showImage) {
        ConfigManager.getInstance().setShowImage(showImage);
        return mImagePicker;
    }

    //是否展示视频
    public ImagePicker showVideo(boolean showVideo) {
        ConfigManager.getInstance().setShowVideo(showVideo);
        return mImagePicker;
    }


    //图片最大选择数
    public ImagePicker setMaxCount(int maxCount) {
        ConfigManager.getInstance().setMaxCount(maxCount);
        return mImagePicker;
    }


    //设置图片加载器
    public ImagePicker setImageLoader(ImageLoader imageLoader) {
        ConfigManager.getInstance().setImageLoader(imageLoader);
        return mImagePicker;
    }

    //设置图片选择历史记录
    public ImagePicker setImages(ArrayList<MediaFile> images) {
        ConfigManager.getInstance().setImages(images);
        return mImagePicker;
    }


    public void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }
}
