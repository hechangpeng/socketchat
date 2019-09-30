package client.tcp.com.tcpclient.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
    public static final String CHAT_ID = "chat_id";
    public static final String CHAT_IMG = "chat_img";
    public static final String CHAT_NAME = "chat_name";
    public static final String NOTIFY_ID = "notify_id";

    /**
     * 保存在手机里面的文件名
     */
    private static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(context.getPackageName() + ".sp", Context.MODE_PRIVATE);
    }

    /**
     * String
     *
     * @param key
     * @param value
     */
    public static void putString(String key, String value, Context context) {
        getSp(context).edit().putString(key, value).commit();
    }

    public static String getString(String key, String def, Context context) {
        return getSp(context).getString(key, def);
    }

    public static String getString(String key, Context context) {
        return getString(key, "", context);
    }

    /**
     * boolean类型
     */
    public static void putBoolean(String key, boolean status, Context context) {
        getSp(context).edit().putBoolean(key, status).commit();
    }

    public static boolean getBoolean(String key, boolean def, Context context) {
        return getSp(context).getBoolean(key, def);
    }

    public static boolean getBoolean(String key, Context context) {
        return getBoolean(key, false, context);
    }

    /**
     * int类型
     */
    public static void putInt(String key, int position, Context context) {
        getSp(context).edit().putInt(key, position).commit();
    }

    public static int getInt(String key, int def, Context context) {
        return getSp(context).getInt(key, def);
    }

    public static int getInt(String key, Context context) {
        return getInt(key, 1, context);
    }

    /**
     * long
     *
     * @param key
     * @param value
     */
    public static void putLong(String key, long value, Context context) {
        getSp(context).edit().putLong(key, value).commit();
    }

    public static long getLong(String key, long def, Context context) {
        return getSp(context).getLong(key, def);
    }


    public static void putFloat(String key, Float value, Context context) {
        getSp(context).edit().putFloat(key, value).commit();
    }

    public static float getFloat(String key, long def, Context context) {
        return getSp(context).getFloat(key, def);
    }

}