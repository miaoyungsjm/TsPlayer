package com.excellence.ggz.libparsetsstream;

import com.excellence.ggz.libparsetsstream.bean.Packet;

public class Test {
    private static final String INPUT_FILE1_PATH = "/Users/miaoyun/Documents/ts_file/000.ts";
    private static final String INPUT_FILE2_PATH = "c:/Users/GGZ/Desktop/sx/tools/001.ts";

    public static void main(String[] args) {
        PacketManager packetManager = new PacketManager(INPUT_FILE2_PATH);
        int packetLength = packetManager.getPacketLength();
        int packetStartPosition = packetManager.getPacketStartPosition();

        packetManager.setOnFilterListener(new PacketManager.OnFilterListener() {
            @Override
            public void onFilter(Packet packet) {
                packet.toPrint();
            }
        });
        packetManager.filterPacketByPid(0x0);
    }
}