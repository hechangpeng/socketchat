package client.tcp.com.tcpclient.socket;

import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import client.tcp.com.tcpclient.MainApplication;
import client.tcp.com.tcpclient.bean.MessageInfo;
import client.tcp.com.tcpclient.db.DbManager;
import client.tcp.com.tcpclient.view.OnPushMsgListener;

/**
 * Created by HECP2 on 2019-7-11.
 */

public class ReceiverThread implements Runnable {
    private Socket mSocket;
    private List<OnPushMsgListener> mListeners;

    public ReceiverThread(Socket socket, List<OnPushMsgListener> listeners) {
        this.mSocket = socket;
        this.mListeners = listeners;
    }

    @Override
    public void run() {
        while (SocketSDK.getInstance().isRuning()) {
            try {
                InputStream inputStream = mSocket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String s;
                final StringBuilder stringBuilder = new StringBuilder("");
                while ((s = bufferedReader.readLine()) != null) {
                    stringBuilder.append(s);
                    break;
                }
                cacheMsg(stringBuilder.toString());
                Intent intent = new Intent();
                intent.setAction("client.tcp.com.tcpclient.PUSH_MSG");
                intent.putExtra("push_msg", stringBuilder.toString());
                MainApplication.getInstance().sendBroadcast(intent);
                for (OnPushMsgListener listener : mListeners) {
                    listener.onMsgPush(stringBuilder.toString());
                }
            } catch (SocketException se) {
                se.printStackTrace();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SocketSDK.getInstance().setRuning(false);
    }

    private void cacheMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        try {
            MessageInfo messageInfo = new Gson().fromJson(msg, MessageInfo.class);
            DbManager.getInstance(MainApplication.getInstance()).insertPushMsg(messageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
