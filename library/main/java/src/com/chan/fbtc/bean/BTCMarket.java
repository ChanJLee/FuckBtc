package com.chan.fbtc.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chan on 2017/9/8.
 */
public class BTCMarket {
    @SerializedName("p_last")
    public float lastPrice;

    @SerializedName("p_low")
    public float lowestPrice;

    @SerializedName("p_high")
    public float highestPrice;

    public List<BTCMarket.TransactionRecord> sells;

    public List<BTCMarket.TransactionRecord> buys;

    /**
     * Created by chan on 2017/9/8.
     */
    public static class TransactionRecord {
        public float price;
        public float amount;
        public int level;
    }
}
