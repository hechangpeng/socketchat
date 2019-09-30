package client.tcp.com.tcpclient.socket;

import com.google.gson.Gson;

import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import client.tcp.com.tcpclient.bean.MessageInfo;
import client.tcp.com.tcpclient.cache.Cache;
import client.tcp.com.tcpclient.view.OnPushMsgListener;

/**
 * Created by HECP2 on 2019-7-15.
 */

public final class SocketSDK {
    private SocketSDK() {

    }

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private boolean isRuning = false;
    private List<OnPushMsgListener> listeners = new ArrayList<>();
    private ArrayBlockingQueue<String> mMsgQueue = new ArrayBlockingQueue<String>(10);

    public void sendMsg(String msg) {
        try {
            mMsgQueue.put(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void addPushListener(OnPushMsgListener listener) {
        listeners.clear();
        listeners.add(listener);
    }

    public synchronized void removePushListener(OnPushMsgListener listener) {
        listeners.remove(listener);
    }

    public synchronized boolean isRuning() {
        return isRuning;
    }

    public synchronized void setRuning(boolean runing) {
        isRuning = runing;
    }

    private static class Holder {
        public static final SocketSDK INSTANCE = new SocketSDK();
    }

    public static SocketSDK getInstance() {
        return Holder.INSTANCE;
    }

    public synchronized void initSocket(OnPushMsgListener listener) {
        addPushListener(listener);
        System.out.println("isRuning=" + isRuning);
        if (isRuning) {
            return;
        }
        isRuning = true;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    socket = new Socket("Your server ip", 55555);
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setMsgType(2);
                    messageInfo.setFromUser(Cache.getInstance().chatId);
                    messageInfo.setUserImg(Cache.getInstance().chatImg);
                    messageInfo.setUserName(Cache.getInstance().chatName);
                    OutputStream dos = socket.getOutputStream();
                    String ss = new Gson().toJson(messageInfo) + "\r\n";
                    dos.write(ss.getBytes("UTF-8"));
                    executorService.execute(new SenderThread(socket, mMsgQueue));
                    executorService.execute(new ReceiverThread(socket, listeners));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("连接成功");
            }
        });
    }

}
