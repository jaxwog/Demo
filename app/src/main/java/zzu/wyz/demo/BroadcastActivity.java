package zzu.wyz.demo;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import zzu.wyz.demo.util.BroadcastReceiverUtil;

public class BroadcastActivity extends AppCompatActivity {
    private Button bcrsendbut;

    private BroadcastReceiverUtil broadUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        this.init();

    }

    private void init(){
        this.bcrsendbut = (Button)super.findViewById(R.id.bcrsendbut);

        this.bcrsendbut.setOnClickListener(new OnClickBroadcast());
    }

    private class OnClickBroadcast implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //基本实现，通过程序配置，通过BroadcastReceiver调用Service
            //BroadcastActivity.this.startBroadcastReceiver();
        }
    }

    private void startBroadcastReceiver(){

        //发送广播，再通过广播启动Service
        String s = "com.zzu.wyz.AXON";
        Intent it = new Intent(s);
        this.sendBroadcast(it);

     /*   //配置程序定义广播
         String s = "com.zzu.wyz.LOVE";
        Intent intent = new Intent(s);//此处必须与IntentFilter相同
        intent.putExtra("msg", "我是王永政哦！");
      //  Structured description of Intent values to be matched. An IntentFilter can match against actions,
      // categories, and data (either via its type, scheme, and / or path)
        //in an Intent. It also includes a "priority" value which is used to order multiple matching filters.
        IntentFilter intentFilter  = new IntentFilter(s);//结构化的匹配intent的value
        this.broadUtil = new BroadcastReceiverUtil();
        this.registerReceiver(broadUtil,intentFilter);

        this.sendBroadcast(intent);
*/

/*      //添加广播过滤条件,基本形式
        Intent it = new Intent(Intent.ACTION_EDIT);
        this.sendBroadcast(it);//发送广播
        */

    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
