package com.chan.fbtc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chan on 2017/9/7.
 */
public class Message {
    @SerializedName("msgtype")
    public final String msgType;

    public At at = new At();

    public Message(String msgType) {
        this.msgType = msgType;
    }
}
