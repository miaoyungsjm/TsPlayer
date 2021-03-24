package com.excellence.ggz.libparsetsstream.Section.entity;

import static java.lang.Integer.toHexString;

/**
 * @author cjr
 */
public class Program {
    private int programNumber;
    private int networkId;
    private int programMapPid;

    public Program(int programNumber, int networkId, int programMapPid) {
        this.programNumber = programNumber;
        this.networkId = networkId;
        this.programMapPid = programMapPid;
    }

    public int getProgramNumber() {
        return programNumber;
    }

    public int getNetworkId() {
        return networkId;
    }

    public int getProgramMapPid() {
        return programMapPid;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("------\n")
                .append("[Program] programNumber: 0x").append(toHexString(programNumber)).append("\n")
                .append("[Program] networkId: 0x").append(toHexString(networkId)).append("\n")
                .append("[Program] programMapPid: 0x").append(toHexString(programMapPid)).append("\n");
        return builder.toString();
    }
}
