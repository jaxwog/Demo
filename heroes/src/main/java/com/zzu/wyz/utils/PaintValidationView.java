package com.zzu.wyz.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**********************************************
 * 版权信息：北京振中电子技术有限公司版权©所有
 * 创建作者：
 * 创建日期：2017/4/24
 * ********************************************
 * 更新历史：作者         日期        更新摘要
 *
 * ********************************************
 * 类功能：自定义消缺验证界面弧形view
*/

public class PaintValidationView extends View {

    private static final String TAG = "PaintValidationView";

    private Paint mArcPaint, mWaitTextPaint, mTextPaint;

    private float length;

    private float mRadius;

    private float mCircleXY;

    private float mSweepValue = 0;

    private int waitNumber = 0;

    private String mShowText = "0%";

    private String mWaitText = "......";

    private float mShowTextSize = 50.0f;

    private RectF mRectF;

    public PaintValidationView(Context context) {
        super(context);
        initView();
    }



    public PaintValidationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PaintValidationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
         initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PaintValidationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        mArcPaint = new Paint();
        mArcPaint.setStrokeWidth(50);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(Color.GREEN);
        mArcPaint.setStyle(Paint.Style.STROKE);


        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(mShowTextSize);
        mTextPaint.setStrokeWidth(0);


        mWaitTextPaint = new Paint();
        mWaitTextPaint.setAntiAlias(true);
        mWaitTextPaint.setColor(Color.BLACK);
        mWaitTextPaint.setTextSize(2*mShowTextSize);
        mWaitTextPaint.setStrokeWidth(0);
//
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        length = w;
        mCircleXY = length / 2;
        mRadius = (float) (length * 0.5 / 2);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mRectF = new RectF((float) (length * 0.1), (float) (length * 0.1),
                (float) (length * 0.9), (float) (length * 0.9));


        canvas.drawArc(mRectF, 270, mSweepValue, false, mArcPaint);

        // 绘制文字
        float textWidth = mTextPaint.measureText(mShowText);   //测量字体宽度，根据字体的宽度设置在圆环中间


        canvas.drawText(mShowText, (int)(length/2-textWidth/2), (int)(length/2) , mTextPaint);
        canvas.drawText(mWaitText, (int)(length/2-textWidth/4), (int)(length/2+textWidth/2) , mWaitTextPaint);

    }

    public void setProgress(float mSweepValue) {
        float a = (float) mSweepValue;
        if (a != 0) {
            this.mSweepValue = (float) (360.0 * (a / 100.0));
            waitNumber = (int) (a%6);
            this.mWaitText = this.setWaitNumber(waitNumber);
            if (a==100f){
                mShowText = "消缺验证完成";
                mArcPaint.setColor(Color.GREEN);
                this.mWaitText = "......";
            }else {
                mArcPaint.setColor(Color.BLUE);
                mShowText ="正在消缺验证";
            }

        } else {
            this.mSweepValue = 360;
            mShowText = "消缺验证失败";
            mArcPaint.setColor(Color.RED);
            this.mWaitText = "......";
        }

        invalidate();
    }


    private String setWaitNumber(int number){
        if (1==number){
           return ".";
        }else if (2==number){
            return "..";
        }else if (3==number){
            return "...";
        }else if (4==number){
            return "....";
        }else if (5==number){
            return ".....";
        }else if (0==number){
            return ".......";
        }else {
            return "......";
        }

    }



}
