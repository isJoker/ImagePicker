package com.jokerwan.lib.task;

import android.content.Context;

import com.jokerwan.lib.bean.MediaFile;
import com.jokerwan.lib.loader.MediaHandler;
import com.jokerwan.lib.loader.MediaLoadCallback;
import com.jokerwan.lib.loader.VideoScanner;

import java.util.ArrayList;

/**
 * 媒体库扫描任务（视频）
 */
public class VideoLoadTask implements Runnable {

    private VideoScanner mVideoScanner;
    private MediaLoadCallback mMediaLoadCallback;

    public VideoLoadTask(Context context, MediaLoadCallback mediaLoadCallback) {
        this.mMediaLoadCallback = mediaLoadCallback;
        mVideoScanner = new VideoScanner(context);
    }

    @Override
    public void run() {

        //存放所有视频
        ArrayList<MediaFile> videoFileList = new ArrayList<>();

        if (mVideoScanner != null) {
            videoFileList = mVideoScanner.queryMedia();
        }

        if (mMediaLoadCallback != null) {
            mMediaLoadCallback.loadMediaSuccess(MediaHandler.getSortMediaFiles(null, videoFileList));
        }

    }

}
