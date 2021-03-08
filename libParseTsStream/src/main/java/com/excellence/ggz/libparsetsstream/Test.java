package com.excellence.ggz.libparsetsstream;

public class Test {
    private static final String INPUT_FILE1_PATH = "c:/Users/GGZ/Desktop/sx/tools/000.ts";
    private static final String INPUT_FILE2_PATH = "c:/Users/GGZ/Desktop/sx/tools/001.ts";

    public static void main(String[] args) {
        PacketManager packetManager1 = new PacketManager(INPUT_FILE1_PATH);
        int packetLength = packetManager1.getPacketLength();
        int packetStartPosition = packetManager1.getPacketStartPosition();
        System.out.println("[result] packetLength = " + packetLength +
                ", packetStartPosition = " + packetStartPosition);
    }
}