package com.jokerwan.lib.manager;


import com.jokerwan.lib.bean.MediaFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 媒体选择集合管理类
 */
public class SelectionManager {

    private static volatile SelectionManager mSelectionManager;
    private List<MediaFile> selectImages = new ArrayList<>();
    private int maxCount = 1;

    public static SelectionManager getInstance() {
        if (mSelectionManager == null) {
            synchronized (SelectionManager.class) {
                if (mSelectionManager == null) {
                    mSelectionManager = new SelectionManager();
                }
            }
        }
        return mSelectionManager;
    }


    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    /**
     * 获取当前所选图片集合
     */
    public List<MediaFile> getSelects() {
        return selectImages;
    }

    /**
     * 添加图片到选择集合
     */
    public boolean addImageToSelectList(MediaFile mediaFile) {
        if (isImageSelect(mediaFile)) {
            selectImages.remove(getSelectImagePosition(mediaFile));
            return true;
        } else {
            if (selectImages.size() < maxCount) {
                return selectImages.add(mediaFile);
            } else {
                return false;
            }
        }
    }

    /**
     * 添加图片到选择集合
     */
    public void addImagePathsToSelectList(List<MediaFile> mediaFiles) {
        if (mediaFiles != null) {
            for (int i = 0; i < mediaFiles.size(); i++) {
                MediaFile mediaFile = mediaFiles.get(i);
                if (!isImageSelect(mediaFile) && selectImages.size() < maxCount) {
                    selectImages.add(mediaFile);
                }
            }
        }
    }

    /**
     * 判断当前图片是否被选择
     */
    public boolean isImageSelect(MediaFile mediaFile) {
        boolean select = false;
        for (MediaFile file : selectImages) {
            if (file.getPath().equals(mediaFile.getPath())) {
                select = true;
                break;
            }
        }
        return select;
    }


    /**
     * 获取选中的图片在指定图库列表的位置
     */
    public int getSelectImagePosition(List<MediaFile> mMediaFileList, MediaFile mediaFile) {
        if (mMediaFileList.contains(mediaFile)) {
            for (int i = 0; i < mMediaFileList.size(); i++) {
                if (mMediaFileList.get(i).getPath().equals(mediaFile.getPath())) {
                    return i;
                }
            }
        }
        return 0;
    }

    /**
     * 获取选中的图片在已选中的图库列表的位置
     */
    public int getSelectImagePosition(MediaFile mediaFile) {
        for (int i = 0; i < selectImages.size(); i++) {
            if (selectImages.get(i).getPath().equals(mediaFile.getPath())) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 清除已选图片
     */
    public void removeAll() {
        selectImages.clear();
    }

}
