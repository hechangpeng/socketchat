package com.chat.message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import com.google.gson.Gson;

public class MsgSolveThread implements Runnable {
	private Socket mSocket;
	private MessageInfo messageInfo;
	private TcpServer tcpServer;
	public static final String END_CHAR = "k232kje3eft89ere";

	public MsgSolveThread(Socket socket, TcpServer tcpServer) {
		this.mSocket = socket;
		this.tcpServer = tcpServer;
	}

	@Override
	public void run() {
		while (true) {
			try {
				InputStream inputStream = mSocket.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				String s;
				StringBuilder stringBuilder = new StringBuilder("");
				while ((s = bufferedReader.readLine()) != null) {
					System.out.println(s);
					stringBuilder.append(s);
					break;
				}
				messageInfo = new Gson().fromJson(stringBuilder.toString(), MessageInfo.class);
				if (messageInfo != null) {
					messageInfo.setTimeStamp(Utils.getCurrentTime());
					tcpServer.addUser(messageInfo.getFromUser(), mSocket);
					String[] toUser = messageInfo.getToUser();
					int msgType = messageInfo.getMsgType();
					if (msgType == 0) {
						// 单发
						for (String string : toUser) {
							MessageInfo sendMsg = new MessageInfo();
							sendMsg.setTimeStamp(Utils.getCurrentTime());
							sendMsg.setContent(messageInfo.getContent());
							sendMsg.setFromUser(messageInfo.getFromUser());
							sendMsg.setUserImg(messageInfo.getUserImg());
							sendMsg.setUserName(messageInfo.getUserName());
							sendMsg.setToUser(new String[] { string });
							sendMsg.setMsgType(0);
							tcpServer.sendToSomeone(new Gson().toJson(sendMsg), string, messageInfo.getFromUser());
						}
					} else if (msgType == 1) {
						// 群发
						messageInfo.setMsgType(1);
						tcpServer.sendToAll(new Gson().toJson(messageInfo), messageInfo.getFromUser(), true);
					} else if (msgType == 2) {
						// 接入
						messageInfo.setMsgType(2);
						tcpServer.sendToAll(new Gson().toJson(messageInfo), messageInfo.getFromUser(), false);
					}
				}
			} catch (SocketException e) {
				e.printStackTrace();
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
