package com.jokerwan.lib.loader;


import com.jokerwan.lib.bean.MediaFolder;

import java.util.List;

public interface MediaLoadCallback {
    void loadMediaSuccess(List<MediaFolder> mediaFolderList);
}
