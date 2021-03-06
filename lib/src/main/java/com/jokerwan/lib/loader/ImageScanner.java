package com.jokerwan.lib.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.jokerwan.lib.bean.MediaFile;


/**
 * 媒体库扫描类(图片)
 */
public class ImageScanner extends AbsMediaScanner<MediaFile> {

    public ImageScanner(Context context) {
        super(context);
    }

    @Override
    protected Uri getScanUri() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String[] getProjection() {
        return new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
        };
    }

    @Override
    protected String getSelection() {
        return MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?" + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?";
    }

    @Override
    protected String[] getSelectionArgs() {
        return new String[]{"image/jpeg", "image/png", "image/gif"};
    }

    @Override
    protected String getOrder() {
        return MediaStore.Images.Media.DATE_TAKEN + " desc";
    }


    @Override
    protected MediaFile parse(Cursor cursor) {

        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        long dateToken = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));

        MediaFile mediaFile = new MediaFile();
        mediaFile.setPath(path);
        mediaFile.setDateToken(dateToken);

        return mediaFile;
    }


}
