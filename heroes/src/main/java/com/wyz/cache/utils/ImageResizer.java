package com.wyz.cache.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * Created by wangyongzheng on 2017/8/21.
 */

public class ImageResizer {

    private static final String TAG = "ImageResizer";

public ImageResizer(){}

     public Bitmap decodeSampledBitmapFromResource(Resources res,int resId,int reqWidth,int reqHeight){
        //①创建options，设置options参数inJustDecodeBounds为true（不加载到内存中）
          final  BitmapFactory.Options options = new BitmapFactory.Options();
          options.inJustDecodeBounds = true;
          BitmapFactory.decodeResource(res,resId,options);
          //②获取原始图片信息，③计算出需要缩放的比例
          options.inSampleSize = calcuteInSampleSize(options,reqWidth,reqHeight);
          //④设置options参数inJustDecodeBounds为false，加载缩放后的图片到内存中
          options.inJustDecodeBounds = false;
          return BitmapFactory.decodeResource(res,resId,options);
     }


     public Bitmap decodeSampleBitmapFromFileDescriptor(FileDescriptor fd,int reqWidth,int reqHeight){
       final   BitmapFactory.Options options= new BitmapFactory.Options();
       options.inJustDecodeBounds = true;
       BitmapFactory.decodeFileDescriptor(fd,null,options);
       options.inSampleSize = calcuteInSampleSize(options,reqWidth,reqHeight);
       options.inJustDecodeBounds = false;
         return BitmapFactory.decodeFileDescriptor(fd,null,options);
     }

    public int calcuteInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        if (reqHeight==0||reqWidth==0){
            return 1;
        }
        //获取原始图片的宽和高
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(TAG, "calcuteInSampleSize: w = "+width+" ,h = "+height);
        //如果原始宽或高小于请求的宽或高直接返回缩放比率1
        int inSampleSize = 1;
        //如果原始宽或高大于请求的宽或高进行计算缩放比率
        if (height>reqHeight||width>reqHeight){
            final int halfHeight = height/2;
            final int halfWidth = width/2;
            while ((halfHeight/inSampleSize)>=reqHeight
                    &&(halfWidth/inSampleSize)>=reqWidth){
                inSampleSize *= 2;
            }
        }
        Log.d(TAG, "inSampleSize =  "+inSampleSize);
       return inSampleSize;
    }


}
