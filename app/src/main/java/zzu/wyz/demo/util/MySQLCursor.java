package zzu.wyz.demo.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库的查询操作
 * 考虑问题，打开关闭数据库分开方法
 * Created by WYZ on 2016/1/11.
 */
public class MySQLCursor {
    //要操作的数据库表名
    private static final String TABLENAME = "wyz";
    private SQLiteDatabase database;

    public MySQLCursor(SQLiteDatabase database){
       this.database = database;
    }


    //得到数据总量，根据数据总量以及每页显示的条数进行分页
    public int getCount(){
        int count = 0;
        String sql = "SELECT COUNT(id) FROM " + TABLENAME; // 查询SQL
        Cursor result = this.database.rawQuery(sql, null);
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
         // 采用循环的方式检索数据
            count = result.getInt(0);
        }

        return count;
    }

    /**
     *
     * @param currentPage   当前显示的页数
     * @param lineSize       每页显示的数据数量
     * @return                查询到的数据保存到List<Map<String,Object>>中，为了方便ListView的显示
     */
    //本地数据库分页全部查询，使用SimpleAdapter显示ListView
    public List<Map<String,Object>> currentFind(int currentPage,int lineSize){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();

        //使用SQL进行模糊查询分页
        String sql = "SELECT id,name,birthday FROM " + TABLENAME
                + " LIMIT ?,?" ;

        String args[] = new String[] {String.valueOf((currentPage - 1)*lineSize),
                String.valueOf(lineSize)  };
        Cursor cursor = this.database.rawQuery(sql, args); // 执行查询语句

        //循环检索数据内容,得到返回值保存到变量中
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            Map<String,Object>map = new HashMap<String, Object>();
            map.put("queryId",cursor.getInt(0));
            map.put("queryName",cursor.getString(1));
            map.put("queryBirthday",cursor.getString(2));

            list.add(map);

        }
        this.database.close();
        return list;
    }










    /**
     * sql	        the SQL query. The SQL string must not be ; terminated
    * selectionArgs	  You may include ?s in where clause in the query,
                        which will be replaced by the values from selectionArgs.
                      The values will be bound as Strings.
     * @return 保存了查询到的数据的List集合
     */

    /**
     * moveToFirst      Move the cursor to the first row.
     *isAfterLast       Returns whether the cursor is pointing to the position after the last row.
     *moveToNext        Move the cursor to the next row.
     */

    //查询到的数据以List集合的形式返回
    public List<String> find(){
        List<String> list = new ArrayList<String>();

       /* //使用SQL进行查询全部
        String sql = "SELECT id,name,birthday FROM " + TABLENAME ;
        Cursor cursor = this.database.rawQuery(sql,null);
        */

      /*  //使用内建query查询全部
        String columns[] = new String[] { "id", "name", "birthday" };
        Cursor cursor = this.database.query(TABLENAME, columns, null, null, null,
                null, null);	// 这些条件根据自己的情况增加
        */

       /* //使用SQL进行模糊查询
        String sql = "SELECT id,name,birthday FROM " + TABLENAME
                + " WHERE name LIKE ? OR birthday LIKE ?" ;
        // 查询关键字 ，应该由方法定义
        String keyWord = "3" ;
        String args[] = new String[] { "%" + keyWord + "%", "%" + keyWord + "%" };
        Cursor cursor = this.database.rawQuery(sql, args); // 执行查询语句

*/

      /* //使用内建进行模糊查询
        String columns[] = new String[] { "id", "name", "birthday" };
        String keyWord = "3" ;	// 查询关键字 ，应该由方法定义
        String selectionArgs[] = new String[] { "%" + keyWord + "%", "%" + keyWord + "%" };
        String selection = "name LIKE ? OR birthday LIKE ?" ;
        Cursor cursor = this.database.query(TABLENAME, columns, selection, selectionArgs, null,
                null, null);	// 这些条件根据自己的情况增加
*/

          //使用SQL进行模糊查询分页
        String sql = "SELECT id,name,birthday FROM " + TABLENAME
                + " WHERE (name LIKE ? OR birthday LIKE ?)LIMIT ?,?" ;
        int currentPage = 3;//当前显示的页数,现在在第一页
        int lineSize = 5;//没页显示的数量
        // 查询关键字 ，应该由方法定义
        String keyWord = "王" ;
        String args[] = new String[] { "%" + keyWord + "%", "%" + keyWord + "%" ,
              String.valueOf((currentPage - 1)*lineSize),String.valueOf(lineSize)  };
        Cursor cursor = this.database.rawQuery(sql, args); // 执行查询语句


        int cursorId;
        String cursorName,cursorBirthday;
        //循环检索数据内容,得到返回值保存到变量中
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){

            cursorId = cursor.getInt(0);
            cursorName = cursor.getString(1);
            cursorBirthday = cursor.getString(2);

            list.add("【"+cursorId+"】"+" - "+cursorName+" - "+cursorBirthday);

        }

        this.database.close();
        return list;
    }
}
