package com.wyz.animation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zzu.wyz.R;

public class AnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
    }

    @Override
    public void finish() {
        super.finish();
        //控制当前页面的动画，与需要跳转的界面无关
        overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
    }
}
