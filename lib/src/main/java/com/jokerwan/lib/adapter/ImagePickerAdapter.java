package com.jokerwan.lib.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jokerwan.lib.R;
import com.jokerwan.lib.bean.ItemType;
import com.jokerwan.lib.bean.MediaFile;
import com.jokerwan.lib.manager.ConfigManager;
import com.jokerwan.lib.manager.SelectionManager;
import com.jokerwan.lib.utils.Utils;
import com.jokerwan.lib.view.SquareImageView;
import com.jokerwan.lib.view.SquareRelativeLayout;

import java.util.List;

/**
 * 列表适配器
 */
public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.BaseHolder> {

    private List<MediaFile> mMediaFileList;
    private int mSelectionMode;
    private OnItemClickListener mOnItemClickListener;

    public ImagePickerAdapter(List<MediaFile> mediaFiles) {
        this.mMediaFileList = mediaFiles;
        this.mSelectionMode = ConfigManager.getInstance().getSelectionMode();
    }


    @Override
    public int getItemViewType(int position) {
        if (mMediaFileList.get(position).getDuration() > 0) {
            return ItemType.ITEM_TYPE_VIDEO;
        } else {
            return ItemType.ITEM_TYPE_IMAGE;
        }
    }

    @Override
    public int getItemCount() {
        if (mMediaFileList == null) {
            return 0;
        }
        return mMediaFileList.size();
    }

    public MediaFile getMediaFile(int position) {
        return mMediaFileList.get(position);
    }


    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == ItemType.ITEM_TYPE_IMAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picker_image, null);
            return new ImageHolder(view);
        }
        if (viewType == ItemType.ITEM_TYPE_VIDEO) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picker_video, null);
            return new VideoHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        int itemType = getItemViewType(position);
        MediaFile mediaFile = getMediaFile(position);
        switch (itemType) {
            //图片、视频Item
            case ItemType.ITEM_TYPE_IMAGE:
            case ItemType.ITEM_TYPE_VIDEO:
                MediaHolder mediaHolder = (MediaHolder) holder;
                bindMedia(mediaHolder, mediaFile);
                break;
            //相机Item
            default:
                break;
        }
        //设置点击事件监听
        if (mOnItemClickListener != null) {
            if (holder instanceof MediaHolder) {
                ((MediaHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onMediaCheck(view, position);
                    }
                });
            }
        }
    }


    //绑定数据（图片、视频）
    private void bindMedia(MediaHolder mediaHolder, MediaFile mediaFile) {
        String imagePath = mediaFile.getPath();
        //如果是单选模式，隐藏多选框
        if (mSelectionMode == ConfigManager.SELECT_MODE_SINGLE) {
            mediaHolder.mTvCheck.setVisibility(View.GONE);
        } else {
            //选择状态（仅是UI表现，真正数据交给SelectionManager管理）
            if (SelectionManager.getInstance().isImageSelect(mediaFile)) {
                mediaHolder.mImageView.setColorFilter(Color.parseColor("#77000000"));
                mediaHolder.mTvCheck.setBackground(mediaHolder.mTvCheck.getResources().getDrawable(R.drawable.shape_circle_select));
                mediaHolder.mTvCheck.setText(String.format(mediaHolder.mTvCheck.getContext().getString(R.string.format_number),
                        SelectionManager.getInstance().getSelectImagePosition(mediaFile) + 1));
            } else {
                mediaHolder.mImageView.setColorFilter(null);
                mediaHolder.mTvCheck.setBackground(mediaHolder.mTvCheck.getResources().getDrawable(R.drawable.shape_circle_unselect));
                mediaHolder.mTvCheck.setText("");
            }

        }

        try {
            ConfigManager.getInstance().getImageLoader().loadImage(mediaHolder.mImageView, imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //如果是gif图，显示gif标识
        if (mediaHolder instanceof ImageHolder) {
            String suffix = imagePath.substring(imagePath.lastIndexOf(".") + 1);
            if (suffix.toUpperCase().equals("GIF")) {
                ((ImageHolder) mediaHolder).mImageGif.setVisibility(View.VISIBLE);
            } else {
                ((ImageHolder) mediaHolder).mImageGif.setVisibility(View.GONE);
            }
        }
        //如果是视频，需要显示视频时长
        if (mediaHolder instanceof VideoHolder) {
            String duration = Utils.getVideoDuration(mediaFile.getDuration());
            ((VideoHolder) mediaHolder).mVideoDuration.setText(duration);
        }
    }


    class ImageHolder extends MediaHolder {
        public ImageView mImageGif;

        public ImageHolder(View itemView) {
            super(itemView);
            mImageGif = itemView.findViewById(R.id.iv_item_gif);
        }
    }


    class VideoHolder extends MediaHolder {
        private TextView mVideoDuration;

        public VideoHolder(View itemView) {
            super(itemView);
            mVideoDuration = itemView.findViewById(R.id.tv_item_videoDuration);
        }
    }


    class MediaHolder extends BaseHolder {

        public SquareImageView mImageView;
        public TextView mTvCheck;

        public MediaHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_item_image);
            mTvCheck = itemView.findViewById(R.id.tv_item_check);
        }
    }


    class BaseHolder extends RecyclerView.ViewHolder {

        public SquareRelativeLayout mSquareRelativeLayout;

        public BaseHolder(View itemView) {
            super(itemView);
            mSquareRelativeLayout = itemView.findViewById(R.id.srl_item);
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onMediaCheck(View view, int position);
    }
}