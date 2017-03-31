package zzu.wyz.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by WYZ on 2015/12/18.
 */
public interface MetaDataInterface {
    //保存到本地的文件路径
    public static final String FILE_PATH = Environment.getExternalStorageDirectory()
            + File.separator + "WanHe" + File.separator;


}
