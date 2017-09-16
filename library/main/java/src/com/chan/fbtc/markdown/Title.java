package com.chan.fbtc.markdown;

/**
 * Created by chan on 2017/9/7.
 */
public class Title implements MarkdownElement {

    private Size mSize;
    private String content;

    public enum Size {
        SIZE_1,
        SIZE_2,
        SIZE_3
    }

    public Title(String content) {
        this(Size.SIZE_1, content);
    }

    public Title(Size size, String content) {
        mSize = size;
        this.content = content;
    }

    @Override
    public String toMarkdownTexture() {

        String header = "### ";
        if (mSize == Size.SIZE_2) {
            header = "## ";
        } else if (mSize == Size.SIZE_3) {
            header = "# ";
        }

        return header + content;
    }
}
