package com.excellence.ggz.libparsetsstream.Section.entity;

import static java.lang.Integer.toHexString;

/**
 * @author cjr
 */
public class ProgramAssociation {
    private int programNumber;
    private int networkId;
    private int programMapPid;

    public ProgramAssociation(int programNumber, int networkId, int programMapPid) {
        this.programNumber = programNumber;
        this.networkId = networkId;
        this.programMapPid = programMapPid;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("------\n")
                .append("[ProgramAssociation] programNumber: 0x").append(toHexString(programNumber)).append("\n")
                .append("[ProgramAssociation] networkId: 0x").append(toHexString(networkId)).append("\n")
                .append("[ProgramAssociation] programMapPid: 0x").append(toHexString(programMapPid)).append("\n");
        return builder.toString();
    }
}
