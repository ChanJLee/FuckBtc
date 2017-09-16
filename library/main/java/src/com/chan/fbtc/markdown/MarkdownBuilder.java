package com.chan.fbtc.markdown;


import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chan on 2017/9/7.
 */
public class MarkdownBuilder {

    private List<MarkdownElement> mMarkdownElements = new ArrayList<>();

    public void addElement(MarkdownElement element) {
        mMarkdownElements.add(element);
    }

    public String toMarkdown() {

        List<String> textureMarkdown = new ArrayList<>();
        for (MarkdownElement element : mMarkdownElements) {
            textureMarkdown.add(element.toMarkdownTexture());
        }

        return StringUtils.join(textureMarkdown, "\n");
    }
}
