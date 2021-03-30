package com.excellence.ggz.libparsetsstream.Section;

import com.excellence.ggz.libparsetsstream.Packet.Packet;
import com.excellence.ggz.libparsetsstream.Section.entity.Section;
import com.excellence.ggz.libparsetsstream.Section.entity.Service;
import com.excellence.ggz.libparsetsstream.Section.entity.ServiceDescriptionSection;
import com.excellence.ggz.libparsetsstream.descriptor.Descriptor;
import com.excellence.ggz.libparsetsstream.descriptor.DescriptorManager;
import com.excellence.ggz.libparsetsstream.descriptor.ServiceDescriptor;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author ggz
 * @date 2021/3/22
 */
public class ServiceDescriptionSectionManager extends AbstractSectionManager implements Observer {
    public static final int SDT_PID = 0x0011;
    public static final int SDT_TABLE_ID = 0x42;
    private static final int SECTION_HEADER = 8;
    private static final int SERVICE_HEADER = 5;
    private static final int CRC_32 = 4;

    private static volatile ServiceDescriptionSectionManager sInstance = null;

    private Logger mLogger = Logger.getLogger(ServiceDescriptionSectionManager.class);

    private ServiceDescriptionSectionManager() {
    }

    public static ServiceDescriptionSectionManager getInstance() {
        if (sInstance == null) {
            synchronized (ServiceDescriptionSectionManager.class) {
                if (sInstance == null) {
                    sInstance = new ServiceDescriptionSectionManager();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void parseSection(Section section) {
        mLogger.debug("\n[SDS] parse Section");

        int tableId = section.getTableId();
        int sectionSyntaxIndicator = section.getSectionSyntaxIndicator();
        int sectionLength = section.getSectionLength();
        byte[] buff = section.getSectionBuff();

        int transportStreamId = ((buff[0] & 0xFF << 8) | buff[1] & 0xFF) & 0xFFFF;
        int versionNumber = (buff[2] >> 1) & 0x1F;
        int currentNextIndicator = buff[2] & 0x1;
        int sectionNumber = buff[3] & 0xFF;
        int lastSectionNumber = buff[4] & 0xFF;
        int originalNetworkId = ((buff[5] & 0xFF << 8) | buff[6] & 0xFF) & 0xFFFF;
        byte[] crc32 = new byte[CRC_32];
        System.arraycopy(buff, buff.length - CRC_32, crc32, 0, CRC_32);

        List<Service> serviceList = new ArrayList<>();
        int serviceLength = sectionLength - SECTION_HEADER - CRC_32;
        int i = 0;
        while (i < serviceLength) {
            int serviceId = ((buff[8 + i] & 0xFF << 8) | buff[9 + i] & 0xFF) & 0xFFFF;
            int eitScheduleFollowingFlag = (buff[10 + i] >> 1) & 0x1;
            int eitPresentFollowingFlag = (buff[10 + i]) & 0x1;
            int runningStatus = (buff[11 + i] >> 5) & 0x7;
            int freeCaMode = (buff[11 + i] >> 4) & 0x1;
            int descriptorLoopLength = ((buff[11 + i] & 0xF << 4) | buff[12 + i] & 0xFF) & 0xFFF;

            byte[] descriptorBuf = new byte[descriptorLoopLength];
            int descriptorStartPos = SECTION_HEADER + i + SERVICE_HEADER;
            System.arraycopy(buff, descriptorStartPos, descriptorBuf, 0, descriptorLoopLength);
            Descriptor descriptor = Descriptor.newInstance(descriptorBuf);
            DescriptorManager descriptorManager = DescriptorManager.getInstance();
            ServiceDescriptor serviceDescriptor =
                    (ServiceDescriptor) descriptorManager.parseDescriptor(descriptor);

            Service service = new Service(serviceId, eitScheduleFollowingFlag, eitPresentFollowingFlag,
                    runningStatus, freeCaMode, descriptorLoopLength, serviceDescriptor);
            serviceList.add(service);

            int oneService = SERVICE_HEADER + descriptorLoopLength;
            i += oneService;
        }

        ServiceDescriptionSection sds = new ServiceDescriptionSection(
                tableId, sectionSyntaxIndicator, sectionLength, buff,
                transportStreamId, versionNumber, currentNextIndicator,
                sectionNumber, lastSectionNumber, originalNetworkId,
                serviceList, crc32);

        if (mParseListener != null) {
            mParseListener.onFinish(sds);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Packet packet = (Packet) arg;
        Logger logger = Logger.getLogger(ServiceDescriptionSectionManager.class);
        logger.debug("\n[SDS] get packet");
        if (packet.getPid() == SDT_PID) {
            logger.debug("\n[SDS] assembleSection");
            assembleSection(SDT_TABLE_ID, packet);
        }
    }
}
