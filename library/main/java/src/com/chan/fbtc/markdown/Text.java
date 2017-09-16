package com.chan.fbtc.markdown;

/**
 * Created by chan on 2017/9/7.
 */
public class Text implements MarkdownElement {
    private String mContent;

    public Text(String content) {
        mContent = content;
    }

    @Override
    public String toMarkdownTexture() {
        return mContent;
    }
}
