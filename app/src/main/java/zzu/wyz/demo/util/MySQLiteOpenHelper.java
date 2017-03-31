package zzu.wyz.demo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * 数据库创建，与版本更新
 * Created by WYZ on 2016/1/11.
 */
public class MySQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper{

    //数据库名称
    public static final String DATABASENAME = "zzu.db";
    //创建的表名称
    public static final String TABLENAME = "wyz";
    //创建的数据库版本号,不能高版本向低版本转换
    public static final int DATABASEVERSION = 1;

//数据库创建到内存卡上，并不在是私有化（只有本程序可以打开，除了ContentProvider）的数据库
    public static final String PATH = Environment.getExternalStorageDirectory()
            +File.separator+"zzu"+File.separator;

/**
 * Context  to use to open or create the database
 * name   of the database file, or null for an in-memory database
 * factory    to use for creating cursor objects, or null for the default
 * version    number of the database (starting at 1); if the database is older,
 * onUpgrade(SQLiteDatabase, int, int) will be used to upgrade the database;
 * if the database is newer,
 * onDowngrade(SQLiteDatabase, int, int) will be used to downgrade the database

 */
    public MySQLiteOpenHelper(Context context){
        //创建数据库到存储卡上
        //super(context,PATH+DATABASENAME,null,DATABASEVERSION);
        super(context,DATABASENAME,null,DATABASEVERSION);

    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    //创建数据库和表
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 在SQLite中设置为Integer、PRIMARY KEY则ID自动增长;不设置Integer需要自定义数据
        String sql = "CREATE TABLE " + TABLENAME + "("
                + "id		INTEGER			PRIMARY KEY ,"
                + "name 	VARCHAR(50) 	NOT NULL ,"
                + "birthday DATE NOT 		NULL" + ")";
        // 执行SQL
        db.execSQL(sql) ;
        System.out.println("****************** 创建：onCreate()。");


    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLENAME ;
        db.execSQL(sql) ;
        System.out.println("****************** 更新：onUpgrade()。");

        this.onCreate(db) ;

    }
}
