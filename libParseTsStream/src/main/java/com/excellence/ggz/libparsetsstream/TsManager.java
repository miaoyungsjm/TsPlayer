package com.excellence.ggz.libparsetsstream;

import com.excellence.ggz.libparsetsstream.Packet.PacketManager;
import com.excellence.ggz.libparsetsstream.Section.AbstractSectionManager;
import com.excellence.ggz.libparsetsstream.Section.ProgramAssociationSectionManager;
import com.excellence.ggz.libparsetsstream.Section.ProgramMapSectionManager;
import com.excellence.ggz.libparsetsstream.Section.ServiceDescriptionSectionManager;
import com.excellence.ggz.libparsetsstream.Section.entity.Program;
import com.excellence.ggz.libparsetsstream.Section.entity.ProgramAssociationSection;
import com.excellence.ggz.libparsetsstream.Section.entity.ProgramMapSection;
import com.excellence.ggz.libparsetsstream.Section.entity.Section;
import com.excellence.ggz.libparsetsstream.Section.entity.Service;
import com.excellence.ggz.libparsetsstream.Section.entity.ServiceDescriptionSection;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.util.ArrayList;
import java.util.List;

import static com.excellence.ggz.libparsetsstream.Section.ProgramAssociationSectionManager.PAT_PID;
import static com.excellence.ggz.libparsetsstream.Section.ServiceDescriptionSectionManager.SDT_PID;
import static java.lang.Integer.toHexString;

/**
 * @author ggz
 * @date 2021/3/30
 */
public class TsManager {

    private static volatile TsManager sInstance = null;

    private int mPacketLength;
    private int mPacketStartPosition;
    private ProgramAssociationSection mPat;
    private ServiceDescriptionSection mSdt;
    private List<ProgramMapSection> mPmtList = new ArrayList<>();
    private List<Program> mProgramList = new ArrayList<>();

    private final Logger mLogger = Logger.getRootLogger();

    private TsManager() {
        mLogger.addAppender(new ConsoleAppender(new PatternLayout("%r [%t] %p %l %m%n")));
//        root.addAppender(new FileAppender(new SimpleLayout(), "ts.log"));
        mLogger.setLevel(Level.DEBUG);
    }

    public static TsManager getInstance() {
        if (sInstance == null) {
            synchronized (TsManager.class) {
                if (sInstance == null) {
                    sInstance = new TsManager();
                }
            }
        }
        return sInstance;
    }

    public void parseTsFile(String filePath) {
        final PacketManager packetManager = new PacketManager(filePath);
        mPacketLength = packetManager.getPacketLength();
        mPacketStartPosition = packetManager.getPacketStartPosition();

        // observable - observer
        final ProgramMapSectionManager pmsManager = ProgramMapSectionManager.getInstance();
        pmsManager.setOnParseListener(new AbstractSectionManager.OnParseListener() {
            @Override
            public void onFinish(Section section) {
                ProgramMapSection programMapSection = (ProgramMapSection) section;
                mLogger.debug(programMapSection.toString());
                mPmtList.add(programMapSection);

                int pmtPid = programMapSection.getPid();
                mLogger.debug("\n[PMS] stop filter pid: 0x" + toHexString(pmtPid));
                packetManager.removeFilterPid(pmtPid);
                pmsManager.removeFilterPid(pmtPid);
                if (pmsManager.getFilterPidList().size() == 0) {
                    packetManager.deleteObserver(pmsManager);
                }
            }
        });

        final ProgramAssociationSectionManager pasManager = ProgramAssociationSectionManager.getInstance();
        pasManager.setOnParseListener(new AbstractSectionManager.OnParseListener() {
            @Override
            public void onFinish(Section section) {
                mPat = (ProgramAssociationSection) section;
                mLogger.debug(mPat.toString());
                mLogger.debug("\n[PAS] stop filter");
                packetManager.removeFilterPid(PAT_PID);
                packetManager.deleteObserver(pasManager);

                mPmtList.clear();
                List<Program> programList = mPat.getProgramList();
                for (Program program : programList) {
                    int programNumber = program.getProgramNumber();
                    int pmtPid = program.getProgramMapPid();
                    if (programNumber > 0) {
                        packetManager.addFilterPid(pmtPid);
                        pmsManager.addFilterPid(pmtPid);
                    }
                }
            }
        });

        final ServiceDescriptionSectionManager sdsManager = ServiceDescriptionSectionManager.getInstance();
        sdsManager.setOnParseListener(new AbstractSectionManager.OnParseListener() {
            @Override
            public void onFinish(Section section) {
                mSdt = (ServiceDescriptionSection) section;
                mLogger.debug(mSdt.toString());
                mLogger.debug("\n[SDS] stop filter");
                packetManager.removeFilterPid(SDT_PID);
                packetManager.deleteObserver(sdsManager);
            }
        });


        // add Observer
        packetManager.addObserver(pasManager);
        packetManager.addObserver(sdsManager);
        packetManager.addObserver(pmsManager);

        // start filter
        packetManager.addFilterPid(PAT_PID);
        packetManager.addFilterPid(SDT_PID);
        packetManager.filterPacketByPid();
    }

    public List<Program> getProgramList() {
        if (mPat != null && mSdt != null) {
            mProgramList = mPat.getProgramList();
            List<Service> serviceList = mSdt.getServiceList();
            for (Program program : mProgramList) {
                int serviceId = program.getProgramNumber();
                for (Service service : serviceList) {
                    if (service.getServiceId() == serviceId) {
                        String name = new String(service.getServiceDescriptor().getServiceName());
                        program.setProgramName(name);
                        break;
                    }
                }
            }
        }
        return mProgramList;
    }

    public List<ProgramMapSection> getPmtList() {
        return mPmtList;
    }
}
