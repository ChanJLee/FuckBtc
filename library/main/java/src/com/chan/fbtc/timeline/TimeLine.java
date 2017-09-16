package com.chan.fbtc.timeline;

import java.util.Timer;

/**
 * Created by chan on 2017/9/8.
 */
public class TimeLine {
    private Timer mTimer;
    private TimeLineBus mTimeLineBus = new TimeLineBus();

    public void start() {
        mTimer = new Timer("fuck btc");
        mTimer.schedule(mTimeLineBus, 0,1000);
    }

    public void stop() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = null;
    }

    public void addJob(Job job) {
        mTimeLineBus.addJob(job);
    }
}
