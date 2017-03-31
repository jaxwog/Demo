package zzu.wyz.demo;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import android.widget.ImageView;

public class AnimationActivity extends AppCompatActivity {
    private ImageView animationImage,animationface;
    private Button animationstart;
    AnimationSet animationSet;
    private ViewGroup animationLayout;
    //帧动画定义
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        this.init();

       // this.initAnimaClick();

    }

    //定义组件，并进行组件监听
    private void init(){
        this.animationImage = (ImageView)super.findViewById(R.id.animationImage);
        this.animationImage.setOnClickListener(new OnClickAnimation());

        this.animationstart = (Button)super.findViewById(R.id.animationstart);
        this.animationface = (ImageView)super.findViewById(R.id.animationface);
        this.animationstart.setOnClickListener(new OnClickButStart());

    }

    //帧动画的监听操作，用来显示一组图片依次加载
    private class OnClickButStart implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //取得背景图片xml资源
            AnimationActivity.this.animationface.setBackgroundResource(R.drawable.allface);
            //将图片资源传递给animationDrawable
            AnimationActivity.this.animationDrawable = (AnimationDrawable) AnimationActivity.this.animationface
                    .getBackground();
           // AnimationActivity.this.animationDrawable.setOneShot(false) ;//是否只是显示一次
            AnimationActivity.this.animationDrawable.start() ;

        }
    }

    //设置动画监听操作,LinearLayout是ViewGroup的子类
    private void initAnimaClick(){
        this.animationImage = (ImageView) super.findViewById(R.id.animationImage);
        this.animationLayout = (ViewGroup) super.findViewById(R.id.animationLayout);
        animationSet = new AnimationSet(true);
        //实现动画平移，在动画上设置监听操作
        TranslateAnimation tran = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0.0f ,	// X轴开始位置
                Animation.RELATIVE_TO_SELF,0.5f ,	// X轴移动的结束位置
                Animation.RELATIVE_TO_SELF,0.0f ,	// Y轴开始位置
                Animation.RELATIVE_TO_SELF,1.5f );	// Y轴移动位置
        tran.setDuration(6000); // 6秒完成动画
        animationSet.addAnimation(tran); // 增加动画
        animationSet.setAnimationListener(new AnimationListenerImpl()) ;
        this.animationImage.startAnimation(animationSet); // 启动动画
    }


    //动画监听操作，动画开始结束以及中间过程的监听
    private class AnimationListenerImpl implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {
            //动画开始，强制转换，并添加渐变动画
            if(animation instanceof AnimationSet) {
                AnimationSet set = (AnimationSet) animation ;
                AlphaAnimation alpha = new AlphaAnimation(1, 0);
                alpha.setDuration(6000) ;
                set.addAnimation(alpha) ;
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {

            //动画结束以后，从viewGridView中移除ImageView视图
            AnimationActivity.this.animationLayout.removeView(animationImage);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    //设置组件监听操作类，用于监听组件触发相应的操作
    private class OnClickAnimation implements View.OnClickListener{

        @Override
        public void onClick(View v) {
           // AnimationActivity.this.alphaAnima();//渐变
           // AnimationActivity.this.scaleAnima();//缩放
           // AnimationActivity.this.translateAnima();//平移
           // AnimationActivity.this.rotateAnima();//旋转
           // AnimationActivity.this.allAnima();//平移加缩放
            AnimationActivity.this.xmlAnima();


        }
    }

    //加载资源文件配置动画
    private void xmlAnima(){
        Animation animation;
        //用AnimationUtils类进行资源文件的加载
       //animation = AnimationUtils.loadAnimation(this,R.anim.alpha);
       // animation = AnimationUtils.loadAnimation(this,R.anim.scale);
       // animation = AnimationUtils.loadAnimation(this,R.anim.translate);
        //animation = AnimationUtils.loadAnimation(this,R.anim.rotate);
        animation = AnimationUtils.loadAnimation(this,R.anim.translate_scale);
        this.animationImage.startAnimation(animation);//启动动画
    }

    //渐变动画程序实现
        private void alphaAnima(){
        //继承自animation类，true表示进行动画叠加，并且是set集合
         this.animationSet = new AnimationSet(true);
        //设置从1到0（有到无）的动画渐变
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        //设置动画时间
        alphaAnimation.setDuration(3000);
        //渐变动画添加到AnimationSet集合里面
        animationSet.addAnimation(alphaAnimation);
        //在指定view上开始动画
        this.animationImage.startAnimation(animationSet);
    }

        //缩放动画的程序实现
        private void scaleAnima(){
             this.animationSet = new AnimationSet(true);
            //ScaleAnimation(float fromX, float toX, float fromY, float toY,
            // int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
            //x,y从1-0,以自身为中心，实现缩放
            ScaleAnimation scaleAnimation = new ScaleAnimation(1,0,1,0,
                    Animation.RELATIVE_TO_SELF,0.5f,//取0-1之间的数，0表示左边缘的位置
                    Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation.setDuration(3000);
            animationSet.addAnimation(scaleAnimation);
             this.animationImage.startAnimation(animationSet);
    }

        //平移动画的程序实现
        private void translateAnima(){
            this.animationSet = new AnimationSet(true);
            //TranslateAnimation(int fromXType, float fromXValue, int toXType, float toXValue,
            // int fromYType, float fromYValue, int toYType, float toYValue)
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,0.0f,//x轴开始位置，与组件自身相关
                    Animation.RELATIVE_TO_SELF,0.5f,//x轴结束位置
                    Animation.RELATIVE_TO_SELF,0.0f,
                    Animation.RELATIVE_TO_SELF,1.5f);
            translateAnimation.setDuration(3000);
            animationSet.addAnimation(translateAnimation);
            this.animationImage.startAnimation(animationSet);

        }

    //旋转动画的程序实现
    private void rotateAnima(){
        this.animationSet = new AnimationSet(true);
        //0-360度旋转，以父屏幕x=0，y = 0.5f为中心旋转
        RotateAnimation rotateAnimation = new RotateAnimation(0,360.0f,
                Animation.RELATIVE_TO_PARENT,0,
                Animation.RELATIVE_TO_PARENT,0.5f);
        rotateAnimation.setDuration(3000);
        animationSet.addAnimation(rotateAnimation);
        this.animationImage.startAnimation(rotateAnimation);
    }


    private void allAnima(){
        animationSet = new AnimationSet(true);
        TranslateAnimation tran = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0.0f ,	// X轴开始位置
                Animation.RELATIVE_TO_SELF,0.5f ,	// X轴移动的结束位置
                Animation.RELATIVE_TO_SELF,0.0f ,	// Y轴开始位置
                Animation.RELATIVE_TO_SELF,1.5f );	// Y轴移动位置
        ScaleAnimation scale = new ScaleAnimation(1, 0.0f, 1, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animationSet.addAnimation(tran); // 增加动画
        animationSet.addAnimation(scale); // 增加动画
        // Sets how many times the animation should be repeated.
        animationSet.setRepeatCount(3) ;

        //动画设置加速度曲线
        animationSet.setInterpolator(new AccelerateInterpolator());

        animationSet.setDuration(6000); // 6秒完成动画
        this.animationImage.startAnimation(animationSet);
    }

}
