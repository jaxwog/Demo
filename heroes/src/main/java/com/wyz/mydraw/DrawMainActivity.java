package com.wyz.mydraw;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzu.wyz.R;

public class DrawMainActivity extends AppCompatActivity implements View.OnClickListener{
private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    TransitionDrawable transitionDrawable;
    ScaleDrawable scaleDrawable;
    ImageView testClip;
    ClipDrawable testClipDrawable;
    View view;
    CustomDrawable customDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_main);


           initView();
    }

    private void initView() {

        //根据等级切换图片
       textView1 = (TextView) findViewById(R.id.test_level);
       textView1.setOnClickListener(this);

       //淡入淡出动画
        textView2 = (TextView) findViewById(R.id.test_transition);
        transitionDrawable= (TransitionDrawable) textView2.getBackground();
        textView2.setOnClickListener(this);

        //scale缩放比例
        textView3 = (TextView) findViewById(R.id.test_scale);
        textView3.setOnClickListener(this);
        scaleDrawable = (ScaleDrawable) textView3.getBackground();
        scaleDrawable.setLevel(10000);

        //clip裁剪图片
        testClip = (ImageView) findViewById(R.id.test_clip);
        testClip.setOnClickListener(this);
        testClipDrawable = (ClipDrawable) testClip.getDrawable();
        testClipDrawable.setLevel(8000);

        //自定义Drawable
        view = findViewById(R.id.test_custom_drawable);
        customDrawable = new CustomDrawable(Color.parseColor("#0ac39e"));
        view.setBackgroundDrawable(customDrawable);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_level:
                textView1.getBackground().setLevel(1);
                break;
            case R.id.test_transition:
                transitionDrawable.startTransition(2000);
                break;
            case R.id.test_scale:
                scaleDrawable.setLevel(1);
                break;
            case R.id.test_clip:
                testClipDrawable.setLevel(1);
                break;
            default:
                break;
        }


    }
}
