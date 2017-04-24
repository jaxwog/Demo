package com.wyz.bean;

import java.io.Serializable;

/**
 * Created by wangyongzheng on 2017/4/13.
 */

public class UserBean implements Serializable{
    private static final Long serialVersionUID = 1l;


    public int userId;
    public String userName;
    public boolean isMale;


    public UserBean(int id,String name,boolean b){
        this.userId = id;
        this.userName = name;
        this.isMale = b;
    }



    @Override
    public String toString() {

        String s  = String.format("userId:%s,userName:%s, isMale:%s",userId,userName,isMale);
        String ss = "userId = "+userId+";userName = "+userName + ";isMale = "+ isMale;
        return ss;


    }
}
