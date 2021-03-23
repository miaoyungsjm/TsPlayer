package com.excellence.ggz.libparsetsstream;

import com.excellence.ggz.libparsetsstream.Packet.PacketManager;
import com.excellence.ggz.libparsetsstream.Section.ProgramAssociationSectionManager;
import com.excellence.ggz.libparsetsstream.Section.ServiceDescriptionSectionManager;

import static com.excellence.ggz.libparsetsstream.Section.ServiceDescriptionSectionManager.SDT_PID;

public class Test {
    private static final String INPUT_FILE1_PATH = "/Users/miaoyun/Documents/ts_file/000.ts";
    private static final String INPUT_FILE2_PATH = "f:/bak/sx/tools/001.ts";

    public static void main(String[] args) {
        String inputFile = INPUT_FILE2_PATH;

        PacketManager packetManager = new PacketManager(inputFile);
        int packetLength = packetManager.getPacketLength();
        int packetStartPosition = packetManager.getPacketStartPosition();

        // observable - observer
        ProgramAssociationSectionManager pasManager = ProgramAssociationSectionManager.getInstance();
        ServiceDescriptionSectionManager sdsManager = ServiceDescriptionSectionManager.getInstance();
        packetManager.addObserver(pasManager);
        packetManager.addObserver(sdsManager);

        // start filter
        packetManager.filterPacketByPid(SDT_PID);
    }
}