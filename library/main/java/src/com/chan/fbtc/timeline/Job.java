package com.chan.fbtc.timeline;

import com.chan.fbtc.bean.BTCMarket;

/**
 * Created by chan on 2017/9/8.
 */
public interface Job {
    boolean interceptAction(long timestamp);

    void action();
}
