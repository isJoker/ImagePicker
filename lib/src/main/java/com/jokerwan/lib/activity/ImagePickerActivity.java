package com.jokerwan.lib.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jokerwan.lib.ImagePicker;
import com.jokerwan.lib.R;
import com.jokerwan.lib.adapter.ImagePickerAdapter;
import com.jokerwan.lib.bean.MediaFile;
import com.jokerwan.lib.loader.MediaLoadCallback;
import com.jokerwan.lib.manager.CommonExecutor;
import com.jokerwan.lib.manager.ConfigManager;
import com.jokerwan.lib.manager.SelectionManager;
import com.jokerwan.lib.task.ImageLoadTask;
import com.jokerwan.lib.task.MediaLoadTask;
import com.jokerwan.lib.task.VideoLoadTask;
import com.jokerwan.lib.utils.DataHelper;
import com.jokerwan.lib.utils.PermissionUtil;
import com.jokerwan.lib.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class ImagePickerActivity extends AppCompatActivity implements ImagePickerAdapter.OnItemClickListener {

    private boolean isShowImage;
    private boolean isShowVideo;
    private int maxCount;

    private TextView tvSend;
    private TextView tvBack;
    private TextView tvPreview;
    private RecyclerView recyclerView;

    private ImagePickerAdapter imagePickerAdapter;
    private List<MediaFile> mediaFileList;
    private static final int HORIZONTAL_COUNT = 4;
    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;
    private static final int REQUEST_PERMISSION_CAMERA_CODE = 0x03;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepicker);
        init();
    }

    private void init() {
        initConfig();
        checkPermission();
        initView();
        initRecycle();
        initClick();
    }

    //动态权限
    private void checkPermission() {
        boolean hasPermission = PermissionUtil.checkPermission(this);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CAMERA_CODE);
        } else {
            startScannerTask();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA_CODE) {
            if (grantResults.length >= 1) {
                int cameraResult = grantResults[0];//相机权限
                int sdResult = grantResults[1];//sd卡权限
                boolean cameraGranted = cameraResult == PackageManager.PERMISSION_GRANTED;//拍照权限
                boolean sdGranted = sdResult == PackageManager.PERMISSION_GRANTED;//拍照权限
                if (cameraGranted && sdGranted) {
                    //具有拍照权限，sd卡权限，开始扫描任务
                    startScannerTask();
                } else {
                    //没有权限
                    Toast.makeText(this, getString(R.string.permission_tip), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private void startScannerTask() {
        Runnable mediaLoadTask = null;
        //照片、视频全部加载
        if (isShowImage && isShowVideo) {
            mediaLoadTask = new MediaLoadTask(this, new MediaLoader());
        }
        //只加载视频
        if (!isShowImage && isShowVideo) {
            mediaLoadTask = new VideoLoadTask(this, new MediaLoader());
        }
        //只加载图片
        if (isShowImage && !isShowVideo) {
            mediaLoadTask = new ImageLoadTask(this, new MediaLoader());
        }
        //不符合以上场景，采用照片、视频全部加载
        if (mediaLoadTask == null) {
            mediaLoadTask = new MediaLoadTask(this, new MediaLoader());
        }
        CommonExecutor.getInstance().execute(mediaLoadTask);
    }

    protected void initConfig() {
        isShowImage = ConfigManager.getInstance().isShowImage();
        isShowVideo = ConfigManager.getInstance().isShowVideo();
        maxCount = ConfigManager.getInstance().getMaxCount();
        SelectionManager.getInstance().setMaxCount(maxCount);
        //如果是多选模式，载入历史记录
        if (ConfigManager.getInstance().getSelectionMode() == ConfigManager.SELECT_MODE_MULTI) {
            ArrayList<MediaFile> images = ConfigManager.getInstance().getImages();
            if (images != null && images.size() > 0) {
                SelectionManager.getInstance().addImagePathsToSelectList(images);
            }
        }
    }

    private void initView() {
        StatusBarUtil.setStatusBarColorWhite(this);
        StatusBarUtil.setStatusBarTransparent(this);

        tvBack = findViewById(R.id.tv_cancel);
        TextView mTvTitle = findViewById(R.id.tv_title);
        tvSend = findViewById(R.id.tv_send);
        tvPreview = findViewById(R.id.tv_preview);
        recyclerView = findViewById(R.id.rv_images);

        String title = ConfigManager.getInstance().getTitle();
        if(!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }
        String btnText = ConfigManager.getInstance().getBtnText();
        if(!TextUtils.isEmpty(btnText)) {
            tvSend.setText(btnText);
        }
    }

    private void initRecycle() {
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, HORIZONTAL_COUNT);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50);

        mediaFileList = new ArrayList<>();
        imagePickerAdapter = new ImagePickerAdapter(mediaFileList);
        recyclerView.setAdapter(imagePickerAdapter);
        imagePickerAdapter.setOnItemClickListener(this);
    }


    private void initClick() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectionManager.getInstance().removeAll();
                finish();
            }
        });
        //发送
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitSelectPic();
            }
        });

        //预览
        tvPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpPreView();
            }
        });
    }

    //预览选中图片列表
    private void jumpPreView() {
        ArrayList<MediaFile> selectList = new ArrayList<>(SelectionManager.getInstance().getSelects());
        if (selectList.size() > 0) {
            DataHelper.getInstance().setMediaData(selectList);
            Intent intent = new Intent(this, ImagePreActivity.class);
            //获取选中列表的第一个作为默认位置
            intent.putExtra(ImagePreActivity.IMAGE_POSITION, 0);
            startActivityForResult(intent, REQUEST_SELECT_IMAGES_CODE);
        }
    }

    private void commitSelectPic() {
        ArrayList<MediaFile> list = new ArrayList<>(SelectionManager.getInstance().getSelects());
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePicker.EXTRA_SELECT_IMAGES, list);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        SelectionManager.getInstance().removeAll();
        finish();
    }

    class MediaLoader implements MediaLoadCallback {

        @Override
        public void loadMediaSuccess(final List<MediaFile> files) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //默认加载全部照片
                    mediaFileList.addAll(files);
                    imagePickerAdapter.notifyDataSetChanged();

                    updateCommitButton();
                }
            });
        }
    }

    private void updateCommitButton() {
        int selectCount = SelectionManager.getInstance().getSelects().size();

        String btnText = ConfigManager.getInstance().getBtnText();
        if (selectCount == 0) {
            tvSend.setEnabled(false);
            if(TextUtils.isEmpty(btnText)) {
                tvSend.setText(getString(R.string.confirm));
            } else {
                tvSend.setText(btnText);
            }
        } else if (selectCount <= maxCount) {
            tvSend.setEnabled(true);
            if(TextUtils.isEmpty(btnText)) {
                tvSend.setText(String.format(getString(R.string.confirm_msg), selectCount));
            } else {
                tvSend.setText(String.format(getString(R.string.confirm_text_format), btnText, selectCount));
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_IMAGES_CODE) {
                commitSelection();
            }
        }
    }

    @Override
    public void onMediaCheck(View view, int position) {
        //执行选中/取消操作
        MediaFile mediaFile = imagePickerAdapter.getMediaFile(position);
        if (mediaFile != null) {
            boolean addSuccess = SelectionManager.getInstance().addImageToSelectList(mediaFile);
            if (addSuccess) {
                imagePickerAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, String.format(getString(R.string.select_image_max), maxCount), Toast.LENGTH_SHORT).show();
            }
        }

        if (ConfigManager.getInstance().getSelectionMode() == ConfigManager.SELECT_MODE_SINGLE) {
            commitSelection();
        } else {
            updateCommitButton();
        }
    }

    private void commitSelection() {
        ArrayList<MediaFile> list = new ArrayList<>(SelectionManager.getInstance().getSelects());
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePicker.EXTRA_SELECT_IMAGES, list);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        //清空选中记录
        SelectionManager.getInstance().removeAll();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (imagePickerAdapter != null) {
            imagePickerAdapter.notifyDataSetChanged();
            updateCommitButton();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            ConfigManager.getInstance().getImageLoader().clearMemoryCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
