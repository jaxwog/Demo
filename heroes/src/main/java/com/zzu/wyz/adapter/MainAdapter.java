package com.zzu.wyz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.wyz.R;

import java.util.List;


/**
 * Created by WYZ on 2017/3/2.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mlist;
    public OnItemClickListener itemClickListener;

    public MainAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mlist = list;

    }
    public void setOnItemClickListener(
            OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //将xml文件转换为view视图，并与viewholder绑定
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //将数据与视图关联起来
        holder.textView.setText(this.mlist.get(position)+position);

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getPosition());
            }
        }
    }


}
//class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
//{
//
//    //...
//    public interface OnItemClickLitener
//    {
//        void onItemClick(View view, int position);
//        void onItemLongClick(View view , int position);
//    }
//
//    private OnItemClickLitener mOnItemClickLitener;
//
//    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
//    {
//        this.mOnItemClickLitener = mOnItemClickLitener;
//    }
//
//    @Override
//    public void onBindViewHolder(final MyViewHolder holder, final int position)
//    {
//        holder.tv.setText(mDatas.get(position));
//
//        // 如果设置了回调，则设置点击事件
//        if (mOnItemClickLitener != null)
//        {
//            holder.itemView.setOnClickListener(new OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    int pos = holder.getLayoutPosition();
//                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
//                }
//            });
//
//            holder.itemView.setOnLongClickListener(new OnLongClickListener()
//            {
//                @Override
//                public boolean onLongClick(View v)
//                {
//                    int pos = holder.getLayoutPosition();
//                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
//                    return false;
//                }
//            });
//        }
//    }
////...
//}