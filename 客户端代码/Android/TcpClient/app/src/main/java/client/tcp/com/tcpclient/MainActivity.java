package client.tcp.com.tcpclient;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import client.tcp.com.tcpclient.adapter.MessageAdapter;
import client.tcp.com.tcpclient.bean.MessageInfo;
import client.tcp.com.tcpclient.bean.TimeRecorder;
import client.tcp.com.tcpclient.cache.Cache;
import client.tcp.com.tcpclient.db.DbManager;
import client.tcp.com.tcpclient.socket.SocketSDK;
import client.tcp.com.tcpclient.util.SpUtil;
import client.tcp.com.tcpclient.util.Util;
import client.tcp.com.tcpclient.view.OnPushMsgListener;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class MainActivity extends Activity implements OnPushMsgListener {

    private Button mSendBtn;
    private EditText mMsgEt;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<MessageInfo> mDataList;
    private boolean needScrollBottom = true;
    private ConcurrentHashMap<String, TimeRecorder> mTimeRecorder;

    private static class InternalHandler extends Handler {

    }

    public static final InternalHandler MHANDLER = new InternalHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Cache.getInstance().chatId = SpUtil.getString(SpUtil.CHAT_ID, UUID.randomUUID().toString().replace("-", ""), this);
        Cache.getInstance().chatImg = SpUtil.getString(SpUtil.CHAT_IMG, Util.getRandomImg(), this);
        Cache.getInstance().chatName = SpUtil.getString(SpUtil.CHAT_NAME, Util.getRandomName(), this);
        SpUtil.putString(SpUtil.CHAT_ID, Cache.getInstance().chatId, this);
        SpUtil.putString(SpUtil.CHAT_IMG, Cache.getInstance().chatImg, this);
        SpUtil.putString(SpUtil.CHAT_NAME, Cache.getInstance().chatName, this);
        initReceiver();
        initViews();
        initSocket();
    }

    private void initViews() {
        mSendBtn = (Button) findViewById(R.id.send_btn);
        mMsgEt = (EditText) findViewById(R.id.etv_msg);
        recyclerView = (RecyclerView) findViewById(R.id.msg_list);
        mDataList = new ArrayList<>();
        mTimeRecorder = new ConcurrentHashMap<>();
        messageAdapter = new MessageAdapter(this, mDataList, mTimeRecorder);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mMsgEt.getText().toString();
                if (!TextUtils.isEmpty(msg)) {
                    SocketSDK.getInstance().sendMsg(mMsgEt.getText().toString());
                    mMsgEt.setText(null);
                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastPos = linearLayoutManager.findLastVisibleItemPosition();
                    needScrollBottom = (lastPos == (messageAdapter.getItemCount() - 1));
                }
            }
        });
        findViewById(R.id.right_dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setFromUser(Cache.getInstance().chatId);
                messageInfo.setUserImg(Cache.getInstance().chatImg);
                messageInfo.setUserName(Cache.getInstance().chatName);
                intent.putExtra("message_bean", messageInfo);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMsgPush(String msg) {
        MessageInfo messageInfo1 = null;
        try {
            System.out.println("info=" + msg);
            messageInfo1 = new Gson().fromJson(msg, MessageInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (messageInfo1 != null) {
            mDataList.add(messageInfo1);
            MHANDLER.post(new Runnable() {
                @Override
                public void run() {
                    messageAdapter.notifyItemInserted(mDataList.size() - 1);
                    if (needScrollBottom) {
                        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                    }
                }
            });
        }
    }

    private void initReceiver() {
        try {
            netWorkStateReceiver = new NetChangeBroadcast();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netWorkStateReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSocket() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<MessageInfo> msgHistory = DbManager.getInstance(MainActivity.this).getMsgList();
                if (msgHistory != null) {
                    mDataList.addAll(msgHistory);
                }
                initTimeRecorder();
                MHANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        messageAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                        SocketSDK.getInstance().initSocket(MainActivity.this);
                    }
                });
            }
        });
    }

    private void initTimeRecorder() {
        for (int i = 0; i < mDataList.size(); i++) {
            TimeRecorder timeRecorder = mTimeRecorder.get(i + "");
            if (timeRecorder == null) {
                timeRecorder = new TimeRecorder();
                timeRecorder.setTimeStamp(mDataList.get(i).getTimeStamp());
                mTimeRecorder.put(i + "", timeRecorder);
            }
            if (i == 0) {
                mTimeRecorder.get(i + "").setRecorded(true);
            } else {
                for (int j = (i - 1); j >= 0; j--) {
                    if (mTimeRecorder.get(i + "") == null || mTimeRecorder.get(j + "") == null) {
                        continue;
                    }
                    if (mTimeRecorder.get(j + "").isRecorded()) {
                        if ((Math.abs(Util.dateToStamp(mTimeRecorder.get(i + "").getTimeStamp()) - Util.dateToStamp(mTimeRecorder.get(j + "").getTimeStamp())) >= (5 * 60 * 1000))) {
                            mTimeRecorder.get(i + "").setRecorded(true);
                        }
                        break;
                    }
                }
            }
        }
    }

    private boolean isFirstEnter = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstEnter) {
            SocketSDK.getInstance().initSocket(MainActivity.this);
        }
        isFirstEnter = false;
    }

    private class NetChangeBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Util.isNetworkConnected(context)) {
                SocketSDK.getInstance().initSocket(MainActivity.this);
            }
        }
    }

    private NetChangeBroadcast netWorkStateReceiver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(netWorkStateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SocketSDK.getInstance().removePushListener(this);
    }
}
