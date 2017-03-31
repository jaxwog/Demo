package zzu.wyz.demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationViewActivity extends AppCompatActivity {
    private GridView animationGridView;
    private ListView animationListView;
    private String idData [] = new String[] {"zzu","wyz","hebi","android"} ;
    private String titleData [] = new String[] {"郑州大学","王 永 政","河南鹤壁","初级开发"} ;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载GridView，并显示出来
        //this.initGridView();

        this.init();

    }


    private void init(){
        setContentView(R.layout.activity_animationlistview);

        this.animationListView = (ListView) super.findViewById(R.id.animationListView) ;
        List<Map<String,Object>> all = new ArrayList<Map<String,Object>>() ;
        Map<String,Object> map = null ;
        for (int x = 0; x < this.idData.length; x++) {
            map = new HashMap<String,Object>() ;
            map.put("id", this.idData[x]) ;
            map.put("title", this.titleData[x]) ;
            all.add(map) ;
        }
        this.simpleAdapter = new SimpleAdapter(this, all, R.layout.anima_listtable, new String[] {
                "id", "title" }, new int[] { R.id.animationId, R.id.animationName });
        this.animationListView.setAdapter(this.simpleAdapter) ;

        this.animaJavaSet();
    }


    //通过程序配置ListView动画LayoutAnimationController
    private void animaJavaSet(){
        // android:layoutAnimation="@anim/layout_animation"取消组件配置属性

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_set) ;
        LayoutAnimationController control = new LayoutAnimationController(anim) ;
        control.setDelay(0.5f) ;
        control.setOrder(LayoutAnimationController.ORDER_RANDOM) ;
        this.animationListView.setLayoutAnimation(control) ;
    }


    private void initGridView(){
        setContentView(R.layout.activity_animationview);
        this.animationGridView = (GridView) super.findViewById(R.id.animationGridView) ;
        this.animationGridView.setAdapter(new AnimationGridAdapter(this)) ;
    }

}
