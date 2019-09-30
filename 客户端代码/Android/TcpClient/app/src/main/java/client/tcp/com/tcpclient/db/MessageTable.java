package client.tcp.com.tcpclient.db;

/**
 * Created by HECP2 on 2019-7-15.
 */

public interface MessageTable {
    String TABLE_NAME = "message_table";
    String _ID = "auto_id";
    String COLUMN_MSG_TYPE = "msg_type";
    String COLUMN_TOUSER = "to_user";
    String COLUMN_FROMUSER = "from_user";
    String COLUMN_CONTENT = "c_content";
    String COLUMN_TIMESTAMP = "time_stamp";
    String COLUMN_USERIMG = "user_img";
    String COLUMN_USERNAME = "user_name";
}
