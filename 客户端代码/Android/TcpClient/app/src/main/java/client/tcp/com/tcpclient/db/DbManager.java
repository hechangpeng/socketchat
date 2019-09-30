package client.tcp.com.tcpclient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import client.tcp.com.tcpclient.UserInfoActivity;
import client.tcp.com.tcpclient.bean.MessageInfo;

/**
 * Date：2018/1/19
 * Author：HeChangPeng
 */

public class DbManager {
    private static DbManager singleton;

    private DbManager() {
    }

    public static DbManager getInstance(Context context) {
        if (singleton == null) {
            synchronized (DbManager.class) {
                if (singleton == null) {
                    singleton = new DbManager(context.getApplicationContext());
                }
            }
        }
        return singleton;
    }

    private DBHelper helper;

    private DbManager(Context context) {
        helper = new DBHelper(context);
    }

    public synchronized void insertPushMsg(MessageInfo messageInfo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String[] toUser = messageInfo.getToUser();
        StringBuilder stringBuilder = new StringBuilder("");
        if (toUser != null && toUser.length == 0) {
            for (String s : toUser) {
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        contentValues.put(MessageTable.COLUMN_CONTENT, messageInfo.getContent());
        contentValues.put(MessageTable.COLUMN_FROMUSER, messageInfo.getFromUser());
        contentValues.put(MessageTable.COLUMN_MSG_TYPE, messageInfo.getMsgType());
        contentValues.put(MessageTable.COLUMN_TIMESTAMP, messageInfo.getTimeStamp());
        contentValues.put(MessageTable.COLUMN_USERIMG, messageInfo.getUserImg());
        contentValues.put(MessageTable.COLUMN_USERNAME, messageInfo.getUserName());
        contentValues.put(MessageTable.COLUMN_TOUSER, stringBuilder.toString());
        db.insert(MessageTable.TABLE_NAME, null, contentValues);
    }

    public synchronized List<MessageInfo> getMsgList() {
        List<MessageInfo> msgList = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MessageTable.TABLE_NAME + " order by " + MessageTable.COLUMN_TIMESTAMP + " asc", null);
        MessageInfo messageInfo;
        while (cursor.moveToNext()) {
            messageInfo = new MessageInfo();
            messageInfo.setContent(cursor.getString(cursor.getColumnIndex(MessageTable.COLUMN_CONTENT)));
            messageInfo.setUserName(cursor.getString(cursor.getColumnIndex(MessageTable.COLUMN_USERNAME)));
            messageInfo.setUserImg(cursor.getString(cursor.getColumnIndex(MessageTable.COLUMN_USERIMG)));
            messageInfo.setFromUser(cursor.getString(cursor.getColumnIndex(MessageTable.COLUMN_FROMUSER)));
            messageInfo.setTimeStamp(cursor.getString(cursor.getColumnIndex(MessageTable.COLUMN_TIMESTAMP)));
            messageInfo.setMsgType(cursor.getInt(cursor.getColumnIndex(MessageTable.COLUMN_MSG_TYPE)));
            String toUser = cursor.getString(cursor.getColumnIndex(MessageTable.COLUMN_TOUSER));
            messageInfo.setToUser((!TextUtils.isEmpty(toUser)) ? toUser.split(",") : null);
            msgList.add(messageInfo);
        }
        cursor.close();
        return msgList;
    }

    public synchronized HashMap<String, String> getUsetChatData(String fromUser) {
        HashMap<String, String> resultList = new HashMap<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MessageTable.TABLE_NAME + " where " + MessageTable.COLUMN_FROMUSER + "=? order by " + MessageTable.COLUMN_TIMESTAMP + " desc", new String[]{fromUser});
        int msgCount = cursor.getCount();
        if (msgCount == 0) {
            cursor.close();
            return resultList;
        }
        cursor.moveToFirst();
        resultList.put(UserInfoActivity.KEY_LAST_MSG_TIME, cursor.getString(cursor.getColumnIndex(MessageTable.COLUMN_TIMESTAMP)));
        cursor.close();
        DecimalFormat df = new DecimalFormat("#.##");
        resultList.put(UserInfoActivity.KEY_ALIVE_RATE, df.format(msgCount * 1.0d / getAllMsgCount() * 100) + "%");
        return resultList;
    }

    public synchronized int getAllMsgCount() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) as intCount from " + MessageTable.TABLE_NAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(cursor.getColumnIndex("intCount"));
        cursor.close();
        return count;
    }
}