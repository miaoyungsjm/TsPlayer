package com.excellence.ggz.libparsetsstream;

import com.excellence.ggz.libparsetsstream.bean.Packet;

import static java.lang.Integer.toHexString;

public class Test {
    private static final String INPUT_FILE1_PATH = "/Users/miaoyun/Documents/ts_file/000.ts";
    private static final String INPUT_FILE2_PATH = "/Users/miaoyun/Documents/ts_file/001.ts";

    public static void main(String[] args) {
        PacketManager packetManager = new PacketManager(INPUT_FILE2_PATH);
        int packetLength = packetManager.getPacketLength();
        int packetStartPosition = packetManager.getPacketStartPosition();

        packetManager.setOnFilterListener(new PacketManager.OnFilterListener() {
            @Override
            public void onFilter(Packet packet) {
                System.out.println("----------");
                System.out.println("[onResult] syncByte: 0x" + toHexString(packet.getSyncByte()));
                System.out.println("[onResult] transportErrorIndicator: 0x" + toHexString(packet.getTransportErrorIndicator()));
                System.out.println("[onResult] payloadUnitStartIndicator: 0x" + toHexString(packet.getPayloadUnitStartIndicator()));
                System.out.println("[onResult] transportPriority: 0x" + toHexString(packet.getTransportPriority()));
                System.out.println("[onResult] pid: 0x" + toHexString(packet.getPid()));
                System.out.println("[onResult] transportScramblingControl: 0x" + toHexString(packet.getTransportScramblingControl()));
                System.out.println("[onResult] adaptationFieldControl: 0x" + toHexString(packet.getAdaptationFieldControl()));
                System.out.println("[onResult] continuityCounter: 0x" + toHexString(packet.getContinuityCounter()));
                System.out.println("[onResult] playLoad:");
                byte[] playLoad = packet.getPlayLoad();
                for (int i = 0; i < playLoad.length; i++) {
                    System.out.print(" 0x" + toHexString(playLoad[i] & 0xFF));
                    if (i > 0 && i % 20 == 0) {
                        System.out.println("");
                    }
                }
                System.out.println("");
            }
        });
        packetManager.filterPacketByPid(0x0);
    }
}