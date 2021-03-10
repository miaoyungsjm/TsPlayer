package com.excellence.ggz.libparsetsstream;

import com.excellence.ggz.libparsetsstream.bean.Packet;

public class Test {
    private static final String INPUT_FILE1_PATH = "c:/Users/GGZ/Desktop/sx/tools/000.ts";
    private static final String INPUT_FILE2_PATH = "c:/Users/GGZ/Desktop/sx/tools/001.ts";

    public static void main(String[] args) {
        PacketManager packetManager = new PacketManager(INPUT_FILE2_PATH);
        int packetLength = packetManager.getPacketLength();
        int packetStartPosition = packetManager.getPacketStartPosition();

        packetManager.setOnFilterListener(new PacketManager.OnFilterListener() {
            @Override
            public void onResult(Packet packet) {
                System.out.println("----------");
                System.out.println("[onResult] syncByte: " + packet.getSyncByte());
                System.out.println("[onResult] transportErrorIndicator: " + packet.getTransportErrorIndicator());
                System.out.println("[onResult] payloadUnitStartIndicator: " + packet.getPayloadUnitStartIndicator());
                System.out.println("[onResult] transportPriority: " + packet.getTransportPriority());
                System.out.println("[onResult] pid: " + packet.getPid());
                System.out.println("[onResult] transportScramblingControl: " + packet.getTransportScramblingControl());
                System.out.println("[onResult] adaptationFieldControl: " + packet.getAdaptationFieldControl());
                System.out.println("[onResult] continuityCounter: " + packet.getContinuityCounter());
            }
        });
        packetManager.filterPacketByPid(0x0);
    }
}