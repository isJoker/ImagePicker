package com.jokerwan.lib.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.jokerwan.lib.R;
import com.jokerwan.lib.adapter.ImagePreThumbAdapter;
import com.jokerwan.lib.adapter.ImagePreViewAdapter;
import com.jokerwan.lib.bean.MediaFile;
import com.jokerwan.lib.manager.ConfigManager;
import com.jokerwan.lib.manager.ImagePickerProvider;
import com.jokerwan.lib.manager.SelectionManager;
import com.jokerwan.lib.utils.DataUtil;
import com.jokerwan.lib.utils.StatusBarUtil;
import com.jokerwan.lib.view.HackyViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 大图预览界面
 */
public class ImagePreActivity extends AppCompatActivity implements ImagePreViewAdapter.OnVideoClickListener {

    private TextView tvPicIndex;
    private TextView tvSend;
    private HackyViewPager mViewPager;
    private RecyclerView mRePreImage;

    public static final String IMAGE_POSITION = "imagePosition";
    private List<MediaFile> mMediaFileList;//全部媒体（图片和视频）
    private List<MediaFile> mSelectList;//选中列表
    private int mPosition = 0;
    private ImagePreThumbAdapter preViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_image);
        init();
    }

    private void init() {
        initView();
        initPreView();
        initThumbView();
        initData();
        initListener();
    }

    private void initView() {
        StatusBarUtil.setStatusBarColorWhite(this);
        StatusBarUtil.setStatusBarTransparent(this);

        mViewPager = findViewById(R.id.vp_main_preImage);
        mRePreImage = findViewById(R.id.re_preImage);
        tvPicIndex = findViewById(R.id.tv_pic_index);
        tvSend = findViewById(R.id.tv_send);
        String btnText = ConfigManager.getInstance().getBtnText();
        if(!TextUtils.isEmpty(btnText)) {
            tvSend.setText(btnText);
        }
    }

    private void initPreView() {
        mMediaFileList = DataUtil.getInstance().getMediaData();
        mPosition = getIntent().getIntExtra(IMAGE_POSITION, 0);

        ImagePreViewAdapter mImagePreViewAdapter = new ImagePreViewAdapter(this,mMediaFileList);
        mViewPager.setAdapter(mImagePreViewAdapter);
        mViewPager.setCurrentItem(mPosition);
        updateIndex(mPosition);
    }

    private void updateIndex(int index) {
        tvPicIndex.setText(String.format(getString(R.string.format_number), index + 1));
    }

    private void initThumbView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRePreImage.setLayoutManager(layoutManager);

        mSelectList = new ArrayList<>();
        mSelectList.clear();
        preViewAdapter = new ImagePreThumbAdapter(mSelectList);
        mRePreImage.setAdapter(preViewAdapter);
        mRePreImage.smoothScrollToPosition(mPosition);
    }

    private void initData() {
        updateCommitButton();
        updateRecycleCheck();
    }

    private void updateRecycleCheck() {
        //Recycle默认选中的
        List<MediaFile> mediaFiles = SelectionManager.getInstance().getSelects();
        for (MediaFile mf : mediaFiles) {
            initPreData(mf);
        }
        MediaFile mediaFile=mMediaFileList.get(mPosition);
        if (mediaFile!=null&&preViewAdapter!=null){
            preViewAdapter.setSelect(mediaFile);
        }
    }

    private void initPreData(MediaFile mediaFile) {
        boolean isSelect = SelectionManager.getInstance().isImageSelect(mediaFile);
        if (isSelect) {
            mSelectList.add(mediaFile);
        } else {
            mSelectList.remove(mediaFile);
        }
        preViewAdapter.notifyDataSetChanged();
    }


    private void initListener() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ViewPager滚动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateIndex(position);
                MediaFile mediaFile=mMediaFileList.get(position);
                if (mediaFile!=null&&preViewAdapter!=null){
                    preViewAdapter.setSelect(mediaFile);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
        });

        //点击Recycle滚动到指定位置的大图
        preViewAdapter.setOnItemClickListener(new ImagePreThumbAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, MediaFile mediaFile) {
                preViewAdapter.setSelect(mediaFile);
                int position = SelectionManager.getInstance().getSelectImagePosition(mMediaFileList, mediaFile);
                mViewPager.setCurrentItem(position);
            }
        });
    }

    private void updateCommitButton() {
        String btnText = ConfigManager.getInstance().getBtnText();
        if(TextUtils.isEmpty(btnText)) {
            tvSend.setText(String.format(getString(R.string.confirm_msg), SelectionManager.getInstance().getSelects().size()));
        } else {
            tvSend.setText(String.format(getString(R.string.confirm_text_format), btnText, SelectionManager.getInstance().getSelects().size()));
        }
    }

    @Override
    public void onVideoClick(String path) {
        //实现播放视频的跳转逻辑(调用原生视频播放器)
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, ImagePickerProvider.getFileProviderName(this), new File(path));
        intent.setDataAndType(uri, "video/*");
        //给所有符合跳转条件的应用授权
        List<ResolveInfo> resInfoList = this.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        this.startActivity(intent);
    }
}
