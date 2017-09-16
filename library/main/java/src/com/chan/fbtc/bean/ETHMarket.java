package com.chan.fbtc.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chan on 2017/9/10.
 */
public class ETHMarket {

    @SerializedName("tick")
    public Snapshot snapshot;

    public static class Snapshot {
        @SerializedName("asks")
        public Float[][] sells;

        @SerializedName("bids")
        public Float[][] buys;
    }
}
