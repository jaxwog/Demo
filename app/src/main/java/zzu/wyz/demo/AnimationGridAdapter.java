package zzu.wyz.demo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WYZ on 2016/1/26.
 */
public class AnimationGridAdapter extends BaseAdapter {

    private Context context = null;
    private List<Integer> picRes = new ArrayList<Integer>() ;

    public AnimationGridAdapter(Context context){
        this.context = context;
        this.initPic();

    }

    @Override
    public int getCount() {
        return this.picRes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.picRes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.picRes.get(position).intValue();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //定义ImageView，并返回；设置背景，设置图片；定义GridView的每一个图片
        ImageView img = new ImageView(this.context);
        img.setBackgroundColor(0xFF000000) ;
        img.setImageResource(this.picRes.get(position));
        img.setScaleType(ImageView.ScaleType.CENTER);
        img.setLayoutParams(new GridView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        img.setPadding(3, 3, 3, 3);
        return img;
    }

    //利用反射机制加载图片
    private void initPic(){
        //利用反射返回数组，从数组中找出有png_开始的图片
        Field[] fields = R.drawable.class.getDeclaredFields();
        for (int x = 0; x < fields.length; x++) {
            if(fields[x].getName().startsWith("png_")){
                try {
                    this.picRes.add(fields[x].getInt(R.drawable.class)) ;
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
