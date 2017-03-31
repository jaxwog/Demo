package zzu.wyz.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IntentActivityB extends Activity {
    private TextView intentbmsg;
    private Button intentbbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("*** [B] SecondActivity --> onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intentb);
        intentbbut = (Button)super.findViewById(R.id.intentbbut);
        intentbmsg =(TextView) super.findViewById(R.id.intentbmsg);
        this.intentFromA();

        intentbbut.setOnClickListener(new OnclickIntent());
    }

    private class OnclickIntent implements View.OnClickListener{

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
       IntentActivityB.this.intentBackA();
        }
    }

    private void intentFromA(){
        Intent _intent = super.getIntent();
       String s = _intent.getStringExtra("key");
       // System.out.println("**********************=="+s);
        this.intentbmsg.setText(s);

    }

    private void intentBackA(){
        this.getIntent().putExtra("keyback","返回到A界面的信息");
        this.setResult(RESULT_OK, this.getIntent());
        this.finish();
    }

    @Override
    protected void onDestroy() {
        System.out.println("*** [B] SecondActivity --> onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        System.out.println("*** [B] SecondActivity --> onPause()");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        System.out.println("*** [B] SecondActivity --> onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        System.out.println("*** [B] SecondActivity --> onResume()");
        super.onResume();
    }

    @Override
    protected void onStart() {
        System.out.println("*** [B] SecondActivity --> onStart()");
        super.onStart();
    }

    @Override
    protected void onStop() {
        System.out.println("*** [B] SecondActivity --> onStop()");
        super.onStop();
    }

}
