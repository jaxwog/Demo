package com.zzu.wyz.testmap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.cld.navisdk.routeguide.CldNaviConfig;
import com.cld.navisdk.util.view.CldProgress;
import com.epgis.navisdk.EpNaviManager;
import com.epgis.navisdk.routeplan.EpRoutePlaner;
import com.epgis.navisdk.routeplan.NavigatorConfig;
import com.epgis.navisdk.routeplan.RoutePlanNode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final int ROUTE_PLANED_FIALED = 1000;// 路径导航失败标识
    Button testmap;
    Button testGPS;
    Button testrouteplan;
    Button testoverload;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ROUTE_PLANED_FIALED:
                    Toast.makeText(MainActivity.this,
                            msg.getData().getString("info"), Toast.LENGTH_LONG)
                            .show();
                    break;

                default:
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();



// 初始化sdk
        EpNaviManager.getInstance().init(this, mInitListener);

    }

    private void init() {
        testmap = (Button) super.findViewById(R.id.testmap);
        testGPS = (Button) super.findViewById(R.id.testGPS);
        testrouteplan = (Button) super.findViewById(R.id.testrouteplan);
        testoverload = (Button) super.findViewById(R.id.testoverload);
        testmap.setOnClickListener(this);
        testGPS.setOnClickListener(this);
        testrouteplan.setOnClickListener(this);
        testoverload.setOnClickListener(this);
    }


    // 监听key是否校验成功,并给出提示.
    private EpNaviManager.NaviInitListener mInitListener = new EpNaviManager.NaviInitListener() {

        @Override
        public void initFailed(String msg) {//初始化失败方法
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onAuthResult(int status, String msg) {//初始化结果
            String str;
            if (0 == status) {//初始化结果状态判断
                str = "key校验成功!";
            } else {
                str = "key校验失败!";
            }
            //初始化结果返回的信息提示
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void initStart() {//初始化开始方法

        }

        @Override
        public void initSuccess() {//初始化成功方法

        }
    };


    @Override
    //点击返回键的事件处理
    public void onBackPressed() {
        //退出程序
        System.exit(0);
        //杀死进程
        android.os.Process.killProcess(android.os.Process.myUid());
        //sdk反初始化
        EpNaviManager.getInstance().unInit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //基础地图
            case R.id.testmap:
                startActivity(
                        new Intent(MainActivity.this,BaseMapActivity.class));
                break;
            //gps导航
            case R.id.testGPS:
                likeGPS();
                break;
            //路径规划
            case R.id.testrouteplan:
                startActivity(
                        new Intent(MainActivity.this, RoutePlanActivity.class));
                break;
            //当前位置导航
            case R.id.testoverload:
                startActivity(
                        new Intent(MainActivity.this,OverloadActivity.class));
                break;
            default:
                break;
        }
    }

    private void likeGPS() {
        // 初始化起点对象
        RoutePlanNode started = new RoutePlanNode(39.9770477680,
                116.3065147885, "长远天地大厦", "", RoutePlanNode.CoordinateType.CLD);

        // 初始化目的地对象
        RoutePlanNode destination = new RoutePlanNode(30.3197176383,
                120.1513929915, "浙江杭州电力职业技术学院", "", RoutePlanNode.CoordinateType.GCJ02);

        NavigatorConfig config = new NavigatorConfig();
        config.startPoint = started;// 起点
        config.endPoint = destination;// 终点
        config.preference = EpNaviManager.RoutePlanPreference.ROUTE_PLAN_MOD_MIN_TIME;// 算路方式
        config.isGPSNavi = false;// true gps导航 false 模拟导航
        config.planListener = new EpNaviManager.RoutePlanListener() {

            @Override
            public void onResultPlanFailed(int err, String info) {
                Message message = Message.obtain();
                message.what = ROUTE_PLANED_FIALED;
                Bundle bundle = new Bundle();
                bundle.putString("info", info);
                message.setData(bundle);
                handler.sendMessage(message);// 路径规划失败，发提示信息
            }

            @Override
            public void onJumpToNavigator() {
                // 规划成功跳转诱导导航界面
                Intent intent = new Intent(MainActivity.this,
                        NavigatorActivity.class);
                intent.putExtra(CldNaviConfig.KEY_NAVIMODEL_RELNAVI,
                        false);
                startActivity(intent);
            }
        };
        // 显示等待进度条
        CldProgress.showProgress(MainActivity.this, "正在规划路线...",
                new CldProgress.CldProgressListener() {

                    @Override
                    public void onCancel() {

                    }
                });
        // 传入配置参数
        EpNaviManager.getInstance().launchNavigator(
                MainActivity.this, config);
    }


}
