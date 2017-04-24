/*
 * Copyright (C) 2015 David Lázaro Esparcia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zzu.wyz.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.view.View;


import com.zzu.wyz.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a graphic in style with Google Fit Application.
 * @attr ref android.R.styleable#itemsLineWidth the width of the wheel indicator lines
 * @attr ref android.R.styleable#backgroundColor color for the inner circle
 * @attr ref android.R.styleable#filledPercent percent of circle filled by WheelItems values
 * @author David Lázaro.
 */
public class WheelIndicatorView extends View {

    private final static int ANGLE_INIT_OFFSET = -90;
    private final static int DEFAULT_FILLED_PERCENT = 100;
    private final static int DEFAULT_ITEM_LINE_WIDTH = 25;
    public static final int ANIMATION_DURATION = 1200;
    public static final int INNER_BACKGROUND_CIRCLE_COLOR = Color.argb(255, 220, 220, 220); // Color for

    private Paint itemArcPaint;
    private Paint itemEndPointsPaint;
    private Paint innerBackgroundCirclePaint;
    private List<WheelIndicatorItem> wheelIndicatorItems;
    private int viewHeight;
    private int viewWidth;
    private int minDistViewSize;
    private int maxDistViewSize;
    private int traslationX;
    private int traslationY;
    private RectF wheelBoundsRectF;
    private Paint circleBackgroundPaint;
    private ArrayList<Float> wheelItemsAngles;     // calculated angle for each @WheelIndicatorItem
    private int filledPercent = 80;
    private int itemsLineWidth = 25;
    private Paint  mWaitTextPaint, mTextPaint;
    private float mShowTextSize = 50.0f;
    private String mShowText = "正在消缺验证";
    private int waitNumber = 0;

    private String mWaitText = "......";

    public WheelIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public WheelIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WheelIndicatorView(Context context) {
        super(context);
        init(null);
    }

    public void setWheelIndicatorItems(List<WheelIndicatorItem> wheelIndicatorItems) {
        if (wheelIndicatorItems == null)
            throw new IllegalArgumentException("wheelIndicatorItems cannot be null");
        this.wheelIndicatorItems = wheelIndicatorItems;
        recalculateItemsAngles();
        invalidate();
    }

    public void setFilledPercent(int filledPercent) {
        if (filledPercent <= 0){
            this.filledPercent = 0;
        } else if (filledPercent > 100) {
            this.filledPercent = 100;
        } else {
            waitNumber = filledPercent%6;
            this.mWaitText = this.setWaitNumber(waitNumber);
            this.filledPercent = filledPercent;
            if (filledPercent==100){
                mShowText = "消缺验证完成";
                this.mWaitText = "......";
            }else {
                mShowText ="正在消缺验证";
            }
        }
        invalidate();
    }

    public int getFilledPercent() {
        return filledPercent;
    }

    public void setItemsLineWidth(int itemLineWidth) {
        if (itemLineWidth <= 0)
            throw new IllegalArgumentException("itemLineWidth must be greater than 0");
        this.itemsLineWidth = itemLineWidth;
        invalidate();
    }

    public void addWheelIndicatorItem(WheelIndicatorItem indicatorItem) {
        if (indicatorItem == null)
            throw new IllegalArgumentException("wheelIndicatorItems cannot be null");

        this.wheelIndicatorItems.add(indicatorItem);
        recalculateItemsAngles();
        invalidate();
    }

    public void notifyDataSetChanged(){
        recalculateItemsAngles();
        invalidate();
    }

    public void setBackgroundColor(int color){
        circleBackgroundPaint = new Paint();
        circleBackgroundPaint.setColor(color);
        invalidate();
    }

    private void init(AttributeSet attrs) {
        TypedArray attributesArray = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.WheelIndicatorView, 0, 0);

        int itemsLineWidth = attributesArray.getDimensionPixelSize(R.styleable.WheelIndicatorView_itemsLineWidth, DEFAULT_ITEM_LINE_WIDTH );
        setItemsLineWidth(itemsLineWidth);

        int filledPercent = attributesArray.getInt(R.styleable.WheelIndicatorView_filledPercent,DEFAULT_FILLED_PERCENT);
        setFilledPercent(filledPercent);

        int bgColor = attributesArray.getColor(R.styleable.WheelIndicatorView_backgroundColor,-1);
        if (bgColor != -1)
            setBackgroundColor(bgColor);

        this.wheelIndicatorItems = new ArrayList<>();
        this.wheelItemsAngles = new ArrayList<>();

        itemArcPaint = new Paint();
        itemArcPaint.setStyle(Paint.Style.STROKE);
        itemArcPaint.setStrokeWidth(itemsLineWidth * 2);
        itemArcPaint.setAntiAlias(true);

