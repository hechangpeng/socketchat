package client.tcp.com.tcpclient.cache;

/**
 * Created by HECP2 on 2019-7-11.
 */

public class Cache {
    private Cache() {

    }

    private static class Holder {
        public static final Cache INSTANCE = new Cache();
    }

    public static Cache getInstance() {
        return Holder.INSTANCE;
    }

    public String chatId;
    public String chatImg;
    public String chatName;
    public int activityNum;
}
