package com.jokerwan.lib.loader;

import com.jokerwan.lib.bean.MediaFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 媒体处理类（对扫描出来的图片、视频做对应聚类处理）
 */
public class MediaHandler {

    /**
     * 对查询到的图片和视频进行聚类（相册分类）
     *
     * @param imageFileList 图片
     * @param videoFileList 视频
     */
    public static List<MediaFile> getSortMediaFiles(ArrayList<MediaFile> imageFileList, ArrayList<MediaFile> videoFileList) {

        //全部图片、视频文件
        ArrayList<MediaFile> mediaFileList = new ArrayList<>();
        if (imageFileList != null) {
            mediaFileList.addAll(imageFileList);
        }
        if (videoFileList != null) {
            mediaFileList.addAll(videoFileList);
        }

        //对媒体数据进行排序
        Collections.sort(mediaFileList, new Comparator<MediaFile>() {
            @Override
            public int compare(MediaFile o1, MediaFile o2) {
                if (o1.getDateToken() > o2.getDateToken()) {
                    return -1;
                } else if (o1.getDateToken() < o2.getDateToken()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        return mediaFileList;
    }

}
