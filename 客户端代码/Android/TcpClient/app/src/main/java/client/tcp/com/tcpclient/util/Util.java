package client.tcp.com.tcpclient.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by HECP2 on 2019-7-12.
 */

public class Util {
    public static final String YOUR_SERVER_IP = "";
    private static final String[] IMG_ARRAY = new String[]{
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_0.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_1.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_2.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_3.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_4.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_5.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_6.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_7.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_8.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_9.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_10.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_11.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_12.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_13.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_14.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_15.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_16.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_17.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_18.jpg",
            "http://" + YOUR_SERVER_IP + ":8091/imgs/img_19.jpg"
    };

    private static final String[] NAME_ARRAY = new String[]{
            "水德星君", "木德星君", "土德星君", "金德星君",
            "火德星君", "太白金星", "天蓬元帅", "齐天大圣",
            "东海龙王", "西海龙王", "北海龙王", "南海龙王",
            "太上老君", "二郎神", "王母娘娘", "金正焕",
            "金正峰", "成德善", "千颂伊", "成宝拉", "都敏俊",
            "许仙", "白蛇", "白浅", "张无忌", "周芷若", "赵敏",
            "灭绝师太", "龟仙人"};

    public static String getRandomImg() {
        int i = (int) (100 + Math.random() * (1000 - 100 + 1));
        return IMG_ARRAY[i % IMG_ARRAY.length];
    }

    public static String getRandomName() {
        int i = (int) (100 + Math.random() * (2000 - 100 + 1));
        return NAME_ARRAY[i % NAME_ARRAY.length];
    }

    public static String getCurrentTime() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(getUtcTimeMills());
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static String addZero(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return num + "";
    }

    public static long getUtcTimeMills() {
        try {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int date = c.get(Calendar.DATE);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int second = c.get(Calendar.SECOND);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            String timeString = year + "-" + addZero(month) + "-" + addZero(date) + " " + addZero(hour) + ":"
                    + addZero(minute) + ":" + addZero(second);
            Date d = sdf.parse(timeString);
            return d.getTime();
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }

    public static long dateToStamp(String time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(time);
            return date.getTime();
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }

    public static synchronized int getNotifyId(Context context) {
        int notifyId = SpUtil.getInt(SpUtil.NOTIFY_ID, 1, context);
        SpUtil.putInt(SpUtil.NOTIFY_ID, notifyId + 1, context);
        return notifyId;
    }

    public static boolean isNetworkConnected(Context context) {
        try {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isConnected();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
