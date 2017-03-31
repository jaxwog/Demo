package zzu.wyz.demo.util;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库操作
 * Created by WYZ on 2016/1/11.
 * Exposes methods to manage a SQLite database.

 SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.

 See the Notepad sample application in the SDK for an example of creating and managing a database.

 Database names must be unique within an application, not across all applications.
 */
public class MySQLOperate {
   private SQLiteDatabase database = null;

    //要操作的数据库表名
    private static final String TABLENAME = "wyz";

    public MySQLOperate(SQLiteDatabase database){
        this.database = database;
    }

    /**
     * 添加数据，打开数据库，关闭数据库是耗时操作，等数据库操作完成再关闭
     * @param name    需要添加的数据库name列里面的数据
     * @param birthday 需要添加的数据库列里面birthday的数据
     */
    public void insert(String name,String birthday) {

       /* //拼凑SQL语句进行数据添加
        String sql = "INSERT INTO " + TABLENAME + "(name,birthday) VALUES ('"
                + name + "','" + birthday + "')";
        this.database.execSQL(sql) ;*/

        /*//使用占位符进行数据添加
        String sql = "INSERT INTO " + TABLENAME + "(name,birthday) VALUES (?,?)";
        Object args[] = new Object[] { name, birthday };
        this.database.execSQL(sql, args);*/

        /**
         *  SQLiteDatabase.insert(table,nullColumnHack,values)
         * table         	the table to insert the row into
         nullColumnHack	           optional; may be null.
                    SQL doesn't allow inserting a completely empty row without naming at least one column name.
                    If your provided values is empty,
                    no column names are known and an empty row can't be inserted.
                    If not set to null, the nullColumnHack parameter provides the name of
                     nullable column name to explicitly insert a NULL into in the case where
                    your values is empty.
         values	     this map contains the initial column values for the row. The keys should be the column names and the values the column values
         */

        //ContentValues
        ContentValues cv = new ContentValues() ;
        cv.put("name", name) ;
        cv.put("birthday", birthday) ;
        this.database.insert(TABLENAME, null, cv) ;

        this.database.close() ;
    }

    /**
     * 改数据（更新数据）
     * @param id  自动增长的id编号
     * @param name 替换掉当前id上的name值
     * @param birthday   替换掉当前id上的birthday值
     */
    public void update(int id, String name, String birthday) {

      /*  String sql = "UPDATE " + TABLENAME + " SET name='" + name
                + "',birthday='" + birthday + "' WHERE id=" + id;
        this.database.execSQL(sql);*/

     /*   //使用占位符进行数据更新
        String sql = "UPDATE " + TABLENAME + " SET name=?,birthday=? WHERE id=?";
        Object args[] = new Object[] { name, birthday, id };
        this.database.execSQL(sql, args);*/

/**
 *public int update (String table, ContentValues values, String whereClause, String[] whereArgs)
 * table	the table to update in
 values	a map from column names to new column values. null is a valid value that will be translated to NULL.
 whereClause	the optional WHERE clause to apply when updating. Passing null will update all rows.
 whereArgs	You may include ?s in the where clause, which will be replaced by the values from whereArgs. The values will be bound as Strings.
 */

        ContentValues cv = new ContentValues();
        cv.put("name", name) ;
        cv.put("birthday", birthday) ;
        String whereClause = "id=?" ;
        String whereArgs[] = new String[]{String.valueOf(id)} ;
        this.database.update(TABLENAME,cv,whereClause,whereArgs);


        this.database.close() ;
    }

    /**
     * 删除数据
     * @param id 根据id删除当前id的数据
     */
    public void delete(int id) {

        /*String sql = "DELETE FROM " + TABLENAME + " WHERE id=" + id ;
        this.database.execSQL(sql) ;*/

       /* String sql = "DELETE FROM " + TABLENAME + " WHERE id=?";
        Object args[] = new Object[] { id };
        this.database.execSQL(sql,args) ;*/

        /**public int delete (String table, String whereClause, String[] whereArgs)
         * table	the table to delete from
         whereClause	the optional WHERE clause to apply when deleting. Passing null will delete all rows.
         whereArgs	You may include ?s in the where clause, which will be replaced by the values from whereArgs. The values will be bound as Strings.
         */

        ContentValues cv = new ContentValues();
        String whereClause = "id=?" ;
        String whereArgs[] = new String[]{String.valueOf(id)} ;
        this.database.delete(TABLENAME,whereClause,whereArgs);


        this.database.close() ;
    }
}
