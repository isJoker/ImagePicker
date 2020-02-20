package com.jokerwan.lib.manager;


import com.jokerwan.lib.bean.MediaFile;
import com.jokerwan.lib.utils.ImageLoader;

import java.util.ArrayList;

/**
 * 统一配置管理类
 */
public class ConfigManager {

    public static final int SELECT_MODE_SINGLE = 0;
    public static final int SELECT_MODE_MULTI = 1;

    //标题
    private String title;
    //确认按钮上的标题
    private String btnText;
    //是否显示图片，默认显示
    private boolean showImage = true;
    //是否显示视频，默认显示
    private boolean showVideo = true;
    //选择模式，默认单选
    private int selectionMode = SELECT_MODE_SINGLE;
    //最大选择数量，默认为1
    private int maxCount = 1;
    //上一次选择的图片集合
    private ArrayList<MediaFile> images;

    private ImageLoader imageLoader;

    private static volatile ConfigManager mConfigManager;

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        if (mConfigManager == null) {
            synchronized (SelectionManager.class) {
                if (mConfigManager == null) {
                    mConfigManager = new ConfigManager();
                }
            }
        }
        return mConfigManager;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public boolean isShowImage() {
        return showImage;
    }

    public void setShowImage(boolean showImage) {
        this.showImage = showImage;
    }

    public boolean isShowVideo() {
        return showVideo;
    }

    public void setShowVideo(boolean showVideo) {
        this.showVideo = showVideo;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        if (maxCount > 1) {
            setSelectionMode(SELECT_MODE_MULTI);
        }
        this.maxCount = maxCount;
    }

    public int getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(int mSelectionMode) {
        this.selectionMode = mSelectionMode;
    }


    public ArrayList<MediaFile> getImages() {
        return images;
    }

    public void setImages(ArrayList<MediaFile> images) {
        this.images = images;
    }


    public ImageLoader getImageLoader() throws Exception {
        if (imageLoader == null) {
            throw new Exception("imageLoader is null");
        }
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }
}
