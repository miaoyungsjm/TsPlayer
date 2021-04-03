package com.excellence.ggz.libparsetsstream.Section.entity;

import com.excellence.ggz.libparsetsstream.descriptor.ServiceDescriptor;

import static java.lang.Integer.toHexString;

/**
 * @author ggz
 * @date 2021/3/30
 */
public class Service {
    private int serviceId;
    private int eitScheduleFlag;
    private int eitPresentFollowingFlag;
    private int runningStatus;
    private int freeCaMode;
    private int descriptorsLoopLength;
    private ServiceDescriptor serviceDescriptor;

    public Service(int serviceId, int eitScheduleFlag, int eitPresentFollowingFlag,
                   int runningStatus, int freeCaMode, int descriptorsLoopLength,
                   ServiceDescriptor serviceDescriptor) {
        this.serviceId = serviceId;
        this.eitScheduleFlag = eitScheduleFlag;
        this.eitPresentFollowingFlag = eitPresentFollowingFlag;
        this.runningStatus = runningStatus;
        this.freeCaMode = freeCaMode;
        this.descriptorsLoopLength = descriptorsLoopLength;
        this.serviceDescriptor = serviceDescriptor;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getEitScheduleFlag() {
        return eitScheduleFlag;
    }

    public void setEitScheduleFlag(int eitScheduleFlag) {
        this.eitScheduleFlag = eitScheduleFlag;
    }

    public int getEitPresentFollowingFlag() {
        return eitPresentFollowingFlag;
    }

    public void setEitPresentFollowingFlag(int eitPresentFollowingFlag) {
        this.eitPresentFollowingFlag = eitPresentFollowingFlag;
    }

    public int getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(int runningStatus) {
        this.runningStatus = runningStatus;
    }

    public int getFreeCaMode() {
        return freeCaMode;
    }

    public void setFreeCaMode(int freeCaMode) {
        this.freeCaMode = freeCaMode;
    }

    public int getDescriptorsLoopLength() {
        return descriptorsLoopLength;
    }

    public void setDescriptorsLoopLength(int descriptorsLoopLength) {
        this.descriptorsLoopLength = descriptorsLoopLength;
    }

    public ServiceDescriptor getServiceDescriptor() {
        return serviceDescriptor;
    }

    public void setServiceDescriptor(ServiceDescriptor serviceDescriptor) {
        this.serviceDescriptor = serviceDescriptor;
    }

    @Override
    public String toString() {
        return "------\n" +
                "[Service] serviceId: 0x" + toHexString(serviceId) + "\n" +
                "[Service] eitScheduleFlag: 0x" + toHexString(eitScheduleFlag) + "\n" +
                "[Service] eitPresentFollowingFlag: 0x" + toHexString(eitPresentFollowingFlag) + "\n" +
                "[Service] runningStatus: 0x" + toHexString(runningStatus) + "\n" +
                "[Service] freeCaMode: 0x" + toHexString(freeCaMode) + "\n" +
                "[Service] descriptorsLoopLength: 0x" + toHexString(descriptorsLoopLength) + "\n" +
                "[Service] serviceDescriptor: \n" +
                serviceDescriptor.toString();
    }
}
