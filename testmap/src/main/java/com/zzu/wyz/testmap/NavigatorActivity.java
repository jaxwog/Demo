/*
 * @Title CldNavigatorActivity.java
 * @Copyright Copyright 2010-2015 Careland Software Co,.Ltd All Rights Reserved.
 * @author Huagx
 * @date 2015-11-10 下午7:20:16
 * @version 1.0
 */
package com.zzu.wyz.testmap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.cld.navisdk.routeguide.CldNaviConfig;
import com.cld.navisdk.routeguide.CldNavigator;
import com.epgis.mapapi.map.MapView;
import com.epgis.navisdk.EpNaviManager;

/**
 * 导航界面
 *
 */
public class NavigatorActivity extends Activity {
    View navigatorView;
    MapView nMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建mapView
        nMapView = EpNaviManager.getInstance().createNMapView(this);
        //创建导航视图
        boolean isRelNavi = getIntent().getExtras().getBoolean(CldNaviConfig.KEY_NAVIMODEL_RELNAVI);
        Bundle bundle = new Bundle();
        //传入导航类型参数
        bundle.putBoolean(CldNaviConfig.KEY_NAVIMODEL_RELNAVI, isRelNavi);
        //初始化导航控件
        navigatorView = CldNavigator.getInstance().init(this, bundle, nMapView);
        navigatorView.setFocusable(true);
        //填充视图
        setContentView(navigatorView);
        //开始导航
        CldNavigator.getInstance().startNavi();
        //让屏幕保持不暗不关闭
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * @see Activity#onBackPressed()
     */
    public void onBackPressed() {
        CldNavigator.getInstance().onBackPressed();//调用导航模式相应回退方法
    }

    /**
     * @see Activity#onStop()
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != nMapView) {
            nMapView.onPause();//当地图控件存在，调用地图控件暂停方法

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != nMapView) {
            nMapView.onResume();//当地图控件存在，调用地图控件恢复方法
            nMapView.update(true);//同时更新地图控件的状态
        }
        CldNavigator.getInstance().onResume();
    }

    /**
     * @see Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != nMapView) {
            nMapView.destroy();//当地图控件存在时，销毁地图控件
        }
    }
}
