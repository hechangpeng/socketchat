package com.chat.message;

import java.util.Arrays;

public class MessageInfo {
	private int msgType;
	private String[] toUser;
	private String fromUser;
	private String content;
	private String timeStamp;
	private String userImg;
	private String userName;

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String[] getToUser() {
		return toUser;
	}

	public void setToUser(String[] toUser) {
		this.toUser = toUser;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "MessageInfo [msgType=" + msgType + ", toUser=" + Arrays.toString(toUser) + ", fromUser=" + fromUser
				+ ", content=" + content + "]";
	}

}
