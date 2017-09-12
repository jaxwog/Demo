package zzu.wyz.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class HandlerMessageActivity extends AppCompatActivity {

    private boolean flag = true;//while循环判断
    private TextView info = null ;
    private Button but;
    private static int count = 0 ;	// 表示更新后的记录
    private static final int SET1 = 1 ;	// 操作的what标记,定时更新文本
    private static final int SET2 = 2 ;//使用Looper传递消息
    private static final int SET3 = 2 ;//不使用Looper
    private static final int SETMAIN = 13 ;//主线程接收子线程消息的标记
    private static final int SETCHILD= 14 ;//子线程接收主线程消息的标记
    private static final int SET4 = 4 ;//定时更新时间


    private Handler mainHandler,childHandler;

    private Handler clockHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case SET4:
                   HandlerMessageActivity.this.info.setText("当前时间为："+msg.obj.toString());
           }
        }
    };



    //定时更新文本根据返回的msg.what判断返回的标记
    private Handler timeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {	// 判断操作的消息类型
                case SET1:	// 更新组件
                    HandlerMessageActivity.this.info.setText("ZZU - " + count ++) ;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        this.mainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case SETMAIN :
                       HandlerMessageActivity.this.info.setText("主线程接收数据：" + msg.obj.toString()) ;
                        break ;
                }
            }
        };

        //启动主线程向子线程发送消息，并得到返回消息
       // new Thread(new ChildThread(),"Child  Thread").start();

        this.init();

        //this.timeToNext();

    }

    private void init(){
        this.info = (TextView) super.findViewById(R.id.handlerinfo) ;	// 取得组件
        this.but = (Button) super.findViewById(R.id.handlerbut) ;	// 取得组件

        this.but.setOnClickListener(new OnClickHandler());

    }

    private class OnClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //使用Looper消息队列
           // HandlerMessageActivity.this.myLooper();
            //不使用Looper
           // HandlerMessageActivity.this.myNoLooper();
            //主线程向子线程发送消息
           // HandlerMessageActivity.this.mainToChildMsg();
            new Thread(new ClockThread()).start();
        }
    }

    //定时更新时间
    private class ClockThread implements Runnable{

        @Override
        public void run() {

            while (flag) {

                Message message = clockHandler.obtainMessage();
                message.what = SET4;
                message.obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                clockHandler.sendMessage(message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //主线程向子线程发送消息
    private void mainToChildMsg(){
        if (this.childHandler!=null){
            Message childMsg = this.childHandler.obtainMessage();//创建消息
            //取得主线程的名字并发送出去
            childMsg.obj = this.mainHandler.getLooper().getThread().getName()
                    + " --> Hello 王永政 .";
            childMsg.what = SETCHILD;
            this.childHandler.sendMessage(childMsg);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.childHandler.getLooper().quit();
    }

    //子线程接收主线程发过来的消息，并打印在输出台上；并且传递消息到主线程
    private class ChildThread implements Runnable{
        @Override
        public void run() {
            //初始化当前线程作为消息通道
            Looper.prepare();
            HandlerMessageActivity.this.childHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                   switch (msg.what){
                       case SETCHILD:
                           System.out.println("*** Main To Child Message : " + msg.obj) ;
                           Message toMain = mainHandler.obtainMessage();
                           toMain.what = SETMAIN;
                           toMain.obj = "\"\\n\\n[B]这是子线程发送给主线程的信息：\" "
                                   + super.getLooper().getThread().getName() ;
                           mainHandler.sendMessage(toMain);
                           break;
                   }
                }
            };
            //运行这个线程的消息队列
            Looper.loop();

        }
    }


    //向HandlerNoLooper类发送数据
    private void myNoLooper(){
        HandlerNoLooper  myHandler = new HandlerNoLooper();
        myHandler.removeMessages(0);
        String data = "郑州大学工学院（ZZU）";
        Message msg = myHandler.obtainMessage(SET3,data);
        myHandler.sendMessage(msg);

    }

    //不使用Looper消息队列
    private class HandlerNoLooper extends Handler{
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case SET3:
                   HandlerMessageActivity.this.info.setText(msg.obj.toString()); // 设置文本组件
           }
        }
    }


    //使用Looper消息队列，向HandlerLooper类发送数据
    private void myLooper(){
        Looper looper = Looper.myLooper();
        HandlerLooper myHandler = new HandlerLooper(looper);
        myHandler.removeMessages(0);//清空消息
        String data = "郑州大学工学院";
        Message msg = myHandler.obtainMessage(SET2,data);// 创建消息
        myHandler.sendMessage(msg);

    }

    //使用Looper消息队列显示文本更新
    private class HandlerLooper extends Handler {
        public HandlerLooper(Looper looper){
            super(looper);

        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) { // 判断操作的消息类型
                case SET2: // 更新组件
                    HandlerMessageActivity.this.info.setText(msg.obj.toString()); // 设置文本组件
            }
        }


    }


    //定时更新组件显示，并在主界面显示
    private void timeToNext(){
        Timer timer = new Timer() ;
        timer.schedule(new MyTask(), 0, 1000) ;
    }

  //开启新的线程，用来更新时间
    private class MyTask extends TimerTask{

        @Override
        public void run() {
            Message msg = new Message() ;	// 设置更新
            msg.what = SET1 ;	// 操作的标记
            HandlerMessageActivity.this.timeHandler.sendMessage(msg) ;	// 发送消息
        }

    }

}
