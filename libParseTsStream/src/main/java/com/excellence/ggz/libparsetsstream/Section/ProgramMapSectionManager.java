package com.excellence.ggz.libparsetsstream.Section;

import com.excellence.ggz.libparsetsstream.Packet.Packet;
import com.excellence.ggz.libparsetsstream.Section.entity.Component;
import com.excellence.ggz.libparsetsstream.Section.entity.ProgramMapSection;
import com.excellence.ggz.libparsetsstream.Section.entity.Section;
import com.excellence.ggz.libparsetsstream.descriptor.Descriptor;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Integer.toHexString;

/**
 * @author ggz
 * @date 2021/3/22
 */
public class ProgramMapSectionManager extends AbstractSectionManager implements Observer {
    public static final int PMT_TABLE_ID = 0x02;
    private static final int PMS_SECTION_HEADER = 9;
    private static final int CRC_32 = 4;

    private static volatile ProgramMapSectionManager sInstance = null;

    private Logger mLogger = Logger.getLogger(ProgramMapSectionManager.class);
    private List<Integer> mFilterPidList = new ArrayList<>();

    public static ProgramMapSectionManager getInstance() {
        if (sInstance == null) {
            synchronized (ProgramMapSectionManager.class) {
                if (sInstance == null) {
                    sInstance = new ProgramMapSectionManager();
                }
            }
        }
        return sInstance;
    }

    private ProgramMapSectionManager() {
    }

    @Override
    public void parseSection(Section section) {
        mLogger.debug("\n[PMS] parse Section");

        int pid = section.getPid();
        int tableId = section.getTableId();
        int sectionSyntaxIndicator = section.getSectionSyntaxIndicator();
        int sectionLength = section.getSectionLength();
        byte[] buff = section.getSectionBuff();

        int programNumber = (((buff[0] & 0xFF) << 8) | (buff[1] & 0xFF)) & 0xFFFF;
        int versionNumber = (buff[2] >> 1) & 0x1F;
        int currentNextIndicator = buff[2] & 0x1;
        int sectionNumber = buff[3] & 0xFF;
        int lastSectionNumber = buff[4] & 0xFF;
        int pcrPid = (((buff[5] & 0x1F) << 8) | (buff[6] & 0xFF)) & 0x1FFF;
        int programInfoLength = (((buff[7] & 0x3) << 8) | (buff[8] & 0xFF)) & 0x3FF;
        byte[] crc32 = new byte[CRC_32];
        System.arraycopy(buff, buff.length - CRC_32, crc32, 0, CRC_32);

        byte[] programInfoBuff = new byte[programInfoLength];
        System.arraycopy(buff, PMS_SECTION_HEADER, programInfoBuff, 0, programInfoLength);
        List<Descriptor> programInfoDescriptorList = Descriptor.newInstanceList(programInfoBuff);

        int componentLength = sectionLength - PMS_SECTION_HEADER - programInfoLength - CRC_32;
        byte[] componentBuff = new byte[componentLength];
        System.arraycopy(buff, PMS_SECTION_HEADER + programInfoLength,
                componentBuff, 0, componentLength);
        List<Component> componentList = Component.newInstanceList(componentBuff);

        ProgramMapSection pms = new ProgramMapSection(
                pid, tableId, sectionSyntaxIndicator, sectionLength,
                buff, programNumber, versionNumber, currentNextIndicator,
                sectionNumber, lastSectionNumber, pcrPid, programInfoLength,
                programInfoDescriptorList, componentList, crc32);

        if (mParseListener != null) {
            mParseListener.onFinish(pms);
        }
    }

    public void addFilterPid(int pid) {
        mFilterPidList.add(pid);
    }

    public void removeFilterPid(int pid) {
        // fix ConcurrentModificationException
        Iterator<Integer> it = mFilterPidList.iterator();
        while (it.hasNext()) {
            Integer integer = it.next();
            if (integer == pid) {
                it.remove();
            }
        }
    }

    public List<Integer> getFilterPidList() {
        return mFilterPidList;
    }

    @Override
    public void update(Observable o, Object arg) {
        Packet packet = (Packet) arg;
        Logger logger = Logger.getLogger(ProgramMapSectionManager.class);
        logger.debug("\n[PMS] get packet pid: 0x" + toHexString(packet.getPid()));

        for (int i = 0; i < mFilterPidList.size(); i++) {
            if (packet.getPid() == mFilterPidList.get(i)) {
                logger.debug("\n[PMS] assembleSection pid: 0x" + toHexString(packet.getPid()));
                assembleSection(PMT_TABLE_ID, packet);
            }
        }
    }
}
