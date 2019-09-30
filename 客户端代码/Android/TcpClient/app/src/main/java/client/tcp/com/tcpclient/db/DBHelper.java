package client.tcp.com.tcpclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Date：2018/1/19
 * Author：HeChangPeng
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "msgdb.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createMessageTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * 创建图片下载表
     */
    private void createMessageTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + MessageTable.TABLE_NAME +
                "(" + MessageTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MessageTable.COLUMN_MSG_TYPE + " INTEGER,"
                + MessageTable.COLUMN_FROMUSER + " VARCHAR,"
                + MessageTable.COLUMN_TOUSER + " VARCHAR,"
                + MessageTable.COLUMN_TIMESTAMP + " VARCHAR,"
                + MessageTable.COLUMN_USERNAME + " VARCHAR,"
                + MessageTable.COLUMN_USERIMG + " VARCHAR,"
                + MessageTable.COLUMN_CONTENT + " VARCHAR)");
    }
}
