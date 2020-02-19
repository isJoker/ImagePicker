package com.jokerwan.imagepicker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jokerwan.lib.ImagePicker;
import com.jokerwan.lib.bean.MediaFile;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private int REQUEST_SELECT_IMAGES_CODE = 0x01;
    private CameraDialog dialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mTextView = findViewById(R.id.tv_select_images);
        findViewById(R.id.bt_select_images).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new CameraDialog(MainActivity.this);
                dialog.show();
                dialog.setOnClickListener(new CameraDialog.OnItemClickListener() {
                    @Override
                    public void onClick(View v, CameraDialog.ClickType type) {
                        if (type==CameraDialog.ClickType.ALBUM){
                            initPicker();
                        }
                    }
                });

            }
        });
    }

    private void initPicker() {
        ImagePicker.getInstance()
                // 设置标题
                .setTitle("所有照片")
                // 设置确定按钮上文本
                .setConfirmBtnText("确定")
                // 设置是否展示图片
                .showImage(true)
                // 设置是否展示视频
                .showVideo(true)
                // 设置最大选择图片数目(默认为1，单选)
                .setMaxCount(9)
                .setImageLoader(new GlideLoader())
                .start(this, REQUEST_SELECT_IMAGES_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            List<MediaFile> mediaFiles= (List<MediaFile>) data.getSerializableExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("当前选中图片路径：\n\n");
            for (int i = 0; i < mediaFiles.size(); i++) {
                stringBuffer.append(mediaFiles.get(i).getPath() + "\n\n");
            }
            mTextView.setText(stringBuffer.toString());
        }
    }
}

