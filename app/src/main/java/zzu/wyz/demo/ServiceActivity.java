package zzu.wyz.demo;

import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import zzu.wyz.demo.util.IService;
import zzu.wyz.demo.util.MyserviceUtil;


public class ServiceActivity extends AppCompatActivity {
    private Button servicestart,servicestop,servicebind,serviceunbind;

    private IService service;

/*
    private ServiceConnection serviceConnection = new ServiceConnection() {
        *//**
         *Interface for monitoring the state of an application service.
         * See Service and Context.bindService() for more information.
         * Like many callbacks from the system,
         * the methods on this class are called from the main thread of your process.
         * @param name  已经连接的服务的具体组件的名称
         * @param service  Ibinder服务的通信通道
         *//*
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                System.out.println("### Service Connect Success . service = "
                        + service.getInterfaceDescriptor());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("### Service Connect Failure.");
        }
    };*/

        private ServiceConnection serviceConnection = new ServiceConnectionImpl();


    private class ServiceConnectionImpl implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
    // BinderImpl是IBinder接口的子类，所以将IBinder向下转型为BinderImpl类
            ServiceActivity.this.service = (MyserviceUtil.BinderImpl)service;
            try {
                System.out.println("### Service Connect Success . service = "
                        + service.getInterfaceDescriptor());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("### Service Connect Failure.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        this.init();

        this.clipboar();

    }

    //剪切板系统服务
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void clipboar(){
        ClipboardManager clipboardManager = (ClipboardManager)super
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText("郑州大学王永政");
    }

    private void init(){
        this.servicestart = (Button)super.findViewById(R.id.servicestart);
        this.servicestop = (Button)super.findViewById(R.id.servicestop);
        this.servicebind = (Button)super.findViewById(R.id.servicebind);
        this.serviceunbind = (Button)super.findViewById(R.id.serviceunbind);

        this.servicestart.setOnClickListener(new StartOnClick());
        this.servicestop.setOnClickListener(new StopOnClick());
        this.servicebind.setOnClickListener(new BindtOnClick());
        this.serviceunbind.setOnClickListener(new UnBindtOnClick());

    }

    //打开Service
    private class StartOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            ServiceActivity.this.startService(
                    new Intent(ServiceActivity.this, MyserviceUtil.class));

        }
    }

    //停止Service
    private class StopOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            ServiceActivity.this.stopService(
                    new Intent(ServiceActivity.this, MyserviceUtil.class));
        }
    }
    //绑定Service
    private class BindtOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {

           // bindService(Intent service, ServiceConnection conn,int flags)
            ServiceActivity.this.bindService(
                    new Intent(ServiceActivity.this,MyserviceUtil.class),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE);

        }
    }

    //解除绑定
    private class UnBindtOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (ServiceActivity.this.service!=null) {
                ServiceActivity.this.unbindService(serviceConnection);
                ServiceActivity.this.service = null;
            }
        }
    }

}
