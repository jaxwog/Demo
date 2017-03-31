package zzu.wyz.demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //选择器上的图片在页面中显示出来
        super.setTitle("查看图片") ;
        ImageView img = new ImageView(this) ;
        img.setImageResource(R.drawable.mldn_ad) ;
        super.setContentView(img) ;

    }

}
