package com.jokerwan.lib.utils;


import com.jokerwan.lib.bean.MediaFile;

import java.util.ArrayList;
import java.util.List;


public class DataHelper {

    private static volatile DataHelper mDataUtilInstance;
    private List<MediaFile> mData = new ArrayList<>();

    public static DataHelper getInstance() {
        if (mDataUtilInstance == null) {
            synchronized (DataHelper.class) {
                if (mDataUtilInstance == null) {
                    mDataUtilInstance = new DataHelper();
                }
            }
        }
        return mDataUtilInstance;
    }

    public List<MediaFile> getMediaData() {
        return mData;
    }

    public void setMediaData(List<MediaFile> data) {
        this.mData = data;
    }


}
