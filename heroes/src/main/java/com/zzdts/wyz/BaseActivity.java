package com.zzdts.wyz;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by wangyongzheng on 2017/5/20.
 */

public class BaseActivity extends Activity {



    public boolean hasPermission(String... premissions){
        for (String premission:premissions ){
            if (ContextCompat.checkSelfPermission(this,premission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;

    }

    public void requestPermission(int code ,String... permissions){
        ActivityCompat.requestPermissions(this,permissions,code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode){
           case 0x01:
               if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

               }
               break;
       }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
