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
        return "------\n" +
                "[Program] programNumber: 0x" + toHexString(programNumber) + "\n" +
                "[Program] networkId: 0x" + toHexString(networkId) + "\n" +
                "[Program] programMapPid: 0x" + toHexString(programMapPid) + "\n";
    }
}
