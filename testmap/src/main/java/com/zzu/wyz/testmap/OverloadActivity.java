package com.zzu.wyz.testmap;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cld.mapapi.model.LatLng;
import com.cld.nv.map.Overlay;
import com.epgis.mapapi.map.EpMap;
import com.epgis.mapapi.map.MapPoi;
import com.epgis.mapapi.map.MapView;
import com.epgis.mapapi.map.Marker;
import com.epgis.mapapi.map.MarkerOptions;

public class OverloadActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mMapView;//地图view
    protected EpMap cldMap;//地图的操作类
    protected LatLng center;//地图中心坐标
    private Marker marker;//标注View
    private Marker markerPop;//标注view的弹出窗口
    private SeekBar mSeekBar;//调整方向的进度条
    private int progress;//进度
    private int zoomLevel;//地图缩放比例
    private Button mBtn_Clear;//清除按钮
    private Button mBtn_Reset;//重置按钮

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overload);
        init();
        markerInit();
    };

    //初始化
    private void init(){
        //找到对应控件
        mMapView = (MapView) findViewById(R.id.mapView);
        mBtn_Clear = (Button) findViewById(R.id.btn_clear);
        mBtn_Reset = (Button) findViewById(R.id.btn_reset);
        mSeekBar = (SeekBar)this.findViewById(R.id.alphaBar);

        //设置点击事件
        mBtn_Clear.setOnClickListener(this);
        mBtn_Reset.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBarListener());
        cldMap = mMapView.getMap();
        //设置是否显示定位图标
        cldMap.setLocationIconEnabled(true);
        //初始化地图中心位置
        setCenter();
        //重置地图
        resetOverlay();
        //得到滑竿的进度值
        progress = mSeekBar.getProgress();
        //获得地图缩放比例值
        zoomLevel = cldMap.getZoomLevel();
    }

    /** 初始化标注
     * @return void
     * @author Wangxy
     * @date 2016-3-21 上午11:13:13
     */
    private void markerInit() {
        //标注的点击事件
        cldMap.setOnMarkerClickListener(new EpMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.v("SDK", "onMarkerClick");
                if (OverloadActivity.this.marker == marker) {
                    if (markerPop != null){
                        cldMap.removeOverlay(markerPop);
                    }
                    //获得标注详情控件
                    View view = LayoutInflater.from(OverloadActivity.this)
                            .inflate(R.layout.pop_poi, null);
                    TextView txtName = (TextView) view
                            .findViewById(R.id.txt_name);
                    //设置显示控件的名称内容
                    txtName.setText("创建大厦");
                    TextView txtAddress = (TextView) view
                            .findViewById(R.id.txt_address);
                    //设置显示控件的地址内容
                    txtAddress.setText("福田区车公庙深南大道南侧(招商银行大厦对面)");
                    markerPop = (Marker) cldMap.addOverlay(new MarkerOptions()
                            .position(center)//设置标注详情框坐标
                            .layout(view)//设置标注详情框的显示控件
                            .yOffset(-80));//相对于设置的坐标，y轴的偏移量
                    //标注的弹出窗口的点击事件
                    markerPop.setOnClickListener(new Overlay.IOverlayOnClickListener() {

                        @Override
                        public boolean onClick(Overlay arg0,int clickChildId) {
                            if(clickChildId == R.id.img_icon){
                                Toast.makeText(OverloadActivity.this, "您点击了图片", Toast.LENGTH_SHORT).show();
                            }else if(clickChildId == R.id.layout_content){
                                Toast.makeText(OverloadActivity.this, "您点击了内容", Toast.LENGTH_SHORT).show();
                            }else if(clickChildId == R.id.txt_address){
                                Toast.makeText(OverloadActivity.this, "您点击了地址", Toast.LENGTH_SHORT).show();
                            }else if(clickChildId == R.id.txt_name){
                                Toast.makeText(OverloadActivity.this, "您点击了名称", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(OverloadActivity.this, "您点击了弹出框", Toast.LENGTH_SHORT).show();
                            }

                            return true;
                        }
                    });
                    return true;
                }
                return false;
            }
        });
        //点击地图的其他地方,弹出窗口消失
        cldMap.setOnMapClickListener(new EpMap.OnMapClickListener() {

            @Override
            public boolean OnMapPoiClick(MapPoi poi) {
                return false;
            }

            @Override
            public void OnMapClick(LatLng point) {
                Log.v("SDK", "OnMapClick");
                cldMap.removeOverlay(markerPop);
            }
        });
    }


    //按钮的点击事件
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_clear){
            clearOverlay();//清除覆盖物
        }else if(v.getId() == R.id.btn_reset){
            setCenter();//设置中心点
            mSeekBar.setProgress(progress);
            resetOverlay();//重置覆盖物
            //设置初始比例
            cldMap.setZoomLevel(zoomLevel);
        }
    }

    /** 重置覆盖物
     * @return void
     * @author Wangxy
     * @date 2016-3-21 上午11:14:33
     */
    protected void resetOverlay() {
        if (marker != null){//存在时,先移除
            cldMap.removeOverlay(marker);
        }
        //添加一个新的标注
        marker = (Marker) cldMap.addOverlay(new MarkerOptions()
                .position(center).icon(
                        getResources().getDrawable(R.drawable.water))
                .rotate(0).alpha(0.8f));

        if(mSeekBar != null){
            mSeekBar.setProgress(0);
        }
    }

    /** 清除标注
     * @return void
     * @author Wangxy
     * @date 2016-3-21 上午11:16:09
     */
    protected void clearOverlay() {
        if (marker != null){
            cldMap.removeOverlay(marker);
        }
        if(markerPop != null){
            cldMap.removeOverlay(markerPop);
        }
        if(mSeekBar != null){
            mSeekBar.setProgress(0);
        }
    }

    /** 返回中心点坐标
     * @return LatLng
     * @author Wangxy
     * @date 2016-3-21 上午11:16:33
     */
    protected LatLng center() {
        return new LatLng(40.1273272, 116.410184);
    }


    /** 设置地图的中心点
     * @return void
     * @author Wangxy
     * @date 2016-3-21 上午11:16:42
     */
    private void setCenter(){
        if(center == null){
            center = center();
        }
        cldMap.setMapCenter(center);
    }

    //SeekBar的监听类
    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            //标注随着进度条的增加而进行旋转
            marker.setRotate(seekBar.getProgress() * 10);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
        setCenter();
        resetOverlay();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
        clearOverlay();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        mMapView.destroy();
    }

}
