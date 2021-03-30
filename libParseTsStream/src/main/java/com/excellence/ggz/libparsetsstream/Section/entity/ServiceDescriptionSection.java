package com.excellence.ggz.libparsetsstream.Section.entity;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.toHexString;

/**
 * @author ggz
 * @date 2021/3/31
 */
public class ServiceDescriptionSection extends Section {

    private int transportStreamId;
    private int versionNumber;
    private int currentNextIndicator;
    private int sectionNumber;
    private int lastSectionNumber;
    private int originalNetworkId;
    private List<Service> serviceList;
    private byte[] crc32;

    public ServiceDescriptionSection(int tableId, int sectionSyntaxIndicator, int sectionLength,
                                     byte[] sectionBuff, int transportStreamId, int versionNumber,
                                     int currentNextIndicator, int sectionNumber, int lastSectionNumber,
                                     int originalNetworkId, List<Service> serviceList, byte[] crc32) {
        super(tableId, sectionSyntaxIndicator, sectionLength, sectionBuff);
        this.transportStreamId = transportStreamId;
        this.versionNumber = versionNumber;
        this.currentNextIndicator = currentNextIndicator;
        this.sectionNumber = sectionNumber;
        this.lastSectionNumber = lastSectionNumber;
        this.originalNetworkId = originalNetworkId;
        this.serviceList = serviceList;
        this.crc32 = crc32;
    }

    public int getTransportStreamId() {
        return transportStreamId;
    }

    public void setTransportStreamId(int transportStreamId) {
        this.transportStreamId = transportStreamId;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getCurrentNextIndicator() {
        return currentNextIndicator;
    }

    public void setCurrentNextIndicator(int currentNextIndicator) {
        this.currentNextIndicator = currentNextIndicator;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public int getLastSectionNumber() {
        return lastSectionNumber;
    }

    public void setLastSectionNumber(int lastSectionNumber) {
        this.lastSectionNumber = lastSectionNumber;
    }

    public int getOriginalNetworkId() {
        return originalNetworkId;
    }

    public void setOriginalNetworkId(int originalNetworkId) {
        this.originalNetworkId = originalNetworkId;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public byte[] getCrc32() {
        return crc32;
    }

    public void setCrc32(byte[] crc32) {
        this.crc32 = crc32;
    }

    @Override
    public String toString() {
        return "\n" +
                "[SDT] tableId: 0x" + tableId +"\n" +
                "[SDT] sectionSyntaxIndicator: 0x" + sectionSyntaxIndicator +
                "[SDT] sectionLength: 0x" + sectionLength +
                "transportStreamId=" + transportStreamId +
                ", versionNumber=" + versionNumber +
                ", currentNextIndicator=" + currentNextIndicator +
                ", sectionNumber=" + sectionNumber +
                ", lastSectionNumber=" + lastSectionNumber +
                ", originalNetworkId=" + originalNetworkId +
                ", serviceList=" + serviceList +
                ", crc32=" + Arrays.toString(crc32) +
                ", sectionBuff=" + Arrays.toString(sectionBuff) +
                '}';
    }

    @Override
    public void toPrint() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n")
                .append("[SDT] tableId: 0x").append(toHexString(tableId)).append("\n")
                .append("[SDT] sectionSyntaxIndicator: 0x").append(toHexString(sectionSyntaxIndicator)).append("\n")
                .append("[SDT] sectionLength: 0x").append(toHexString(sectionLength)).append("\n")
                .append("[SDT] transportStreamId: 0x").append(toHexString(transportStreamId)).append("\n")
                .append("[SDT] versionNumber: 0x").append(toHexString(versionNumber)).append("\n")
                .append("[SDT] currentNextIndicator: 0x").append(toHexString(currentNextIndicator)).append("\n")
                .append("[SDT] sectionNumber: 0x").append(toHexString(sectionNumber)).append("\n")
                .append("[SDT] lastSectionNumber: 0x").append(toHexString(lastSectionNumber)).append("\n")
                .append("[SDT] originalNetworkId: 0x").append(toHexString(originalNetworkId)).append("\n")
                .append("[SDT] serviceList: \n");
        for (Service service : serviceList) {
            builder.append(service.toString());
        }

        Logger logger = Logger.getLogger(ProgramAssociationSection.class);
        logger.debug(builder.toString());
    }
}
