package zzu.wyz.demo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import zzu.wyz.demo.R;

/**
 * Created by WYZ on 2016/1/25.
 */
public class PaintMyViewUtil extends View{
    private  Paint paint;
    private   Bitmap bitmap;
    private Matrix matrix;
    /**
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see #View(Context, AttributeSet, int)
     */
    public PaintMyViewUtil(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.matrixValue();
    }

    //重写方法，相当于定义画布
    @Override
    protected void onDraw(Canvas canvas) {
        //绘制图形圆，矩形，弧形
       // PaintMyViewUtil.this.drawPaint(canvas);

        //加载图片
       // PaintMyViewUtil.this.bitmapTest3(canvas);

        canvas.drawBitmap(bitmap,matrix,paint);

    }

    //Matrix定义图片的旋转
    private void matrixValue(){

        this.matrix = new Matrix();//定义矩阵
        this.bitmap = BitmapFactory.decodeResource(super.getResources(),
                R.drawable.android_mldn);
        //当为负数时候，是逆时针旋转角度
        float  cosValue = (float) Math.cos(-Math.PI / 3);
        float sinValue = (float) Math.sin(-Math.PI / 3);

        //100,200为旋转点，2为缩放比率,采用矩阵公式
       // this.matrix.setValues(new float[] { cosValue, -sinValue, 100, sinValue, cosValue, 200, 0, 0, 2 });
        this.matrix.preScale(0.5f, 0.5f, 50, 100);//定义缩放比率
        this.matrix.preRotate(-60, 50, 100);//定义旋转角度
        this.matrix.preTranslate(50, 100) ;//定义转换
    }

    //简单加载图片,得到图片的高以及宽度
    private void bitmapTest1(Canvas canvas){
        //找到图片资源对象
        bitmap = BitmapFactory.decodeResource(super.getResources(), R.drawable.android_mldn);
        paint = new Paint();

        paint.setAntiAlias(true);//消除锯齿效果
        canvas.drawBitmap(bitmap, 0, 0, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        //得到图片的高度与宽度bitmap.getHeight()、bitmap.getWidth()
        canvas.drawText("图片高度：" + bitmap.getHeight() + "，图片宽度：" + bitmap.getWidth(),
                10, bitmap.getHeight() + 30, paint);
    }

    //填充整个屏幕
    private void bitmapTest2(Canvas canvas){

        //取得屏幕对象，得到屏幕的宽和高
        DisplayMetrics displayMetrics = super.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        //找到图片资源，并设置图片的大小 true if the source should be filtered.
        bitmap = BitmapFactory.decodeResource(super.getResources(), R.drawable.android_mldn);
        bitmap = Bitmap.createScaledBitmap(bitmap,screenWidth,screenHeight,true);

        paint = new Paint();

        paint.setAntiAlias(true);//消除锯齿效果
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    //指定位置显示图片，图片全部显示，只是进行了缩放,或者剪切图片
    private void bitmapTest3(Canvas canvas){
        bitmap = BitmapFactory.decodeResource(super.getResources(),
                R.drawable.android_mldn);	// 找到图片的Bitmap对象
       paint = new Paint() ;
        paint.setAntiAlias(true); // 消除锯齿
        //canvas.drawBitmap(bitmap, null, new Rect(30, 50, 200, 200), paint);
        canvas.drawBitmap(bitmap, new Rect(200,200,300,300), new Rect(100, 100, 200, 200), paint);
    }



    private void drawPaint(Canvas canvas){
        canvas.drawColor(Color.WHITE);//白色背景
        paint = new Paint();

        //画实心圆
        paint.setColor(Color.BLUE);
        canvas.drawCircle(30, 50, 25, paint);//定义圆点坐标，并制定半径为25

        //画填充矩形
        paint.setColor(Color.BLACK) ;
        canvas.drawRect(80, 20, 160, 80, paint) ;//左上角右下角点的坐标

        //画空心矩形
        Rect rect = new Rect();
        rect.set(180, 20, 300, 80);//左上角右下角点的坐标
        paint.setStyle(Paint.Style.STROKE);//定义风格，风格为空心
        canvas.drawRect(rect, paint) ;

        //定义文字
        paint.setColor(Color.RED) ;
        paint.setTextSize(20) ;
        canvas.drawText("郑州大学工学院王永政", 10, 110, paint) ;//起始点x，y坐标

        //画一条线
        paint.setColor(Color.BLACK) ;
        canvas.drawLine(10, 120, 300, 120, paint);//起始点与终点的坐标


        //画一个椭圆，定义为float类型数据，
        RectF oval = new RectF() ;
        oval.set(10.0f, 140.0f, 110.0f, 200.0f) ;//左上右下
        canvas.drawOval(oval, paint) ;


        //画一条弧线
        oval = new RectF() ;
        oval.set(150.0f, 140.0f, 210.0f, 200.0f);//定义大小
        canvas.drawArc(oval, 150.0f, 140.0f, true, paint) ;
    }
}
