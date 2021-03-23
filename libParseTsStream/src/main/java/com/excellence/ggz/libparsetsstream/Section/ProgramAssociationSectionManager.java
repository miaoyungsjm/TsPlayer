package com.excellence.ggz.libparsetsstream.Section;

import com.excellence.ggz.libparsetsstream.Packet.Packet;
import com.excellence.ggz.libparsetsstream.Section.entity.Section;

import java.util.Observable;
import java.util.Observer;

/**
 * @author ggz
 * @date 2021/3/22
 */
public class ProgramAssociationSectionManager extends AbstractSectionManager implements Observer {
    public static final int PAT_PID = 0x0000;
    public static final int PAT_TABLE_ID = 0x00;

    private static volatile ProgramAssociationSectionManager sInstance = null;

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
        section.toPrint();
    }

    @Override
    public void update(Observable o, Object arg) {
        Packet packet = (Packet) arg;
        if (packet.getPid() == PAT_PID) {
            System.out.println("----------");
            System.out.println("[update] PAT pid: " + PAT_PID);
            packet.toPrint();
            assembleSection(PAT_TABLE_ID, packet);
        }
    }
}
