package com.chan.fbtc.bean;

/**
 * Created by chan on 2017/9/7.
 */
public class TextMessage extends Message {
    public Content text = new Content();

    public TextMessage() {
        super("text");
    }

    public static class Content {
        public String content;
    }
}
