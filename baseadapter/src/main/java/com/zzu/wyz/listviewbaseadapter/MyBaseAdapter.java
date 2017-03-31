package com.zzu.wyz.listviewbaseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by WYZ on 2016/1/27.
 */
public class MyBaseAdapter extends BaseAdapter {

    private LayoutInflater mlayoutInflater;

    private List<ItemBean> mDataList;

    private long mSumTime;

    public MyBaseAdapter(Context context,List<ItemBean> list){


        this.mlayoutInflater = LayoutInflater.from(context);

        this.mDataList = list;

    }


    @Override
    public int getCount() {
        return this.mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
/*
        //逗逼式》》》》》》》》》》》》》》》》》》》》》》》》》》》》34851726
        long start = System.nanoTime();

        //将xml资源文件转换为view视图,并不涉及到具体的布局，所以第二个参数通常设置为null
        View view =mlayoutInflater.inflate(R.layout.item,null);
        ImageView itemImage = (ImageView) view.findViewById(R.id.iv_image);
        TextView itemTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView itemContent = (TextView) view.findViewById(R.id.tv_cotent);

        ItemBean bean = mDataList.get(position);
        itemImage.setImageResource(bean.itemImageResid);
        itemContent.setText(bean.itemContent);
        itemTitle.setText(bean.itemTitle);

        long end = System.nanoTime();
        long dValue = end-start;
        mSumTime +=dValue;
        System.out.println("**********"+mSumTime);

        return view;

        //逗逼式》》》》》》》》》》》》》》》》》》》》》》》》》》》》

        */

/*
        //普通式》》》》》》》》》》》》》》》》》》》》》》》》》》》》27217254

        long start = System.nanoTime();
//判断缓存视图convertView是否为空，避免重复加载视图
        if (convertView == null){
            convertView =mlayoutInflater.inflate(R.layout.item,null);
        }

        ImageView itemImage = (ImageView) convertView.findViewById(R.id.iv_image);
        TextView itemTitle = (TextView) convertView.findViewById(R.id.tv_title);
        TextView itemContent = (TextView) convertView.findViewById(R.id.tv_cotent);

        ItemBean bean = mDataList.get(position);
        itemImage.setImageResource(bean.itemImageResid);
        itemContent.setText(bean.itemContent);
        itemTitle.setText(bean.itemTitle);

        long end = System.nanoTime();
        long dValue = end-start;
        mSumTime +=dValue;
        System.out.println("**********"+mSumTime);

        return convertView;

        //普通式》》》》》》》》》》》》》》》》》》》》》》》》》》》》
        */

        //文艺式》》》》》》》》》》》》》》》》》》》》》》》》》》》》22667449
        long start = System.nanoTime();
        ViewHolder viewHolder;


        if (convertView == null){
            //如果视图不存在，则加载视图，定义一个ViewHolder类，用来避免重复读取组件id
            viewHolder = new ViewHolder();
            convertView =mlayoutInflater.inflate(R.layout.item,null);

            viewHolder.img = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tv_cotent);

            //把查找的view缓存起来方便多次重用
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        ItemBean bean = mDataList.get(position);
        viewHolder.img.setImageResource(bean.itemImageResid);
        viewHolder.title.setText(bean.itemTitle);
        viewHolder.content.setText(bean.itemContent);


        long end = System.nanoTime();
        long dValue = end-start;
        mSumTime +=dValue;
        System.out.println("**********"+mSumTime);

        return convertView;
        //文艺式》》》》》》》》》》》》》》》》》》》》》》》》》》》》
    }

    class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView content;
    }
}
