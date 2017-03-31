package zzu.wyz.demo;

import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SystemRunActivity extends AppCompatActivity {

    private Button wifiopen,wificlose,wificheck;
    private TextView systemmsg;

    private ListView tasklist = null ;
    private ListAdapter adapter = null ;
    private List<String> all = new ArrayList<String>() ;

    private TelephonyManager telephonyManager;

    private WifiManager wifiManager;

    private ActivityManager activityManager = null ;
   // private List<ActivityManager.RunningTaskInfo> allTaskInfo ;//正在运行的Activity
   // private List<ActivityManager.RunningServiceInfo> allTaskInfo ;//正在运行的Service
    private List<ActivityManager.RunningAppProcessInfo> allTaskInfo ;//正在运行的所有进程
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);

        this.tasklist = (ListView) super.findViewById(R.id.runlistview) ;
        //取得系统运行服务信息
      // this.systemActivityManager();

        this.systemTelephonyManager();
        this.systemWifi();
    }

    private void systemWifi(){
        wifiopen = (Button)super.findViewById(R.id.wifiopen);
        wificlose = (Button)super.findViewById(R.id.wificlose);
        wificheck = (Button)super.findViewById(R.id.wificheck);
        systemmsg = (TextView)super.findViewById(R.id.systemmsg);

        this.wifiManager = (WifiManager)super.getSystemService(Context.WIFI_SERVICE);

        wifiopen.setOnClickListener(new OnClickWifi());
        wificlose.setOnClickListener(new OnClickWifi());
        wificheck.setOnClickListener(new OnClickWifi());
    }

    private class OnClickWifi implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.wifiopen:
                    SystemRunActivity.this.wifiManager.setWifiEnabled(true) ;	// 启用wifi
                    SystemRunActivity.this.systemmsg.setText("打开WIFI，状态：" +
                            SystemRunActivity.this.wifiManager.getWifiState()) ;
                    break;
                case R.id.wificlose:
                    SystemRunActivity.this.wifiManager.setWifiEnabled(false) ;	// 启用wifi
                    SystemRunActivity.this.systemmsg.setText("关闭WIFI，状态：" +
                            SystemRunActivity.this.wifiManager.getWifiState()) ;
                    break;
                case R.id.wificheck:
                    SystemRunActivity.this.systemmsg.setText("检查WIFI，状态：" +
                            SystemRunActivity.this.wifiManager.getWifiState()) ;
                    break;
                default:
                    break;
            }
        }
    }


    //取得手机信息的对象
    private void systemTelephonyManager(){
        this.telephonyManager = (TelephonyManager)super.getSystemService(Context.TELEPHONY_SERVICE);

        this.listPhone();

    }

    //取得系统运行服务的对象
    private void systemActivityManager(){
        this.activityManager = (ActivityManager) super
                // .getSystemService(Context.ACTIVITY_SERVICE);
                .getSystemService(Context.ACTIVITY_SERVICE);
        this.listActivity() ;
    }

    //取得手机信息
    private void listPhone(){
        this.all.add(this.telephonyManager.getLine1Number()==null?
                "没有手机号码":"手机号码是："+this.telephonyManager.getLine1Number());
        this.all.add(this.telephonyManager.getNetworkOperatorName()==null?
                "没有网络运营商":"网络运营商："+this.telephonyManager.getNetworkOperatorName());

        if (this.telephonyManager.getPhoneType() == TelephonyManager.NETWORK_TYPE_CDMA) {
            this.all.add("移动网络：CDMA");
        } else if (this.telephonyManager.getPhoneType() == TelephonyManager.NETWORK_TYPE_GPRS) {
            this.all.add("移动网络：GPRS");
        }else if (this.telephonyManager.getPhoneType() == TelephonyManager.NETWORK_TYPE_LTE){
            this.all.add("移动网络：LTE");
        } else {
            this.all.add("移动网络：未知");
        }

        if (this.telephonyManager.getNetworkType() == TelephonyManager.PHONE_TYPE_GSM) {
            this.all.add("网络类型：GSM");
        } else if (this.telephonyManager.getNetworkType() == TelephonyManager.PHONE_TYPE_CDMA) {
            this.all.add("网络类型：CDMA");
        } else {
            this.all.add("网络类型：未知");
        }

        this.all.add("是否漫游：" + (this.telephonyManager.isNetworkRoaming() ? "漫游" : "非漫游"));

        this.adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, this.all);

        this.tasklist.setAdapter(this.adapter) ;
    }

    private void listActivity() {

        this.allTaskInfo = this.activityManager.getRunningAppProcesses() ;
        Iterator<ActivityManager.RunningAppProcessInfo> iterInfo = this.allTaskInfo.iterator() ;
        while(iterInfo.hasNext()) {
            ActivityManager.RunningAppProcessInfo app = iterInfo.next() ;
            this.all.add("【ID = " + app.pid + " 】 "
                    + app.processName);
        }

       /* this.allTaskInfo = this.activityManager.getRunningServices(30) ;
        Iterator<ActivityManager.RunningServiceInfo> iterInfo = this.allTaskInfo.iterator() ;
        while(iterInfo.hasNext()) {
            ActivityManager.RunningServiceInfo service = iterInfo.next() ;
            this.all.add("【ID = " + service.pid + " 】 "
                    + service.process);
        }*/

       /*
         this.allTaskInfo = this.activityManager.getRunningTasks(30); // 返回30条
       Iterator<ActivityManager.RunningTaskInfo> iterInfo = this.allTaskInfo.iterator() ;
        while(iterInfo.hasNext()) {
            ActivityManager.RunningTaskInfo task = iterInfo.next() ;
            this.all.add("【ID = " + task.id + " 】 "
                    + task.baseActivity.getClassName());
        }*/
        this.adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, this.all);
        this.tasklist.setAdapter(this.adapter) ;
    }

}