        innerBackgroundCirclePaint = new Paint();
        innerBackgroundCirclePaint.setColor(INNER_BACKGROUND_CIRCLE_COLOR);
        innerBackgroundCirclePaint.setStyle(Paint.Style.STROKE);
        innerBackgroundCirclePaint.setStrokeWidth(itemsLineWidth * 2);
        innerBackgroundCirclePaint.setAntiAlias(true);

        itemEndPointsPaint = new Paint();
        itemEndPointsPaint.setAntiAlias(true);


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
    }

    private void recalculateItemsAngles() {
        wheelItemsAngles.clear();
        float total = 0;
        float angleAccumulated = 0;

        for (WheelIndicatorItem item : wheelIndicatorItems){
            total += item.getWeight();
        }
        for (int i=0; i < wheelIndicatorItems.size(); ++i) {
            float normalizedValue = wheelIndicatorItems.get(i).getWeight()/total;
            float angle = 360 * normalizedValue * filledPercent/100;
            wheelItemsAngles.add(angle + angleAccumulated);
            angleAccumulated += angle;
        }
    }

    public void startItemsAnimation() {
        ObjectAnimator animation = ObjectAnimator.ofInt(WheelIndicatorView.this,"filledPercent",0,filledPercent);
        animation.setDuration(ANIMATION_DURATION);
        animation.setInterpolator(PathInterpolatorCompat.create(0.4F, 0.0F, 0.2F, 1.0F));
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                recalculateItemsAngles();
                invalidate();
            }
        });
        animation.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.viewHeight = getMeasuredHeight();
        this.viewWidth = getMeasuredWidth();
        this.minDistViewSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
        this.maxDistViewSize = Math.max(getMeasuredWidth(), getMeasuredHeight());

        if (viewWidth <= viewHeight) {
            this.traslationX = 0;
            this.traslationY = (maxDistViewSize - minDistViewSize) / 2;
        } else {
            this.traslationX = (maxDistViewSize - minDistViewSize) / 2;
            this.traslationY = 0;
        }
        // Adding artificial padding, depending on line width
        wheelBoundsRectF = new RectF(0 + itemsLineWidth, 0 + itemsLineWidth, minDistViewSize - itemsLineWidth, minDistViewSize - itemsLineWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(traslationX, traslationY);
        if (circleBackgroundPaint != null)
            canvas.drawCircle(wheelBoundsRectF.centerX(), wheelBoundsRectF.centerY(), wheelBoundsRectF.width() / 2 - itemsLineWidth, circleBackgroundPaint);
        canvas.drawArc(wheelBoundsRectF, ANGLE_INIT_OFFSET, 360, false, innerBackgroundCirclePaint);
        drawIndicatorItems(canvas);
        float textWidth = mTextPaint.measureText(mShowText);   //测量字体宽度，根据字体的宽度设置在圆环中间


        canvas.drawText(mShowText, (int)(this.viewWidth/2-textWidth/2), (int)(this.viewWidth/2) , mTextPaint);
        canvas.drawText(mWaitText, (int)(this.viewWidth/2-textWidth/4), (int)(this.viewWidth/2+textWidth/2) , mWaitTextPaint);

    }

    private void drawIndicatorItems(Canvas canvas) {
        if (wheelIndicatorItems.size() > 0) {
            for (int i = wheelIndicatorItems.size() - 1; i >= 0; i--) { // Iterate backward to overlap larger items
                draw(wheelIndicatorItems.get(i), wheelBoundsRectF, wheelItemsAngles.get(i), canvas);
            }
        }
    }

    private void draw(WheelIndicatorItem indicatorItem, RectF surfaceRectF, float angle, Canvas canvas) {
        itemArcPaint.setColor(indicatorItem.getColor());
        itemEndPointsPaint.setColor(indicatorItem.getColor());
        // Draw arc
        canvas.drawArc(surfaceRectF, ANGLE_INIT_OFFSET, angle, false, itemArcPaint);
        // Draw top circle
        canvas.drawCircle(minDistViewSize / 2, 0 + itemsLineWidth, itemsLineWidth, itemEndPointsPaint);
        int topPosition = minDistViewSize / 2 - itemsLineWidth;
        // Draw end circle
        canvas.drawCircle(
                (float) (Math.cos(Math.toRadians(angle + ANGLE_INIT_OFFSET)) * topPosition + topPosition + itemsLineWidth),
                (float) (Math.sin(Math.toRadians((angle + ANGLE_INIT_OFFSET))) * topPosition + topPosition + itemsLineWidth), itemsLineWidth, itemEndPointsPaint);
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
