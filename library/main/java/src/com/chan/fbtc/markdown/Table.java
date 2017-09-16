package com.chan.fbtc.markdown;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chan on 2017/9/7.
 */
public class Table implements MarkdownElement {
    private String[] mColumnsLabel;
    private List<TableRow> mTableRows = new ArrayList<>();

    public Table(String... columnsLabels) {
        mColumnsLabel = columnsLabels;
    }

    public TableRow newRow() {
        TableRow tableRow = new TableRow(mColumnsLabel.length);
        mTableRows.add(tableRow);
        return tableRow;
    }

    @Override
    public String toMarkdownTexture() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("|");
        List<String> formats = new ArrayList<>();
        for (int i = 0; i < mColumnsLabel.length; ++i) {
            formats.add("%-20s");
        }
        stringBuilder.append(String.format(StringUtils.join(formats, "|"), mColumnsLabel));
        stringBuilder.append("|\n\n");

        for (TableRow tableRow : mTableRows) {
            stringBuilder.append(tableRow.toMarkdownTexture());
        }
        return stringBuilder.toString();
    }
}
