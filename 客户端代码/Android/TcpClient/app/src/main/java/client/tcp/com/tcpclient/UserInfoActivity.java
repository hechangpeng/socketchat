package client.tcp.com.tcpclient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.concurrent.Executors;

import client.tcp.com.tcpclient.bean.MessageInfo;
import client.tcp.com.tcpclient.cache.Cache;
import client.tcp.com.tcpclient.db.DbManager;
import client.tcp.com.tcpclient.view.GlideRoundTransform;

/**
 * Created by HECP2 on 2019-7-16.
 */

public class UserInfoActivity extends Activity {
    private TextView title;
    private ImageView userImg;
    private TextView nameTv;
    private TextView aliveTv;
    private TextView lastAlive;
    private ImageView backImg;
    public static final String KEY_LAST_MSG_TIME = "last_msg_time";
    public static final String KEY_ALIVE_RATE = "alive_rate";

    private static class InternalHandler extends Handler {

    }

    public static final InternalHandler MHANDLER = new InternalHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        initViews();
        initDatas();
    }

    private void initViews() {
        title = findViewById(R.id.title);
        userImg = findViewById(R.id.user_img);
        nameTv = findViewById(R.id.name_tv);
        aliveTv = findViewById(R.id.alive_tv);
        lastAlive = findViewById(R.id.lastalive_tv);
        backImg = findViewById(R.id.back_img);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDatas() {
        final MessageInfo messageInfo = (MessageInfo) getIntent().getSerializableExtra("message_bean");
        title.setText(messageInfo.getUserName() + (Cache.getInstance().chatId.equals(messageInfo.getFromUser()) ? "(我)" : ""));
        nameTv.setText(messageInfo.getUserName());
        Glide.with(this).load(messageInfo.getUserImg()).centerCrop().bitmapTransform(new GlideRoundTransform(this, 100)).placeholder(R.drawable.def_img).into(userImg);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final HashMap<String, String> data = DbManager.getInstance(UserInfoActivity.this).getUsetChatData(messageInfo.getFromUser());
                MHANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        aliveTv.setText(data.get(KEY_ALIVE_RATE));
                        lastAlive.setText(data.get(KEY_LAST_MSG_TIME));
                    }
                });
            }
        });
    }
}
