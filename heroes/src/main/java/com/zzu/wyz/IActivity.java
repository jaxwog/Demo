package com.zzu.wyz;

/**
 * Created by WYZ on 2017/3/2.
 */
public interface IActivity {
    /**
     * 进行控件初始化操作
     */
    void initView();

    /**
     * 进行数据初始化操作
     */
    void  initData();

    /**
     * 进行事件监听处理
     */
    void  registerListener();
}
