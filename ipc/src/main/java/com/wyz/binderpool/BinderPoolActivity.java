package com.wyz.binderpool;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wyz.ipc.R;

public class BinderPoolActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "BinderPoolActivity";
    private Button button1;
    private IPassWord passWord;
    private ICompute compute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);

        button1 = (Button) super.findViewById(R.id.btn_pool);
        button1.setOnClickListener(this);



    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pool:
               initData();
                break;
            default:
                break;
        }
    }

    private void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }


    private void doWork() {

        BinderPool binderPool = BinderPool.getInstance(BinderPoolActivity.this);
        IBinder passWordBinder = binderPool.queryBinder(BinderPool.BINDER_PASSWORD);
        passWord = (IPassWord) PassWordImpl.asInterface(passWordBinder);
        Log.d(TAG, "开始加密密码");
        String msg = "HelloWord-安卓";
        Log.d(TAG, "doWork: msg="+msg);
        try {
            String passString = passWord.encrypt(msg);
            Log.d(TAG, "doWork: 加密后密码："+passString);
            Log.d(TAG, "doWork: 解密后密码："+passWord.decrypt(passString));
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        Log.d(TAG, "doWork: 开始进行数学计算");

        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_CONPUTE);
        compute = (ICompute) ComputeImpl.asInterface(computeBinder);

        try {
            int c = compute.add(3,5);
            Log.d(TAG, "doWork: 3+5 = "+c);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


}
