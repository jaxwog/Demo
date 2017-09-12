package com.wyz.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.zzu.wyz.R;

public class AnimationMainActivity extends AppCompatActivity {
    private static final String TAG = "AnimationMainActivity";
    Button movButton;
    Button layoutButton;
    Button activityButton;
    Button aminsetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_main);

        //initViewAnimation();

        //initView3dAnimation();

        //initViewAnimationDrawable();
        initLayoutAnimation();
        initActivityAnimation();

        initAnimator();



    }

    private void initAnimator() {
        movButton = (Button) super.findViewById(R.id.button_moving);
        //改变对象的属性
        movButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "initAnimator: movButton.getHeight()==="+movButton.getHeight());
               // ObjectAnimator.ofFloat(movButton,"translationY",-movButton.getHeight()).start();


//                ValueAnimator colorAnim = ObjectAnimator.ofInt(movButton,"backgroundColor",Color.RED,Color.BLACK,Color.BLUE);
//                colorAnim.setDuration(5000);
//                colorAnim.setEvaluator(new ArgbEvaluator());
//                colorAnim.setRepeatCount(ValueAnimator.INFINITE);
//                colorAnim.setRepeatMode(ValueAnimator.REVERSE);
//                colorAnim.start();

                AnimatorSet set = new AnimatorSet();
                set.playSequentially(
                        ObjectAnimator.ofFloat(movButton,"rotationX",0,360),
                        ObjectAnimator.ofFloat(movButton,"rotationY",0,180),
                        ObjectAnimator.ofFloat(movButton,"rotation",0,-90),
                        ObjectAnimator.ofFloat(movButton,"translationX",0,90),
                        ObjectAnimator.ofFloat(movButton,"translationY",0,90),
                        ObjectAnimator.ofFloat(movButton,"scaleX",1,1.5f),
                        ObjectAnimator.ofFloat(movButton,"scaleY",1,0.5f),
                        ObjectAnimator.ofFloat(movButton,"alpha",1,0.25f,1)
                );
                set.setDuration(5*1000).start();

                //Button不支持设置宽度改变，需要定义一个类用来间接实现get和set方法
//                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(AnimationMainActivity.this,R.animator.property_animator);
//                set.setTarget(movButton);
//                set.start();

            }
        });


    }

    private void initActivityAnimation() {
        activityButton = (Button) super.findViewById(R.id.button_activity);
        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimationMainActivity.this, AnimationActivity.class);
                startActivity(intent);
                //控制当前页面的动画，与需要跳转的界面无关
                overridePendingTransition(R.anim.enter_anim,R.anim.exit_anim);
            }
        });
    }

    private void initLayoutAnimation() {

        layoutButton= (Button) super.findViewById(R.id.button_layout);
        layoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimationMainActivity.this, LayoutAnimationActivity.class);
                startActivity(intent);
            }
        });

        aminsetButton= (Button) super.findViewById(R.id.button_getandset);
        aminsetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimationMainActivity.this, GetAndSetActivity.class);
                startActivity(intent);
            }
        });

    }


    //帧动画作为背景
    private void initViewAnimationDrawable() {
        movButton = (Button) super.findViewById(R.id.button_moving);
        movButton.setBackgroundResource(R.drawable.animatation_draw);
        AnimationDrawable animationDrawable = (AnimationDrawable) movButton.getBackground();
        animationDrawable.start();


    }

    //自定义动画
    private void initView3dAnimation() {
        movButton = (Button) super.findViewById(R.id.button_moving);
        Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(0,100,100,100,30,true);
        rotate3dAnimation.setDuration(5000);
        movButton.startAnimation(rotate3dAnimation);
    }

    //View动画
    private void initViewAnimation() {
        movButton = (Button) super.findViewById(R.id.button_moving);

        //XML文件定义，实现动画效果
//        Animation animation = AnimationUtils.loadAnimation(this,R.anim.translate_rotate);
//        movButton.startAnimation(animation);

        //代码实现动画效果
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(5000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG, "onAnimationStart: 监听到动画开始，执行相应的操作");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(TAG, "onAnimationEnd: 监听到动画结束后执行相应的操作");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i(TAG, "onAnimationRepeat: %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

            }
        });
        movButton.startAnimation(alphaAnimation);

    }
}
