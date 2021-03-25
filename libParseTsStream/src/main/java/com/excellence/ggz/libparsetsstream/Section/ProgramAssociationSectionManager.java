package com.excellence.ggz.libparsetsstream.Section;

import com.excellence.ggz.libparsetsstream.Packet.Packet;
import com.excellence.ggz.libparsetsstream.Section.entity.Program;
import com.excellence.ggz.libparsetsstream.Section.entity.ProgramAssociationSection;
import com.excellence.ggz.libparsetsstream.Section.entity.Section;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author ggz
 * @date 2021/3/22
 */
public class ProgramAssociationSectionManager extends AbstractSectionManager implements Observer {
    public static final int PAT_PID = 0x0000;
    public static final int PAT_TABLE_ID = 0x00;
    private static final int SECTION_PROGRAM_LENGTH = 4;
    private static final int PROGRAM_START_POS = 5;
    private static final int CRC_32 = 4;

    private static volatile ProgramAssociationSectionManager sInstance = null;

    private Logger mLogger = Logger.getLogger(ProgramAssociationSectionManager.class);

    private ProgramAssociationSectionManager() {
    }

    public static ProgramAssociationSectionManager getInstance() {
        if (sInstance == null) {
            synchronized (ProgramAssociationSectionManager.class) {
                if (sInstance == null) {
                    sInstance = new ProgramAssociationSectionManager();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void parseSection(Section section) {
        mLogger.debug("\n[PAS] parse Section");

        int tableId = section.getTableId();
        int sectionSyntaxIndicator = section.getSectionSyntaxIndicator();
        int sectionLength = section.getSectionLength();
        byte[] buff = section.getSectionBuff();

        int transportStreamId = (buff[0] & 0xFF) << 8 | buff[1] & 0xFF;
        int versionNumber = (buff[2] >> 1) & 0x1F;
        int currentNextIndicator = buff[2] & 0x1;
        int sectionNumber = buff[3];
        int lastSectionNumber = buff[4];
        byte[] crc32 = new byte[CRC_32];
        System.arraycopy(buff, buff.length - CRC_32, crc32, 0, CRC_32);

        List<Program> programList = new ArrayList<>();
        int programSize = (sectionLength - PROGRAM_START_POS - CRC_32) / SECTION_PROGRAM_LENGTH;
        for (int i = 0; i < programSize; i++) {
            int startPos = PROGRAM_START_POS + SECTION_PROGRAM_LENGTH * i;
            int programNumber = (buff[startPos] & 0xFF) << 8 | (buff[startPos + 1] & 0xFF);
            int networkPid = -1;
            int programMapPid = -1;
            if (programNumber == 0) {
                networkPid = ((buff[startPos + 2] & 0x1F) << 8 | buff[startPos + 3] & 0xFF) & 0x1FFF;
            } else {
                programMapPid = ((buff[startPos + 2] & 0x1F) << 8 | buff[startPos + 3] & 0xFF) & 0x1FFF;
            }
            Program program = new Program(programNumber, networkPid, programMapPid);
            programList.add(program);
        }

        ProgramAssociationSection pas = new ProgramAssociationSection(
                tableId, sectionSyntaxIndicator, sectionLength, buff,
                transportStreamId, versionNumber, currentNextIndicator,
                sectionNumber, lastSectionNumber, programList, crc32);

        if (mParseListener != null) {
            mParseListener.onFinish(pas);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Packet packet = (Packet) arg;
        mLogger.debug("\n[PAS] get packet");
        if (packet.getPid() == PAT_PID) {
            mLogger.debug("\n[PAS] assembleSection");
            assembleSection(PAT_TABLE_ID, packet);
        }
    }
}
