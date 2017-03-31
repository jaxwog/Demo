package zzu.wyz.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by WYZ on 2016/1/15.
 */
public class MenuImageAdapter extends BaseAdapter{
    private ImageView []menuImg ;//保存所有要填充的组件
    private Context context;//构建上下文对象
    private int selectedMenuImg;//被选中的组件的position

    /**
     *
     * @param context     上下文菜单
     * @param imgIds      保存导航按钮图片的数组
     * @param width       导航按钮的宽和高
     * @param height
     * @param selectedMenuImg      被选中的导航按钮的id
     */

    public MenuImageAdapter(Context context,int imgIds[],int width,int height,int selectedMenuImg){
        this.context = context;
        this.selectedMenuImg = selectedMenuImg;

        this.setImgToMenu(imgIds,width,height);


    }

     //实例化并填充ImageView
    private void setImgToMenu(int imgIds[],int width,int height){
        menuImg = new ImageView[imgIds.length];//开辟新的数组
        for (int i = 0;i<imgIds.length;i++){
            menuImg[i] = new ImageView(this.context);
            menuImg[i].setLayoutParams(new GridView.LayoutParams(width,height));// 设置图片的大小
            menuImg[i].setAdjustViewBounds(false);// 不调整边界显示
            menuImg[i].setPadding(2,2,2,2);// 设置间距
            menuImg[i].setImageResource(imgIds[i]);//设置要显示的图片

        }


    }


    @Override
    public int getCount() {
        return this.menuImg.length;
    }


    @Override
    public Object getItem(int position) {
        return this.menuImg[position];
    }


    @Override
    public long getItemId(int position) {
        return this.menuImg[position].getId();
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        if (convertView==null){
            imageView = this.menuImg[position];
        }else{
            imageView = (ImageView)convertView;
        }

        return imageView;
    }

    /**
     *
     * @param selId 被选中的组件的id
     */
    public void setFocus(int selId){
        for (int i=0;i<this.menuImg.length;i++){
            if (i != selId){
                //遍历的方式改变图片背景，为0时候不设置背景图片
                this.menuImg[i].setBackgroundResource(0);

            }
        }
        //此时selId？？？？？selectedMenuImg
        this.menuImg[selId].setBackgroundResource(this.selectedMenuImg);

    }

}
