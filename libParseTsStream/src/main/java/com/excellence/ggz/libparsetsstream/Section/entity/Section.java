package com.excellence.ggz.libparsetsstream.Section.entity;

import org.apache.log4j.Logger;

import static java.lang.Integer.toHexString;

/**
 * @author ggz
 * @date 2021/3/22
 */
public class Section {

    private int tableId;
    private int sectionSyntaxIndicator;
    private int zero;
    private int reserved;
    private int sectionLength;
    private byte[] sectionBuff;
    private int remainLength;

    public Section(int tableId, int sectionSyntaxIndicator, int zero, int reserved, int sectionLength, byte[] sectionBuff, int remainLength) {
        this.tableId = tableId;
        this.sectionSyntaxIndicator = sectionSyntaxIndicator;
        this.zero = zero;
        this.reserved = reserved;
        this.sectionLength = sectionLength;
        this.sectionBuff = sectionBuff;
        this.remainLength = remainLength;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getSectionSyntaxIndicator() {
        return sectionSyntaxIndicator;
    }

    public void setSectionSyntaxIndicator(int sectionSyntaxIndicator) {
        this.sectionSyntaxIndicator = sectionSyntaxIndicator;
    }

    public int getZero() {
        return zero;
    }

    public void setZero(int zero) {
        this.zero = zero;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public int getSectionLength() {
        return sectionLength;
    }

    public void setSectionLength(int sectionLength) {
        this.sectionLength = sectionLength;
    }

    public byte[] getSectionBuff() {
        return sectionBuff;
    }

    public void setSectionBuff(byte[] sectionBuff) {
        this.sectionBuff = sectionBuff;
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
                .append("[Section] zero: 0x").append(toHexString(zero)).append("\n")
                .append("[Section] reserved: 0x").append(toHexString(reserved)).append("\n")
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
