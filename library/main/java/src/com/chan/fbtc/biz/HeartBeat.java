package com.chan.fbtc.biz;

import com.chan.fbtc.api.DingDingService;
import com.chan.fbtc.bean.TextMessage;
import com.chan.fbtc.log.Logger;
import com.chan.fbtc.preference.Preference;
import com.chan.fbtc.timeline.Job;
import com.google.gson.JsonElement;
import rx.Subscriber;

/**
 * Created by chan on 2017/9/8.
 */
public class HeartBeat implements Job {
    private long mLastTimestamp = 0;
    private final int mMaxInterval;

    public HeartBeat() {
        mMaxInterval = Preference.getInstance().getInt("HEART_BEAT_INTERVAL", 300000);
    }

    @Override
    public boolean interceptAction(long timestamp) {
        if (timestamp - mLastTimestamp >= mMaxInterval) {
            mLastTimestamp = timestamp;
            return true;
        }

        return false;
    }

    @Override
    public void action() {
        TextMessage message = new TextMessage();
        message.text.content = "我是监控程序心跳包";
        message.at.isAtAll = false;

        DingDingService.getInstance().sendMessage(message)
                .subscribe(new Subscriber<JsonElement>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.log("HeartBeat", "error " + e.getMessage());
                    }

                    @Override
                    public void onNext(JsonElement jsonElement) {
                        Logger.log("HeartBeat", "response " + jsonElement);
                    }
                });
    }
}
