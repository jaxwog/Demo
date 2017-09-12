package com.wyz.binderpool;

import android.os.RemoteException;

/**
 * Created by wangyongzheng on 2017/4/27.
 */

public class PassWordImpl extends IPassWord.Stub {
    private static final char SECERT_CODE = '^';

    @Override
    public String encrypt(String content) throws RemoteException {
        char [] chars = content.toCharArray();
        for (int i = 0;i<chars.length;i++){
            chars[i] ^= SECERT_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
