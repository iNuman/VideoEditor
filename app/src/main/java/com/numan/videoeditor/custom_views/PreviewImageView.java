package com.numan.videoeditor.custom_views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.numan.videoeditor.R;

import static com.numan.videoeditor.utils.Constants.VIDEO_HEIGHT;
import static com.numan.videoeditor.utils.Constants.VIDEO_WIDTH;
import static com.zhpan.bannerview.utils.BannerUtils.log;

public class PreviewImageView extends androidx.appcompat.widget.AppCompatImageView {
    public static int mAspectRatioHeight = 360;
    public static int mAspectRatioWidth = 640;

    public PreviewImageView(Context context) {
        super(context);
    }

    public PreviewImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context, attrs);
    }

    public PreviewImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context, attrs);
    }

    public PreviewImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        Init(context, attrs);
    }

    @SuppressLint("ResourceType")
    private void Init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextAppearance);
        mAspectRatioWidth = a.getInt(0, VIDEO_WIDTH);
        mAspectRatioHeight = a.getInt(1, VIDEO_HEIGHT);
        log("mAspectRatioWidth", "mAspectRatioWidth:" + mAspectRatioWidth + " mAspectRatioHeight:" + mAspectRatioHeight);
        a.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalWidth;
        int finalHeight;
        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
        int calculatedHeight = (int) (((float) (mAspectRatioHeight * originalWidth)) / ((float) mAspectRatioWidth));
        if (calculatedHeight > originalHeight) {
            finalWidth = (int) (((float) (mAspectRatioWidth * originalHeight)) / ((float) mAspectRatioHeight));
            finalHeight = originalHeight;
        } else {
            finalWidth = originalWidth;
            finalHeight = calculatedHeight;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
    }
}
