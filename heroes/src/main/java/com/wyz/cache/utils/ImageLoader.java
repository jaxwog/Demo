package com.wyz.cache.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;


import com.zzu.wyz.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wangyongzheng on 2017/8/21.
 */

public class ImageLoader {

    private static final String TAG = "ImageLoader";
    private static final long DISK_CACHE_SIZE = 1024*1024*50;
    private boolean mIsDiskLruCacheCreated = false;
    private static final int DISK_CACHE_INDEX = 0;
    private static final int IO_BUFFER_SIZE = 1024*8;
    private static final int TAG_KEY_URI = R.id.imageloader_uri;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT+1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT*2+1;
    private static final long KEEP_ALIVE = 10L;

    public static final int MESSAGE_POST_RESULT = 1;

    private Context mContext;
    private LruCache<String,Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;
    private ImageResizer mImageResizer = new ImageResizer();

   private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
       private final AtomicInteger mCount = new AtomicInteger(1);
       @Override
       public Thread newThread(@NonNull Runnable r) {
           return new Thread(r,"ImageLoader#"+mCount.getAndIncrement());
       }
   };

   public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,
           KEEP_ALIVE, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(),THREAD_FACTORY);

    private Handler mMainHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            imageView.setImageBitmap(result.bitmap);
            String uri = (String) imageView.getTag(TAG_KEY_URI);
            if (uri.equals(result.url)){
                imageView.setImageBitmap(result.bitmap);
            }else {
                Log.d(TAG, "set image bitmap,but url has changed ,ignored");
            }
        }
    };

    private ImageLoader(Context context){
        mContext = context.getApplicationContext();
        //注册内存缓存策略
        int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        //内存缓存为当前进程的可用内存的1/8
        int cacheSize = maxMemory / 6;
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight()/1024;
            }
        };

        File diskCacheDir = getDiskCacheDir(mContext,"bitmap");
        if (!diskCacheDir.exists()){
            diskCacheDir.mkdir();
        }
        if (getUsableSpace(diskCacheDir)> DISK_CACHE_SIZE){
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir,1,1,DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static ImageLoader build(Context context){
        return new ImageLoader(context);
    }


    private  Bitmap loadBitmapFromHttp(String url,int reqWidth,int reqHeight) throws IOException {
        if (Looper.myLooper()==Looper.getMainLooper()){
            throw new RuntimeException("不能在主线程中网络下载文件");
        }
        if (mDiskLruCache ==null){
            return null;
        }
        String key = hashKeyFromUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if (editor!=null){
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);

            if (downLoadUrlToStream(url,outputStream)){
                editor.commit();//提交写入操作
            }else {
                editor.abort();//回退整个操作
            }
            mDiskLruCache.flush();
        }
       return loadBitmapFromDiskCache(url,reqWidth,reqHeight);
    }


    private Bitmap loadBitmapFromDiskCache(String url,int reqWidth,int reqHeight) throws IOException {
        if (Looper.myLooper()==Looper.getMainLooper()){
            Log.i(TAG, "Load Bitmap from UI Thread,it's not recommended! ");
        }
        if (mDiskLruCache ==null){
            return null;
        }
        Bitmap bitmap  = null;
        String key = hashKeyFromUrl(url);
        //磁盘缓存读取需要通过Snapshot来完成，得到磁盘缓存对象FileInputStream，
        // 通过FileDescriptor来加载压缩后的图片，最后将加载的BitMap加载到内存缓存中
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if (snapshot !=null){
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = mImageResizer.decodeSampleBitmapFromFileDescriptor(fileDescriptor,reqWidth,reqHeight);
            if (bitmap!=null){
                addBitmapToMemoryCache(key,bitmap);
            }
        }
        return bitmap;
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key)==null){
            mMemoryCache.put(key,bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key){
        return mMemoryCache.get(key);
    }
    //从网络下载图片，通过文件流写入到文件系统上
    public boolean downLoadUrlToStream(String urlString,OutputStream outputStream){
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            out  = new BufferedOutputStream(outputStream,IO_BUFFER_SIZE);
            int b;
            while ((b = in.read())!=-1){
                out.write(b);
            }
            return true;
        }catch (IOException e){
            Log.e(TAG, "downLoadUrlToStream: fialed"+e);
        }finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            MyUtils.close(out);
            MyUtils.close(in);
        }
        return false;
    }
    public void bindBitmap(final String url, final ImageView imageView){
        bindBitmap(url,imageView,0,0);
    }


    public void bindBitmap(final String url, final ImageView imageView, final int reqWidth, final int reqHeight){
        imageView.setTag(TAG_KEY_URI,url);
        final Bitmap bitmap = loadBitmapFromMemCache(url);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }

        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1 = loadBitmap(url,reqWidth,reqHeight);
                if (bitmap1!=null){
                    LoaderResult result = new LoaderResult(imageView,url,bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT,result).sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    private Bitmap loadBitmap(String url,int reqWidth,int reqHeight){
        Bitmap bitmap = loadBitmapFromMemCache(url);
        if (bitmap!=null){
            Log.i(TAG, "loadBitmapFromMemCache:LruCache from url = "+url);
            return bitmap;
        }
        try {
            bitmap = loadBitmapFromDiskCache(url,reqWidth,reqHeight);
            if (bitmap!=null){
                Log.i(TAG, "loadBitmapFromDiskCache:DiskLruCache from url = "+url);
                return bitmap;
            }
            bitmap = loadBitmapFromHttp(url,reqWidth,reqHeight);
            Log.i(TAG, "loadBitmapFromHttp: HTTP from url = "+url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap==null&&!mIsDiskLruCacheCreated){
            Log.i(TAG, "encounter error,DiskLruCache is not created. ");
            bitmap = downLoadBitmapFromUrl(url);
        }

        return bitmap;
    }

    private Bitmap downLoadBitmapFromUrl(String url) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            final URL url1 = new URL(url);
            urlConnection = (HttpURLConnection) url1.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "downLoadBitmapFromUrl: Error");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            MyUtils.close(in);
        }
        return bitmap;
    }


    //从内存中获取图片
    private Bitmap loadBitmapFromMemCache(String url){
        final String key = hashKeyFromUrl(url);
        Bitmap bitmap = getBitmapFromMemCache(key);
        return bitmap;
    }

    //获取缓存文件路径，返回File对象
    public File getDiskCacheDir(Context context,String uniqueName){
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable){
            cachePath = context.getExternalCacheDir().getPath();
        }else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator+ uniqueName);
    }

    //编译版本大于2.3 即：9采用此方法
    private long getUsableSpace(File file){
        return file.getUsableSpace();
    }

    //将url转化为MD5，用于key
    private String hashKeyFromUrl(String url){
        String cacheKay;
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            cacheKay = bytesToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
           cacheKay = String.valueOf(url.hashCode());
        }
        return cacheKay;
    }


    //将字节数组转化为String字符串
    private String bytesToHexString(byte[] bytes){
        StringBuilder sb= new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF&bytes[i]);
            if (hex.length()==1){
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private static class LoaderResult{
        public ImageView imageView;
        public String url;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView,String url,Bitmap bitmap){
            this.bitmap = bitmap;
            this.imageView = imageView;
            this.url = url;

        }
    }


}
