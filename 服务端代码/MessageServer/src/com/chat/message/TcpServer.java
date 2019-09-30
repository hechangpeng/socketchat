package com.chat.message;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {
	private ServerSocket serverSocket;
	private HashMap<String, Socket> userInfos;
	private ExecutorService executorService;

	public TcpServer() {
		try {
			this.serverSocket = new ServerSocket(55555);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.userInfos = new HashMap<String, Socket>();
		executorService = Executors.newCachedThreadPool();
	}

	public synchronized void addUser(String userId, Socket socket) {
		userInfos.put(userId, socket);
	}

	public synchronized void removeUser(String userId) {
		Socket preSocket = userInfos.get(userId);
		try {
			if (preSocket != null) {
				preSocket.close();
			}
			userInfos.remove(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void sendToAll(String message, String fromUser, boolean notifySelf) {
		Iterator<Entry<String, Socket>> iterator = userInfos.entrySet().iterator();
		if (notifySelf) {
			while (iterator.hasNext()) {
				Entry<String, Socket> next = iterator.next();
				Socket socket = next.getValue();
				try {
					if (socket != null) {
						socket.getOutputStream().write((message + "\r\n").getBytes("UTF-8"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return;
		}
		while (iterator.hasNext()) {
			Entry<String, Socket> next = iterator.next();
			if (next.getKey() != null && !next.getKey().equals(fromUser)) {
				Socket socket = next.getValue();
				try {
					if (socket != null) {
						socket.getOutputStream().write((message + "\r\n").getBytes("UTF-8"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized void sendToSomeone(String message, String userId, String fromUser) {
		/*
		 * if (userId != null && userId.equals(fromUser)) { return; }
		 */
		Socket socket = userInfos.get(userId);
		try {
			if (socket != null) {
				socket.getOutputStream().write((message + "\r\n").getBytes("UTF-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startReceive() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				InetAddress inetAddress = socket.getInetAddress();
				System.out.println("已经成功建立连接  : " + inetAddress.getHostAddress());
				executorService.execute(new MsgSolveThread(socket, TcpServer.this));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
