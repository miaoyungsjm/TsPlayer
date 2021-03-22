package com.excellence.ggz.libparsetsstream;

import com.excellence.ggz.libparsetsstream.Packet.Packet;
import com.excellence.ggz.libparsetsstream.Packet.PacketManager;
import com.excellence.ggz.libparsetsstream.Section.AbstractSectionManager;
import com.excellence.ggz.libparsetsstream.Section.ProgramAssociationSectionManager;
import com.excellence.ggz.libparsetsstream.Section.ServiceDescriptionSectionManager;

public class Test {
    private static final String INPUT_FILE1_PATH = "/Users/miaoyun/Documents/ts_file/000.ts";
    private static final String INPUT_FILE2_PATH = "f:/bak/sx/tools/001.ts";
    private static final int PAT_PID = 0x0000;
    private static final int SDT_PID = 0x0011;
    private static final int EIT_PID = 0x0012;
    private static final int PAT_TABLE_ID = 0x00;
    private static final int PMT_TABLE_ID = 0x02;
    private static final int SDT_TABLE_ID = 0x42;
    private static final int EIT_TABLE_ID = 0x4e;

    public static void main(String[] args) {
        String inputFile = INPUT_FILE2_PATH;
        int inputPid = SDT_PID;
        final int inputTableId = SDT_TABLE_ID;

        PacketManager packetManager = new PacketManager(inputFile);
        int packetLength = packetManager.getPacketLength();
        int packetStartPosition = packetManager.getPacketStartPosition();

        packetManager.setOnFilterPacketListener(new PacketManager.OnFilterPacketListener() {
            @Override
            public void onFilter(Packet packet) {
//                packet.toPrint();
                AbstractSectionManager sectionManager = null;
                switch (inputTableId) {
                    case PAT_TABLE_ID:
                        sectionManager = ProgramAssociationSectionManager.getInstance();
                        break;
                    case SDT_TABLE_ID:
                        sectionManager = ServiceDescriptionSectionManager.getInstance();
                    default:
                        break;
                }
                if (sectionManager != null) {
                    sectionManager.assembleSection(inputTableId, packet);
                }
            }
        });
        packetManager.filterPacketByPid(inputPid);
    }
}