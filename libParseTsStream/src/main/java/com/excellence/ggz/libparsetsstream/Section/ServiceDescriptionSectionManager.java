package com.excellence.ggz.libparsetsstream.Section;

import com.excellence.ggz.libparsetsstream.Packet.Packet;
import com.excellence.ggz.libparsetsstream.Section.entity.Section;

import java.util.Observable;
import java.util.Observer;

/**
 * @author ggz
 * @date 2021/3/22
 */
public class ServiceDescriptionSectionManager extends AbstractSectionManager implements Observer {
    public static final int SDT_PID = 0x0011;
    public static final int SDT_TABLE_ID = 0x42;

    private static volatile ServiceDescriptionSectionManager sInstance = null;

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
        section.toPrint();
    }

    @Override
    public void update(Observable o, Object arg) {
        Packet packet = (Packet) arg;
        if (packet.getPid() == SDT_PID) {
            packet.toPrint();
            assembleSection(SDT_TABLE_ID, packet);
        }
    }
}
