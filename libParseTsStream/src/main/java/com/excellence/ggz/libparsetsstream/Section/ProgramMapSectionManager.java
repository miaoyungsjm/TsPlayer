package com.excellence.ggz.libparsetsstream.Section;

import com.excellence.ggz.libparsetsstream.Packet.Packet;
import com.excellence.ggz.libparsetsstream.Section.entity.Section;

import java.util.Observable;
import java.util.Observer;

/**
 * @author ggz
 * @date 2021/3/22
 */
public class ProgramMapSectionManager extends AbstractSectionManager implements Observer {
    public static final int PMT_TABLE_ID = 0x02;

    public static ProgramMapSectionManager getInstance(int filterPid) {
        return new ProgramMapSectionManager(filterPid);
    }

    private int mFilterPid;

    public ProgramMapSectionManager(int filterPid) {
        this.mFilterPid = filterPid;
    }

    @Override
    public void parseSection(Section section) {
        section.toPrint();
    }

    @Override
    public void update(Observable o, Object arg) {
        Packet packet = (Packet) arg;
        if (packet.getPid() == mFilterPid) {
            packet.toPrint();
            assembleSection(PMT_TABLE_ID, packet);
        }
    }
}
