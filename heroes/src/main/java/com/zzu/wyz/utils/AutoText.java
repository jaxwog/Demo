package com.zzu.wyz.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by wangyongzheng on 2017/4/24.
 */

public class AutoText extends android.support.v7.widget.AppCompatTextView{

    /** 字幕滚动的速度 快，普通，慢 */
    public static final int SCROLL_SLOW = 0;
    public static final int SCROLL_NORM = 1;
    public static final int SCROLL_FAST = 2;

    /** 字幕内容 */
    private String mText;

    /** 字幕字体颜色 */
    private int mTextColor;

    /** 字幕字体大小 */
    private float mTextSize;

    private float offX = 0f;

    private float mStep = 0.5f;

    private Rect mRect = new Rect();

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);;
    public AutoText(Context context) {
        super(context);
        setSingleLine();
    }

    public AutoText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
    }

    public AutoText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSingleLine();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mText = getText().toString();
        mTextColor = getCurrentTextColor();
        mTextSize = getTextSize();
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mText, 0, mText.length(), mRect);
    };

    @Override
    protected void onDraw(Canvas canvas) {
        float x, y;
        x = getMeasuredWidth() - offX;
        y = getMeasuredHeight() / 2 + (mPaint.descent() - mPaint.ascent()) / 2;
        canvas.drawText(mText, x, y, mPaint);
        offX += mStep;
        if (offX >= getMeasuredWidth() + mRect.width()) {
            offX = 0f;
        }
        invalidate();
    }

    /**
     * 设置字幕滚动的速度
     */
    public void setScrollMode(int scrollMod) {
        if (scrollMod == SCROLL_SLOW) {
            mStep = 0.5f;
        } else if (scrollMod == SCROLL_NORM) {
            mStep = 1f;
        } else {
            mStep = 1.5f;
        }
    }

}
