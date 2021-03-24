package com.excellence.ggz.libparsetsstream.Packet;

import org.apache.log4j.Logger;

import static java.lang.Integer.toHexString;

/**
 * @author ggz
 * @date 2021/3/10
 */
public class Packet {
    public static final int PACKET_HEADER_LENGTH = 4;

    private int syncByte;
    private int transportErrorIndicator;
    private int payloadUnitStartIndicator;
    private int transportPriority;
    private int pid;
    private int transportScramblingControl;
    private int adaptationFieldControl;
    private int continuityCounter;
    private byte[] payLoad;
    private int packetLength;

    public static Packet newInstance(byte[] buff) {
        int syncByte = buff[0] & 0xFF;
        int transportErrorIndicator = (buff[1] >> 7) & 0x1;
        int payloadUnitStartIndicator = (buff[1] >> 6) & 0x1;
        int transportPriority = (buff[1] >> 5) & 0x1;
        int pid = (((buff[1] & 0x1F) << 8) | (buff[2] & 0xFF)) & 0x1FFF;
        int transportScramblingControl = (buff[3] >> 6) & 0x3;
        int adaptationFieldControl = (buff[3] >> 4) & 0x3;
        int continuityCounter = buff[3] & 0xF;

        int packetLength = buff.length;
        int playLoadLength = packetLength - PACKET_HEADER_LENGTH;
        byte[] playLoad = new byte[playLoadLength];
        System.arraycopy(buff, PACKET_HEADER_LENGTH, playLoad, 0, playLoadLength);

        return new Packet(syncByte, transportErrorIndicator, payloadUnitStartIndicator,
                transportPriority, pid, transportScramblingControl,
                adaptationFieldControl, continuityCounter, playLoad, packetLength);
    }

    public Packet(int syncByte, int transportErrorIndicator, int payloadUnitStartIndicator,
                  int transportPriority, int pid, int transportScramblingControl,
                  int adaptationFieldControl, int continuityCounter, byte[] payLoad,
                  int packetLength) {
        this.syncByte = syncByte;
        this.transportErrorIndicator = transportErrorIndicator;
        this.payloadUnitStartIndicator = payloadUnitStartIndicator;
        this.transportPriority = transportPriority;
        this.pid = pid;
        this.transportScramblingControl = transportScramblingControl;
        this.adaptationFieldControl = adaptationFieldControl;
        this.continuityCounter = continuityCounter;
        this.payLoad = payLoad;
        this.packetLength = packetLength;
    }

    public int getSyncByte() {
        return syncByte;
    }

    public int getTransportErrorIndicator() {
        return transportErrorIndicator;
    }

    public int getPayloadUnitStartIndicator() {
        return payloadUnitStartIndicator;
    }

    public int getTransportPriority() {
        return transportPriority;
    }

    public int getPid() {
        return pid;
    }

    public int getTransportScramblingControl() {
        return transportScramblingControl;
    }

    public int getAdaptationFieldControl() {
        return adaptationFieldControl;
    }

    public int getContinuityCounter() {
        return continuityCounter;
    }

    public byte[] getPayLoad() {
        return payLoad;
    }

    public int getPacketLength() {
        return packetLength;
    }

    public void toPrint() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n")
                .append("[Packet] syncByte: 0x").append(toHexString(syncByte)).append("\n")
                .append("[Packet] transportErrorIndicator: 0x").append(toHexString(transportErrorIndicator)).append("\n")
                .append("[Packet] payloadUnitStartIndicator: 0x").append(toHexString(payloadUnitStartIndicator)).append("\n")
                .append("[Packet] transportPriority: 0x").append(toHexString(transportPriority)).append("\n")
                .append("[Packet] pid: 0x").append(toHexString(pid)).append("\n")
                .append("[Packet] transportScramblingControl: 0x").append(toHexString(transportScramblingControl)).append("\n")
                .append("[Packet] adaptationFieldControl: 0x").append(toHexString(adaptationFieldControl)).append("\n")
                .append("[Packet] continuityCounter: 0x").append(toHexString(continuityCounter)).append("\n")
                .append("[Packet] payLoad: \n");
        for (int i = 0; i < payLoad.length; i++) {
            builder.append("0x").append(toHexString(payLoad[i] & 0xFF)).append(", ");
            if (i > 0 && i % 20 == 0) {
                builder.append("\n");
            }
        }

        Logger logger = Logger.getLogger(Packet.class);
        logger.debug(builder.toString());
    }
}
