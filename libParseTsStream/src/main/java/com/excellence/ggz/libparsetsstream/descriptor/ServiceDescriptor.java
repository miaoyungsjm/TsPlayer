package com.excellence.ggz.libparsetsstream.descriptor;

/**
 * @author ggz
 * @date 2021/3/30
 */
public class ServiceDescriptor extends Descriptor {
    private int serviceType;
    private int serviceProviderNameLength;
    private byte[] serviceProviderName;
    private int serviceNameLength;
    private byte[] serviceName;

    public ServiceDescriptor(int descriptorTag, int descriptorLength, byte[] descriptorBuff,
                             int serviceType, int serviceProviderNameLength, byte[] serviceProviderName,
                             int serviceNameLength, byte[] serviceName) {
        super(descriptorTag, descriptorLength, descriptorBuff);
        this.serviceType = serviceType;
        this.serviceProviderNameLength = serviceProviderNameLength;
        this.serviceProviderName = serviceProviderName;
        this.serviceNameLength = serviceNameLength;
        this.serviceName = serviceName;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getServiceProviderNameLength() {
        return serviceProviderNameLength;
    }

    public void setServiceProviderNameLength(int serviceProviderNameLength) {
        this.serviceProviderNameLength = serviceProviderNameLength;
    }

    public byte[] getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(byte[] serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public int getServiceNameLength() {
        return serviceNameLength;
    }

    public void setServiceNameLength(int serviceNameLength) {
        this.serviceNameLength = serviceNameLength;
    }

    public byte[] getServiceName() {
        return serviceName;
    }

    public void setServiceName(byte[] serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "[ServiceDescriptorData] descriptorTag: 0x" + descriptorTag + "\n" +
                "[ServiceDescriptorData] descriptorLength: 0x" + descriptorLength + "\n" +
                "[ServiceDescriptorData] serviceType: 0x" + serviceType + "\n" +
                "[ServiceDescriptorData] serviceProviderNameLength: 0x" + serviceProviderNameLength + "\n" +
                "[ServiceDescriptorData] serviceProviderName: " + new String(serviceProviderName) + "\n" +
                "[ServiceDescriptorData] serviceNameLength: 0x" + serviceNameLength + "\n" +
                "[ServiceDescriptorData] serviceName: " + new String(serviceName) + "\n";
    }
}
