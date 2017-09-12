package com.wyz.binderpool;

import android.os.RemoteException;

/**
 * Created by wangyongzheng on 2017/4/27.
 */

public class ComputeImpl extends ICompute.Stub {

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
