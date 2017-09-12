package com.wyz.animation;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wyz.touchevent.HorizontalScroollViewEx;
import com.wyz.touchevent.MyUitls;
import com.zzu.wyz.R;

import java.util.ArrayList;
import java.util.List;

public class LayoutAnimationActivity extends AppCompatActivity {
    private HorizontalScroollViewEx mListContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_animation);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = getLayoutInflater();
        mListContainer = (HorizontalScroollViewEx) findViewById(R.id.container);
        final int  screenWidth = MyUitls.getScreenMetrics(this).widthPixels;
        final int screenHeight = MyUitls.getScreenMetrics(this).heightPixels;

        for (int i = 0; i < 1; i++) {
            ViewGroup layout = (ViewGroup) layoutInflater.inflate(R.layout.content_layout,mListContainer,false);
            layout.getLayoutParams().width = screenWidth;
            TextView textView = (TextView) layout.findViewById(R.id.list_title);
            textView.setText("page"+(i + 1));
            layout.setBackgroundColor(Color.rgb((255/(i+1)),(255/(i+1)),0));
            createList(layout);
            mListContainer.addView(layout);
        }
    }

    private void createList(ViewGroup layout) {
        ListView listView = (ListView) layout.findViewById(R.id.list_list);
//代码中注册该选项，还可以在XML中定义
//        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_item);
//        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
//        layoutAnimationController.setDelay(0.5f);
//        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_RANDOM);
//        listView.setLayoutAnimation(layoutAnimationController);



        List<String> datas = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            datas.add("name"+i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.content_list_item,R.id.item_name,datas);
        listView.setAdapter(adapter);
    }
}
