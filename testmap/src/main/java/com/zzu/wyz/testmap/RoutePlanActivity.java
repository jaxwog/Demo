package com.zzu.wyz.testmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cld.navisdk.routeguide.CldNaviConfig;
import com.cld.navisdk.routeinfo.CldRouteGuideManager;
import com.cld.navisdk.routeinfo.RouteLineInfo;
import com.cld.navisdk.util.view.CldProgress;
import com.epgis.mapapi.map.MapView;
import com.epgis.navisdk.EpNaviManager;
import com.epgis.navisdk.routeplan.EpRoutePlaner;
import com.epgis.navisdk.routeplan.RoutePlanNode;

/**
 * Created by user on 2016/6/30.
 */
public class RoutePlanActivity extends Activity implements View.OnClickListener {
    // 路径规划失败标识
    private final int MSG_ID_PLANROUTE_FAILED = 1001;
    // 路径规划成功标识
    private final int MSG_ID_PLANROUTE_SUCCESS = MSG_ID_PLANROUTE_FAILED + 1;
    private MapView mMapView = null;
    private FrameLayout mFl_MapView;// 添加地图控件的容器
    private Button mBtn_Online_Calc;// 在线规划按钮
    private Button mBtn_Simulate;// 模拟导航按钮
    private Button mBtn_Real;// 真实导航按钮
    private EditText mEt_StartX;// 起点经度
    private EditText mEt_StartY;// 起点纬度
    private EditText mEt_EndX;// 终点经度
    private EditText mEt_EndY;// 终点纬度
    private RouteLineInfo mInfo;// 导航过程管理类
    private Button mBtn_detail;// 详情按钮
    private Button mBtn_Hud;//HUD 省流量导航

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_routeplan);
        init();// 初始化

    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mMapView) {
            mMapView.onResume();// 当地图控件存在时，调用相应的恢复方法
            mMapView.update(true);// 同时更新地图控件状态
        }
    }

    // 初始化
    private void init() {
        // 创建地图控件对象
        mMapView = EpNaviManager.getInstance().createNMapView(this);
        // 找到相关控件
        mFl_MapView = (FrameLayout) findViewById(R.id.mapview_layout);
        mBtn_Online_Calc = (Button) findViewById(R.id.btn_online_calc);
        mBtn_Simulate = (Button) findViewById(R.id.btn_simulate);
        mBtn_Real = (Button) findViewById(R.id.btn_real);
        mBtn_Hud = (Button) findViewById(R.id.btn_hud);
        mBtn_detail = (Button) findViewById(R.id.btn_detail);
        mEt_StartX = (EditText) findViewById(R.id.et_start_x);
        mEt_StartY = (EditText) findViewById(R.id.et_start_y);
        mEt_EndX = (EditText) findViewById(R.id.et_end_x);
        mEt_EndY = (EditText) findViewById(R.id.et_end_y);

        // 把地图控件添加到界面上
        mFl_MapView.addView(mMapView);
        // 路线规划监听
        mBtn_Online_Calc.setOnClickListener(this);
        // 模拟导航监听
        mBtn_Simulate.setOnClickListener(this);
        // 真实导航监听
        mBtn_Real.setOnClickListener(this);
        // HUD导航监听
        mBtn_Hud.setOnClickListener(this);
        // 详情页面
        mBtn_detail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_online_calc:// 路线规划

                startCalcRoute();// 调用开始规划方法
                break;

            case R.id.btn_simulate:// 模拟导航

                startNavi(false);// 开启导航模式，false表示模拟导航
                break;

            case R.id.btn_real:// 真实导航

                startNavi(true);// 开启导航模式，true表示真实导航
                break;

            case R.id.btn_hud://hud导航
                if (!EpRoutePlaner.getInstance().hasPlannedRoute()) {// 判断是否有规划路径成功
                    Toast.makeText(this, "请先规划路径！", Toast.LENGTH_LONG).show();
                    return;
                }
                // 路径规划成功，则跳转到导航界面
                //startActivity(new Intent(RoutePlanActivity.this, NaviHudActivity.class));
                break;
            case R.id.btn_detail:
                if (!EpRoutePlaner.getInstance().hasPlannedRoute()) {// 判断是否有规划路径成功
                    Toast.makeText(RoutePlanActivity.this, "请先规划路径！", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                // 获得导航过程管理类
                mInfo = CldRouteGuideManager.getInstance().getDrivingRouteDetail();
                if (mInfo == null) {
                    return;
                }
                // 同时把数据传递过去
                Intent intent = new Intent();
                intent.setClass(RoutePlanActivity.this, DetailsDemo.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("info", mInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    // 消息处理
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ID_PLANROUTE_FAILED:// 失败
                    Toast.makeText(RoutePlanActivity.this,
                            msg.getData().getString("info"), Toast.LENGTH_LONG)
                            .show();
                    break;

                case MSG_ID_PLANROUTE_SUCCESS:// 成功
                    Toast.makeText(RoutePlanActivity.this, "路径规划成功！", Toast.LENGTH_LONG)
                            .show();
                    break;
            }
        };
    };

    /**
     * 开始规划路线
     *
     * @return void
     * @author Wangxy
     * @date 2016-3-21 上午11:42:29
     */
    private void startCalcRoute() {

        double sX = 0, sY = 0, eX = 0, eY = 0;
        try {
            // String转成double
            sX = Double.parseDouble(mEt_StartX.getText().toString());
            sY = Double.parseDouble(mEt_StartY.getText().toString());
            eX = Double.parseDouble(mEt_EndX.getText().toString());
            eY = Double.parseDouble(mEt_EndY.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 起点
        RoutePlanNode startNode = new RoutePlanNode(sY, sX, "创建大厦",
                "深圳市福田区深南大道6023号", RoutePlanNode.CoordinateType.CLD);
        // 终点
        RoutePlanNode endNode = new RoutePlanNode(eY, eX, "深圳市政府",
                "深圳市福田区福中三路", RoutePlanNode.CoordinateType.CLD);
        // 路径规划监听器
        EpRoutePlaner.RoutePlanListener listener = new EpRoutePlaner.RoutePlanListener() {

            @Override
            public void onRoutePlanSuccessed() {// 规划成功
                handler.sendEmptyMessage(MSG_ID_PLANROUTE_SUCCESS);
            }

            @Override
            public void onRoutePlanFaied(int err, String info) {// 规划失败
                Message message = Message.obtain();
                message.what = MSG_ID_PLANROUTE_FAILED;
                Bundle bundle = new Bundle();
                bundle.putString("info", info);
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onRoutePlanCanceled() {

            }
        };
        // 显示等待进度条
        CldProgress.showProgress(RoutePlanActivity.this, "正在规划路线...",
                new CldProgress.CldProgressListener() {

                    @Override
                    public void onCancel() {

                    }
                });

        EpRoutePlaner.getInstance().routePlan(RoutePlanActivity.this, startNode, // 起点
                null, // 经由地
                endNode, // 终点
                EpNaviManager.RoutePlanPreference.ROUTE_PLAN_MOD_RECOMMEND, // 算路方式
                listener);
    }

    /**
     * 开启导航方法
     *
     * @return void
     * @author Wangxy
     * @date 2016-3-21 上午11:43:12
     */
    private void startNavi(boolean isReal) {
        if (!EpRoutePlaner.getInstance().hasPlannedRoute()) {// 判断是否有规划路径成功
            Toast.makeText(this, "请先规划路径！", Toast.LENGTH_LONG).show();
            return;
        }
        // 路径规划成功，则跳转到导航界面
        Intent intent = new Intent(RoutePlanActivity.this, NavigatorActivity.class);
        intent.putExtra(CldNaviConfig.KEY_NAVIMODEL_RELNAVI, isReal);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        EpRoutePlaner.getInstance().clearRoute(); // 关闭界面时，清除规划路线
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mMapView) {
            mMapView.onPause();// 当地图控件存在时，调用相应的生命周期方法
        }

    }

}
