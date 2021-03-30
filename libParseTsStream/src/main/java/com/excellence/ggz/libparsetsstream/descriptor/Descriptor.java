package com.excellence.ggz.libparsetsstream.descriptor;

import static java.lang.Integer.toHexString;

/**
 * @author ggz
 * @date 2021/3/30
 */
public class Descriptor {
    public int descriptorTag;
    public int descriptorLength;
    public byte[] descriptorBuff;

    public static Descriptor newInstance(byte[] buff) {
        int descriptorTag = buff[0] & 0xFF;
        int descriptorLength = (buff[1] >> 7) & 0x1;
        byte[] descriptorBuff = new byte[descriptorLength];
        return new Descriptor(descriptorTag, descriptorLength, descriptorBuff);
    }

    public Descriptor(int descriptorTag, int descriptorLength, byte[] descriptorBuff) {
        this.descriptorTag = descriptorTag;
        this.descriptorLength = descriptorLength;
        this.descriptorBuff = descriptorBuff;
    }

    public int getDescriptorTag() {
        return descriptorTag;
    }

    public void setDescriptorTag(int descriptorTag) {
        this.descriptorTag = descriptorTag;
    }

    public int getDescriptorLength() {
        return descriptorLength;
    }

    public void setDescriptorLength(int descriptorLength) {
        this.descriptorLength = descriptorLength;
    }

    public byte[] getDescriptorBuff() {
        return descriptorBuff;
    }

    public void setDescriptorBuff(byte[] descriptorBuff) {
        this.descriptorBuff = descriptorBuff;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n")
                .append("[Descriptor] descriptorTag :0x").append(toHexString(descriptorTag)).append("\n")
                .append("[Descriptor] descriptorLength :0x").append(toHexString(descriptorLength)).append("\n")
                .append("[Descriptor] descriptorData : ");
        for (int i = 0; i < descriptorBuff.length; i++) {
            builder.append("0x").append(toHexString(descriptorBuff[i] & 0xFF)).append(", ");
            if (i > 0 && i % 20 == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
