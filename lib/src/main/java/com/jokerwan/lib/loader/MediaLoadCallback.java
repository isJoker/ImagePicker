package com.jokerwan.lib.loader;


import com.jokerwan.lib.bean.MediaFile;

import java.util.List;

public interface MediaLoadCallback {
    void loadMediaSuccess(List<MediaFile> mediaFileList);
}
