package com.excellence.ggz.libparsetsstream.Section.entity;

import org.apache.log4j.Logger;

import java.util.List;

import static java.lang.Integer.toHexString;

public class ProgramAssociationSection extends Section {

    private int transportStreamId;
    private int versionNumber;
    private int currentNextIndicator;
    private int sectionNumber;
    private int lastSectionNumber;
    private List<Program> programList;
    private byte[] crc32;

    public ProgramAssociationSection(int tableId, int sectionSyntaxIndicator, int sectionLength,
                                     byte[] sectionBuff, int transportStreamId, int versionNumber,
                                     int currentNextIndicator, int sectionNumber, int lastSectionNumber,
                                     List<Program> programList, byte[] crc32) {
        super(tableId, sectionSyntaxIndicator, sectionLength, sectionBuff);
        this.transportStreamId = transportStreamId;
        this.versionNumber = versionNumber;
        this.currentNextIndicator = currentNextIndicator;
        this.sectionNumber = sectionNumber;
        this.lastSectionNumber = lastSectionNumber;
        this.programList = programList;
        this.crc32 = crc32;
    }

    public int getTransportStreamId() {
        return transportStreamId;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public int getCurrentNextIndicator() {
        return currentNextIndicator;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public int getLastSectionNumber() {
        return lastSectionNumber;
    }

    public List<Program> getProgramList() {
        return programList;
    }

    public byte[] getCrc32() {
        return crc32;
    }

    public void toPrint() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n")
                .append("[PAT] tableId: 0x").append(toHexString(tableId)).append("\n")
                .append("[PAT] sectionSyntaxIndicator: 0x").append(toHexString(sectionSyntaxIndicator)).append("\n")
                .append("[PAT] sectionLength: 0x").append(toHexString(sectionLength)).append("\n")
                .append("[PAT] transportStreamId: 0x").append(toHexString(transportStreamId)).append("\n")
                .append("[PAT] versionNumber: 0x").append(toHexString(versionNumber)).append("\n")
                .append("[PAT] currentNextIndicator: 0x").append(toHexString(currentNextIndicator)).append("\n")
                .append("[PAT] sectionNumber: 0x").append(toHexString(sectionNumber)).append("\n")
                .append("[PAT] lastSectionNumber: 0x").append(toHexString(lastSectionNumber)).append("\n")
                .append("[PAT] programList: \n");
        for (Program program : programList) {
            builder.append(program.toString());
        }

        Logger logger = Logger.getLogger(ProgramAssociationSection.class);
        logger.debug(builder.toString());
    }
}
