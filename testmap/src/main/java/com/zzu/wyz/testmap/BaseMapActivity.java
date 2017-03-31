package com.zzu.wyz.testmap;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.epgis.mapapi.map.MapView;

public class BaseMapActivity extends AppCompatActivity {

    private MapView mMapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView = new MapView(this);
        setContentView(mMapView);
    }
    @Override
    protected void onPause() {
        super.onPause();
// activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
// activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
// activity 销毁时同时销毁地图控件
        mMapView.destroy();
    }

}
