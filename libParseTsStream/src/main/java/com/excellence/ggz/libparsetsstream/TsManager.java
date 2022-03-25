package com.excellence.ggz.libparsetsstream;

import static com.excellence.ggz.libparsetsstream.Section.ProgramAssociationSectionManager.PAT_PID;
import static com.excellence.ggz.libparsetsstream.Section.ServiceDescriptionSectionManager.SDT_PID;
import static java.lang.Integer.toHexString;

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
    private final List<ProgramMapSection> mPmtList = new ArrayList<>();
    private List<Program> mProgramList = new ArrayList<>();
    private PacketManager mPacketManager;
    private ProgramAssociationSectionManager mPasManager;
    private ProgramMapSectionManager mPmsManager;
    private ServiceDescriptionSectionManager mSdsManager;

    private final Logger mLogger = Logger.getRootLogger();

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

    private TsManager() {
        mLogger.addAppender(new ConsoleAppender(new PatternLayout("%r [%t] %p %l %m%n")));
//        root.addAppender(new FileAppender(new SimpleLayout(), "ts.log"));
        mLogger.setLevel(Level.DEBUG);
    }

    private void initCallBack() {
        mPasManager.setOnParseListener(new AbstractSectionManager.OnParseListener() {
            @Override
            public void onFinish(Section section) {
                mPat = (ProgramAssociationSection) section;
                mLogger.debug(mPat.toString());
                mLogger.debug("\n[PAS] stop filter");
                mPacketManager.removeFilterPid(PAT_PID);
                mPacketManager.deleteObserver(mPasManager);

                mPmtList.clear();
                List<Program> programList = mPat.getProgramList();
                for (Program program : programList) {
                    int programNumber = program.getProgramNumber();
                    int pmtPid = program.getProgramMapPid();
                    if (programNumber > 0) {
                        mPacketManager.addFilterPid(pmtPid);
                        mPacketManager.addFilterPid(pmtPid);
                    }
                }
            }
        });

        mSdsManager.setOnParseListener(new AbstractSectionManager.OnParseListener() {
            @Override
            public void onFinish(Section section) {
                mSdt = (ServiceDescriptionSection) section;
                mLogger.debug(mSdt.toString());
                mLogger.debug("\n[SDS] stop filter");
                mPacketManager.removeFilterPid(SDT_PID);
                mPacketManager.deleteObserver(mSdsManager);
            }
        });

        mPmsManager.setOnParseListener(new AbstractSectionManager.OnParseListener() {
            @Override
            public void onFinish(Section section) {
                ProgramMapSection programMapSection = (ProgramMapSection) section;
                mLogger.debug(programMapSection.toString());
                mPmtList.add(programMapSection);

                int pmtPid = programMapSection.getPid();
                mLogger.debug("\n[PMS] stop filter pid: 0x" + toHexString(pmtPid));
                mPacketManager.removeFilterPid(pmtPid);
                mPmsManager.removeFilterPid(pmtPid);
                if (mPmsManager.getFilterPidList().size() == 0) {
                    mPacketManager.deleteObserver(mPmsManager);
                }
            }
        });
    }

    public void parseTsFile(String filePath) {
        mPacketManager = new PacketManager(filePath);
        mPacketLength = mPacketManager.getPacketLength();
        mPacketStartPosition = mPacketManager.getPacketStartPosition();

        mPasManager = ProgramAssociationSectionManager.getInstance();
        mSdsManager = ServiceDescriptionSectionManager.getInstance();
        mPmsManager = ProgramMapSectionManager.getInstance();
        initCallBack();

        // add Observer
        mPacketManager.addObserver(mPasManager);
        mPacketManager.addObserver(mSdsManager);
        mPacketManager.addObserver(mPmsManager);

        // start filter
        mPacketManager.addFilterPid(PAT_PID);
        mPacketManager.addFilterPid(SDT_PID);
        mPacketManager.filterPacketByPid();
    }

    public int getPacketLength() {
        return mPacketLength;
    }

    public int getPacketStartPosition() {
        return mPacketStartPosition;
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
