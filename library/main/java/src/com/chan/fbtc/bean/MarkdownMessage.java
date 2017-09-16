package com.chan.fbtc.bean;

/**
 * Created by chan on 2017/9/7.
 */
public class MarkdownMessage extends Message {
    public Markdown markdown = new Markdown();

    public MarkdownMessage() {
        super("markdown");
    }

    public static class Markdown {
        public String title = "即时报表";
        public String text;
    }
}
