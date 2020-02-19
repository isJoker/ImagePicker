package com.jokerwan.lib.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class FitView extends View {

    public FitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FitView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int statusBarHeight = getStatusBarHeight();
        setMeasuredDimension(widthMeasureSpec, statusBarHeight);
    }

    /**
     * 得到状态栏的高度
     *
     * @return 高度（int类型）
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = Resources.getSystem().getDimensionPixelOffset(resId);
        }
        return result;
    }
}

