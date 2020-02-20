package com.jokerwan.lib.task;

import android.content.Context;

import com.jokerwan.lib.bean.MediaFile;
import com.jokerwan.lib.loader.ImageScanner;
import com.jokerwan.lib.loader.MediaHandler;
import com.jokerwan.lib.loader.MediaLoadCallback;

import java.util.ArrayList;

/**
 * 媒体库扫描任务（图片）
 */
public class ImageLoadTask implements Runnable {

    private ImageScanner mImageScanner;
    private MediaLoadCallback mMediaLoadCallback;

    public ImageLoadTask(Context context, MediaLoadCallback mediaLoadCallback) {
        this.mMediaLoadCallback = mediaLoadCallback;
        mImageScanner = new ImageScanner(context);
    }

    @Override
    public void run() {
        //存放所有照片
        ArrayList<MediaFile> imageFileList = new ArrayList<>();
        if (mImageScanner != null) {
            imageFileList = mImageScanner.queryMedia();
        }
        if (mMediaLoadCallback != null) {
            mMediaLoadCallback.loadMediaSuccess(MediaHandler.getSortMediaFiles(imageFileList, null));
        }
    }

}
