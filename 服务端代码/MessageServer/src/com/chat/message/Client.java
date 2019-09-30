package com.chat.message;

public class Client {
	public static void main(String[] args) {
		TcpServer tcpServer = new TcpServer();
		tcpServer.startReceive();
	}
}
