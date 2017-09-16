package com.chan.fbtc.markdown;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chan on 2017/9/7.
 */
public class TableRow implements MarkdownElement {
    private int columnsCount;
    private List<String> mCells = new ArrayList<>();

    public TableRow(int columnsCount) {
        this.columnsCount = columnsCount;
    }

    public <T> TableRow addCell(T cell) {
        if (mCells.size() >= columnsCount) {
            throw new IllegalArgumentException("超出行数");
        }

        mCells.add(cell.toString());
        return this;
    }

    @Override
    public String toMarkdownTexture() {

        List<String> formats = new ArrayList<>();
        for (int i = 0; i < mCells.size(); ++i) {
            formats.add("%-20s");
        }

        String content = String.format(StringUtils.join(formats, "|"), mCells.toArray());
        return "|" + content + "|\n\n";
    }
}
