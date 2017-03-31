package zzu.wyz.demo;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AsyncTaskActivity extends AppCompatActivity {

    private ProgressBar asyncbar;
    private TextView asyncinfo;
    private Button asyncbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynctask);

        this.init();

    }

    //注册事件，监听事件
    private void init(){
        this.asyncbar = (ProgressBar)super.findViewById(R.id.asyncbar);
        this.asyncinfo = (TextView)super.findViewById(R.id.asyncinfo);
        this.asyncbut = (Button)super.findViewById(R.id.asyncbut);

        this.asyncbut.setOnClickListener(new OnClickAsync());

    }


    private class OnClickAsync implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //创建对象，调用执行
            AsyncTaskShow doit = new AsyncTaskShow();
            doit.execute(200);
        }
    }

    //params传递的参数，values当前执行的进度，s执行完毕返回的结果
    private class AsyncTaskShow extends AsyncTask<Integer,Integer,String>{
        /**
         * Runs on the UI thread after {@link #publishProgress} is invoked.
         * The specified values are the values passed to {@link #publishProgress}.
         *
         * @param values The values indicating progress.
         * @see #publishProgress
         * @see #doInBackground
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            //设置当前执行的进度值
            AsyncTaskActivity.this.asyncinfo.setText("当前进度:"+ String.valueOf(values[0])+"/100");
            AsyncTaskActivity.this.asyncbar.setProgress(values[0]);
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param s The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(String s) {
            //执行完毕以后，调用该方法，并显示执行结果
            AsyncTaskActivity.this.asyncinfo.setText(s);
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(Integer... params) {

            //执行具体方法，在后台运行
            for (int i=0;i<100;i++){
                //调用更新操作
                this.publishProgress(i);
                try {
                    //休眠时间由外部决定
                    Thread.sleep(params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            return "执行完毕";
        }
    }

}
