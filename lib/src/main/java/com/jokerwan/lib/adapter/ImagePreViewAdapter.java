package com.jokerwan.lib.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.jokerwan.lib.R;
import com.jokerwan.lib.bean.MediaFile;
import com.jokerwan.lib.manager.ConfigManager;

import java.util.List;

/**
 * 大图浏览适配器（并不是比较好的方案，后期会用RecyclerView来实现）
 */
public class ImagePreViewAdapter extends PagerAdapter {

    private List<MediaFile> mMediaFileList;
    private OnVideoClickListener onVideoClickListener;

    public ImagePreViewAdapter(OnVideoClickListener onVideoClickListener,List<MediaFile> mediaFileList) {
        this.onVideoClickListener = onVideoClickListener;
        this.mMediaFileList = mediaFileList;
    }

    @Override
    public int getCount() {
        return mMediaFileList == null ? 0 : mMediaFileList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {
        final MediaFile mediaFile = mMediaFileList.get(position);
        long duration = mediaFile.getDuration();
        final String path = mediaFile.getPath();
        final View view;
        final ImageView imageView;
        if (duration > 0) {
            //视频
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_viewpager_video, null);
            ImageView playView = view.findViewById(R.id.iv_item_play);
            playView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onVideoClickListener != null) {
                        onVideoClickListener.onVideoClick(path);
                    }
                }
            });
        } else {
            //图片
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_viewpager_image, null);
        }
        imageView = view.findViewById(R.id.iv_item_image);
        try {
            ConfigManager.getInstance().getImageLoader().loadPreImage(imageView, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface OnVideoClickListener {
        void onVideoClick(String path);
    }
}
