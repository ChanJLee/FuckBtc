package com.chan.fbtc.timeline;

import com.chan.fbtc.api.HuoBiApiService;
import com.chan.fbtc.bean.BTCMarket;
import rx.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by chan on 2017/9/8.
 */
public class TimeLineBus extends TimerTask {

    private final List<Job> mJobs = new ArrayList<>();
    private boolean mLock = false;

    @Override
    public void run() {
        if (mLock) {
            return;
        }

        mLock = true;
        synchronized (mJobs) {
            long timestamp = System.currentTimeMillis();
            for (Job job : mJobs) {
                if (job.interceptAction(timestamp)) {
                    job.action();
                }
            }
        }
        mLock = false;
    }

    public void addJob(Job job) {
        synchronized (mJobs) {
            mJobs.add(job);
        }
    }
}
