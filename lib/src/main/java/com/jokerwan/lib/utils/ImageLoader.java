package com.jokerwan.lib.utils;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * 开放图片加载接口
 */
public interface ImageLoader extends Serializable {

    /**
     * 缩略图加载方案
     *
     * @param imageView 图片
     * @param imagePath 图片路径
     */
    void loadImage(ImageView imageView, String imagePath);

    /**
     * 大图加载方案
     *
     * @param imageView 图片
     * @param imagePath 图片路径
     */
    void loadPreImage(ImageView imageView, String imagePath);


    /**
     * 缓存管理
     */
    void clearMemoryCache();

}
