package client.tcp.com.tcpclient.socket;

import com.google.gson.Gson;

import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

import client.tcp.com.tcpclient.bean.MessageInfo;
import client.tcp.com.tcpclient.cache.Cache;

/**
 * Created by HECP2 on 2019-7-11.
 */

public class SenderThread implements Runnable {
    private Socket mSocket;
    private ArrayBlockingQueue<String> mMsgQueue;

    public SenderThread(Socket socket, ArrayBlockingQueue<String> msgQueue) {
        this.mSocket = socket;
        this.mMsgQueue = msgQueue;
    }

    @Override
    public void run() {
        while (SocketSDK.getInstance().isRuning()) {
            try {
                String msg = mMsgQueue.take();
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent(msg);
                messageInfo.setFromUser(Cache.getInstance().chatId);
                messageInfo.setToUser(null);
                messageInfo.setMsgType(1);
                messageInfo.setUserImg(Cache.getInstance().chatImg);
                messageInfo.setUserName(Cache.getInstance().chatName);
                OutputStream dos = mSocket.getOutputStream();
                dos.write((new Gson().toJson(messageInfo) + "\r\n").getBytes("UTF-8"));
            } catch (SocketException se) {
                se.printStackTrace();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SocketSDK.getInstance().setRuning(false);
    }
}
