package com.excellence.ggz.libparsetsstream.Section.entity;

import org.apache.log4j.Logger;

import static java.lang.Integer.toHexString;

/**
 * @author ggz
 * @date 2021/3/22
 */
public class Section {

    public int tableId;
    public int sectionSyntaxIndicator;
    public int sectionLength;
    public byte[] sectionBuff;

    private int remainLength;

    public Section(int tableId, int sectionSyntaxIndicator, int sectionLength, byte[] sectionBuff) {
        this.tableId = tableId;
        this.sectionSyntaxIndicator = sectionSyntaxIndicator;
        this.sectionLength = sectionLength;
        this.sectionBuff = sectionBuff;
    }

    public int getTableId() {
        return tableId;
    }

    public int getSectionSyntaxIndicator() {
        return sectionSyntaxIndicator;
    }

    public int getSectionLength() {
        return sectionLength;
    }

    public byte[] getSectionBuff() {
        return sectionBuff;
    }

    public int getRemainLength() {
        return remainLength;
    }

    public void setRemainLength(int remainLength) {
        this.remainLength = remainLength;
    }

    public void toPrint() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n")
                .append("[Section] tableId: 0x").append(toHexString(tableId)).append("\n")
                .append("[Section] sectionSyntaxIndicator: 0x").append(toHexString(sectionSyntaxIndicator)).append("\n")
                .append("[Section] sectionLength: 0x").append(toHexString(sectionLength)).append("\n")
                .append("[Section] sectionBuff: \n");
        for (int i = 0; i < sectionBuff.length; i++) {
            builder.append("0x").append(toHexString(sectionBuff[i] & 0xFF)).append(", ");
            if (i > 0 && i % 20 == 0) {
                builder.append("\n");
            }
        }

        Logger logger = Logger.getLogger(Section.class);
        logger.debug(builder.toString());
    }
}
