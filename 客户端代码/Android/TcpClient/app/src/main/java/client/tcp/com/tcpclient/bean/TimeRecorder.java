package client.tcp.com.tcpclient.bean;

/**
 * Created by HECP2 on 2019-7-15.
 */

public class TimeRecorder {
    private String timeStamp;
    private boolean isRecorded;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isRecorded() {
        return isRecorded;
    }

    public void setRecorded(boolean recorded) {
        isRecorded = recorded;
    }
}
