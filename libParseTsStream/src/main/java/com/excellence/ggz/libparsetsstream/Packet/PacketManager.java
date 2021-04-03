package com.excellence.ggz.libparsetsstream.Packet;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;

import static com.excellence.ggz.libparsetsstream.Section.ProgramAssociationSectionManager.PAT_PID;
import static com.excellence.ggz.libparsetsstream.Section.ServiceDescriptionSectionManager.SDT_PID;

/**
 * @author ggz
 * @date 2021/3/8
 */
public class PacketManager extends Observable {
    private static final int PACKET_HEADER_SYNC_BYTE = 0x47;
    private static final int MATCH_TIMES = 10;
    private static final int BUFF_SIZE = 204 * 11;
    private static final int HASH_MAP_CAPACITY = 300;
    public static final int PACKET_LENGTH_188 = 188;
    public static final int PACKET_LENGTH_204 = 204;

    private Logger mLogger = Logger.getLogger(PacketManager.class);
    private String mInputFilePath;
    private int mPacketStartPosition = -1;
    private int mPacketLength = -1;

    public PacketManager(String inputFilePath) {
        this.mInputFilePath = inputFilePath;
    }

    private void matchPacketLength() {
        long startTime = System.currentTimeMillis();

        mPacketStartPosition = -1;
        mPacketLength = -1;

        try {
            File file = new File(mInputFilePath);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            int fileIndex = 0;
            byte[] buff = new byte[BUFF_SIZE];
            boolean isFinish = false;

            HashMap<Integer, MatchPosition> hashMap188 = new HashMap<>(HASH_MAP_CAPACITY);
            HashMap<Integer, MatchPosition> hashMap204 = new HashMap<>(HASH_MAP_CAPACITY);

            while (!isFinish) {
                // read one buff
                int err = bis.read(buff);
                if (err == -1) {
                    break;
                }
                // match packet length
                for (byte b : buff) {
                    if (b == PACKET_HEADER_SYNC_BYTE) {
                        // match 188
                        if (matchMethod(fileIndex, PACKET_LENGTH_188, hashMap188)) {
                            isFinish = true;
                            break;
                        }
                        // match 204
                        if (matchMethod(fileIndex, PACKET_LENGTH_204, hashMap204)) {
                            isFinish = true;
                            break;
                        }
                    }
                    fileIndex++;
                }
            }
            bis.close();
            mLogger.debug("read size: " + fileIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        mLogger.debug("elapsed time: " + elapsedTime);
    }

    private boolean matchMethod(int fileIndex, int matchPacketLen, HashMap<Integer, MatchPosition> hashMap) {
        // 当前所在区间值
        int curIntervalPosition = fileIndex / matchPacketLen;
        // 当前区间相对位置
        int curRelativePosition = fileIndex % matchPacketLen;
        // 查表
        MatchPosition matchPos = hashMap.get(curRelativePosition);
        if (matchPos != null) {
            int startPosition = matchPos.getStartPosition();
            int intervalPosition = matchPos.getIntervalPosition();
            int accumulator = matchPos.getAccumulator();

            if (accumulator == MATCH_TIMES) {
                mPacketStartPosition = startPosition;
                mPacketLength = matchPacketLen;
                StringBuilder builder = new StringBuilder();
                builder.append("\n")
                        .append("[matchMethod] ").append(matchPacketLen).append(" accumulator: ").append(accumulator).append("\n")
                        .append("[matchMethod] ").append(matchPacketLen).append(" packetStartPosition: ").append(mPacketStartPosition).append("\n")
                        .append("[matchMethod] ").append(matchPacketLen).append(" packetLength: ").append(mPacketLength).append("\n");
                mLogger.debug(builder.toString());
                return true;
            }
            // 判断所在区间是否相邻
            if (curIntervalPosition - intervalPosition == 1) {
                // 相邻，accumulator 进行累加
                matchPos.setIntervalPosition(curIntervalPosition);
                matchPos.setAccumulator(++accumulator);
            } else {
                // 不相邻，重新记录开始位置，accumulator 重新累加
                matchPos.setStartPosition(fileIndex);
                matchPos.setIntervalPosition(curIntervalPosition);
                matchPos.setAccumulator(1);
            }
        } else {
            matchPos = new MatchPosition(fileIndex, curIntervalPosition, 1);
        }
        hashMap.put(curRelativePosition, matchPos);
//       mLogger.debug("\n[matchMethod] " + matchPacketLen + " fileIndex: " + fileIndex +
//                ", relativePosition: " + curRelativePosition +
//                ", startPosition: " + matchPos.getStartPosition() +
//                ", intervalPosition: " + matchPos.getIntervalPosition() +
//                ", accumulator: " + matchPos.getAccumulator());
        return false;
    }

    public int getPacketLength() {
        if (mPacketLength == -1) {
            matchPacketLength();
        }
        return mPacketLength;
    }

    public int getPacketStartPosition() {
        if (mPacketStartPosition == -1) {
            matchPacketLength();
        }
        return mPacketStartPosition;
    }

    public void filterPacketByPid(int inputPid) {
        long startTime = System.currentTimeMillis();

        int packetLength = getPacketLength();
        int packetStartPosition = getPacketStartPosition();
        try {
            File file = new File(mInputFilePath);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            long pos = bis.skip(packetStartPosition);
            if (pos != packetStartPosition) {
                mLogger.debug("[filterPacketByPid] failed to skip packetStartPosition: " + packetStartPosition);
                return;
            }

            byte[] buff = new byte[packetLength * 50];
            int len;
            while ((len = bis.read(buff)) != -1) {
                for (int i = 0; i < len / packetLength; i++) {
                    byte[] onePacket = new byte[packetLength];
                    System.arraycopy(buff, packetLength * i, onePacket, 0, packetLength);
                    if (onePacket[0] == PACKET_HEADER_SYNC_BYTE) {
                        Packet packet = Packet.newInstance(onePacket);
                        if (packet.getTransportErrorIndicator() == 1) {
                            mLogger.debug("[filterPacketByPid] error: transport_error_indicator == 1");
                            continue;
                        }
                        if (packet.getPid() == PAT_PID | packet.getPid() == SDT_PID) {
                            // observable - observer
                            postNewPacket(packet);
                        }
                    } else {
                        mLogger.debug("[filterPacketByPid] error: stream is unstable" +
                                ", need to get new start position");
                    }
                }
            }
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        mLogger.debug("elapsed time: " + elapsedTime);
    }

    private void postNewPacket(Packet packet) {
        mLogger.debug("\n[PacketManager] post packet");
        mLogger.debug(packet.toString());
        setChanged();
        notifyObservers(packet);
    }

    public void addFilterPid(int pid) {
    }
}
