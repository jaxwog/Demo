package com.zzu.wyz.listviewbaseadapter;

/**
 * Created by WYZ on 2016/1/27.
 */
public class ItemBean {
    public int itemImageResid;
    public String itemTitle;
    public String itemContent;

    public ItemBean(int itemImageResid,String itemTitle,String itemContent){
        this.itemImageResid = itemImageResid;
        this.itemTitle = itemTitle;
        this.itemContent = itemContent;
    }
}
