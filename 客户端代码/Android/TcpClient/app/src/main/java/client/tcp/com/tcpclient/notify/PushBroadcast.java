package client.tcp.com.tcpclient.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;

import java.util.UUID;

import client.tcp.com.tcpclient.MainActivity;
import client.tcp.com.tcpclient.R;
import client.tcp.com.tcpclient.bean.MessageInfo;
import client.tcp.com.tcpclient.cache.Cache;
import client.tcp.com.tcpclient.util.SpUtil;
import client.tcp.com.tcpclient.util.Util;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by HECP2 on 2019-7-16.
 */

public class PushBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra("push_msg");
        System.out.println("onReceive data=" + data);
        if (Cache.getInstance().activityNum != 0) {
            return;
        }
        try {
            if (Cache.getInstance().chatId == null) {
                Cache.getInstance().chatId = SpUtil.getString(SpUtil.CHAT_ID, UUID.randomUUID().toString().replace("-", ""), context);
            }
            MessageInfo messageInfo = new Gson().fromJson(data, MessageInfo.class);
            if (Cache.getInstance().chatId.equals(messageInfo.getFromUser())) {
                return;
            }
            int notifyId = Util.getNotifyId(context);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService
                    (NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setContentTitle(messageInfo.getMsgType() == 2 ? "系统通知" : messageInfo.getUserName() + " 给您发来了一条消息");
            mBuilder.setContentText(messageInfo.getMsgType() == 2 ? (messageInfo.getUserName() + " 加入了聊天") : messageInfo.getContent());
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
            mBuilder.setContentIntent(PendingIntent.getActivity(context, 200, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
            notificationManager.notify(notifyId, mBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
