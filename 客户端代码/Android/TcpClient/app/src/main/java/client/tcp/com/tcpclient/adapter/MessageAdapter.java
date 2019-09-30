package client.tcp.com.tcpclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import client.tcp.com.tcpclient.R;
import client.tcp.com.tcpclient.UserInfoActivity;
import client.tcp.com.tcpclient.bean.MessageInfo;
import client.tcp.com.tcpclient.bean.TimeRecorder;
import client.tcp.com.tcpclient.cache.Cache;
import client.tcp.com.tcpclient.util.Util;
import client.tcp.com.tcpclient.view.GlideRoundTransform;

/**
 * Created by HECP2 on 2019-7-11.
 */

public class MessageAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<MessageInfo> mMessageList;
    private static final int LEFT_ITEM = 1;
    private static final int RIGHT_ITEM = 2;
    private static final int ENTER_ITEM = 3;
    private ConcurrentHashMap<String, TimeRecorder> mTimeRecorder;

    public MessageAdapter(Context context, List<MessageInfo> messageList, ConcurrentHashMap<String, TimeRecorder> timeRecorder) {
        this.mContext = context;
        this.mMessageList = messageList;
        this.mTimeRecorder = timeRecorder;
    }

    @Override
    public int getItemViewType(int position) {
        MessageInfo messageInfo = mMessageList.get(position);
        if (messageInfo == null) {
            return RIGHT_ITEM;
        }
        if (messageInfo.getMsgType() == 2) {
            return ENTER_ITEM;
        }
        if (messageInfo.getFromUser() != null && !messageInfo.getFromUser().equals(Cache.getInstance().chatId)) {
            return LEFT_ITEM;
        }
        return RIGHT_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LEFT_ITEM) {
            return new LeftItemHolder(LayoutInflater.from(mContext).inflate(R.layout.item_left_layout, parent, false));
        }
        if (viewType == ENTER_ITEM) {
            return new EnterItemHolder(LayoutInflater.from(mContext).inflate(R.layout.item_enter_layout, parent, false));
        }
        return new RightItemHolder(LayoutInflater.from(mContext).inflate(R.layout.item_right_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        TimeRecorder tr = mTimeRecorder.get(position + "");
        if (tr == null) {
            tr = new TimeRecorder();
            tr.setTimeStamp(mMessageList.get(position).getTimeStamp());
            mTimeRecorder.put(position + "", tr);
        }
        boolean isShowTime = needShowTime(position);
        if (holder instanceof LeftItemHolder) {
            LeftItemHolder leftItemHolder = (LeftItemHolder) holder;
            MessageInfo messageInfo = mMessageList.get(position);
            leftItemHolder.nameTv.setText(messageInfo.getUserName());
            leftItemHolder.msgTv.setText(messageInfo.getContent());
            leftItemHolder.timeTv.setText(isShowTime ? messageInfo.getTimeStamp() : null);
            leftItemHolder.timeTv.setVisibility(isShowTime ? View.VISIBLE : View.GONE);
            Glide.with(mContext).load(messageInfo.getUserImg()).centerCrop().bitmapTransform(new GlideRoundTransform(mContext)).placeholder(R.drawable.def_img).into(leftItemHolder.userImg);
            return;
        }
        if (holder instanceof RightItemHolder) {
            RightItemHolder rightItemHolder = (RightItemHolder) holder;
            MessageInfo messageInfo = mMessageList.get(position);
            rightItemHolder.nameTv.setText(messageInfo.getUserName());
            rightItemHolder.msgTv.setText(messageInfo.getContent());
            rightItemHolder.timeTv.setText(isShowTime ? messageInfo.getTimeStamp() : null);
            rightItemHolder.timeTv.setVisibility(isShowTime ? View.VISIBLE : View.GONE);
            Glide.with(mContext).load(messageInfo.getUserImg()).centerCrop().bitmapTransform(new GlideRoundTransform(mContext)).placeholder(R.drawable.def_img).into(rightItemHolder.userImg);
            return;
        }
        if (holder instanceof EnterItemHolder) {
            EnterItemHolder enterItemHolder = (EnterItemHolder) holder;
            enterItemHolder.timeTv.setText(isShowTime ? mMessageList.get(position).getTimeStamp() : null);
            enterItemHolder.timeTv.setVisibility(isShowTime ? View.VISIBLE : View.GONE);
            enterItemHolder.enterTv.setText(mMessageList.get(position).getUserName() + " 加入了聊天");
            return;
        }
    }

    private boolean needShowTime(int currentPosition) {
        boolean flag = false;
        if (currentPosition == 0) {
            mTimeRecorder.get(currentPosition + "").setRecorded(true);
            return true;
        }
        for (int i = (currentPosition - 1); i >= 0; i--) {
            if (mTimeRecorder.get(currentPosition + "") == null || mTimeRecorder.get(i + "") == null) {
                continue;
            }
            if (mTimeRecorder.get(i + "").isRecorded()) {
                if ((Math.abs(Util.dateToStamp(mTimeRecorder.get(currentPosition + "").getTimeStamp()) - Util.dateToStamp(mTimeRecorder.get(i + "").getTimeStamp())) >= (5 * 60 * 1000))) {
                    flag = true;
                    mTimeRecorder.get(currentPosition + "").setRecorded(true);
                }
                break;
            }
        }
        return flag;
    }

    @Override
    public int getItemCount() {
        return mMessageList == null ? 0 : mMessageList.size();
    }

    private class LeftItemHolder extends RecyclerView.ViewHolder {
        ImageView userImg;
        TextView msgTv;
        TextView timeTv;
        TextView nameTv;

        public LeftItemHolder(View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.user_img);
            msgTv = itemView.findViewById(R.id.msg_tv);
            timeTv = itemView.findViewById(R.id.time_tv);
            nameTv = itemView.findViewById(R.id.name_tv);
            userImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtra("message_bean", mMessageList.get(getAdapterPosition()));
                    mContext.startActivity(intent);
                }
            });
            nameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtra("message_bean", mMessageList.get(getAdapterPosition()));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    private class RightItemHolder extends RecyclerView.ViewHolder {
        ImageView userImg;
        TextView msgTv;
        TextView timeTv;
        TextView nameTv;

        public RightItemHolder(View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.user_img);
            msgTv = itemView.findViewById(R.id.msg_tv);
            timeTv = itemView.findViewById(R.id.time_tv);
            nameTv = itemView.findViewById(R.id.name_tv);
            userImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtra("message_bean", mMessageList.get(getAdapterPosition()));
                    mContext.startActivity(intent);
                }
            });
            nameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtra("message_bean", mMessageList.get(getAdapterPosition()));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    private class EnterItemHolder extends RecyclerView.ViewHolder {
        TextView enterTv;
        TextView timeTv;

        public EnterItemHolder(View itemView) {
            super(itemView);
            enterTv = itemView.findViewById(R.id.enter_msg);
            timeTv = itemView.findViewById(R.id.time_tv);
        }
    }
}
