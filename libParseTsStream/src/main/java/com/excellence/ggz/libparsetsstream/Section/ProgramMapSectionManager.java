package com.excellence.ggz.libparsetsstream.Section;

import com.excellence.ggz.libparsetsstream.Packet.Packet;
import com.excellence.ggz.libparsetsstream.Section.entity.Section;

import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.Observer;

import static java.lang.Integer.toHexString;

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
    private Logger mLogger = Logger.getLogger(ProgramMapSectionManager.class);

    public ProgramMapSectionManager(int filterPid) {
        this.mFilterPid = filterPid;
    }

    @Override
    public void parseSection(Section section) {
        mLogger.debug("\n[PMS] parse Section");
        mLogger.debug(section.toString());
    }

    @Override
    public void update(Observable o, Object arg) {
        Packet packet = (Packet) arg;
        Logger logger = Logger.getLogger(ProgramMapSectionManager.class);
        logger.debug("\n[PMS] get packet");
        if (packet.getPid() == mFilterPid) {
            logger.debug("\n[PMS] assembleSection pid: 0x" + toHexString(packet.getPid()));
            assembleSection(PMT_TABLE_ID, packet);
        }
    }
}
