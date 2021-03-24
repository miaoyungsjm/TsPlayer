package com.excellence.ggz.libparsetsstream;

import com.excellence.ggz.libparsetsstream.Packet.PacketManager;
import com.excellence.ggz.libparsetsstream.Section.AbstractSectionManager;
import com.excellence.ggz.libparsetsstream.Section.ProgramAssociationSectionManager;
import com.excellence.ggz.libparsetsstream.Section.ServiceDescriptionSectionManager;
import com.excellence.ggz.libparsetsstream.Section.entity.Program;
import com.excellence.ggz.libparsetsstream.Section.entity.ProgramAssociationSection;
import com.excellence.ggz.libparsetsstream.Section.entity.Section;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.util.List;

import static com.excellence.ggz.libparsetsstream.Section.ProgramAssociationSectionManager.PAT_PID;

public class Test {
    private static final String INPUT_FILE1_PATH = "/Users/miaoyun/Documents/ts_file/001.ts";
    private static final String INPUT_FILE2_PATH = "f:/bak/sx/tools/001.ts";

    public static void main(String[] args) {
        String inputFile = INPUT_FILE1_PATH;

        final Logger root = Logger.getRootLogger();
        root.addAppender(new ConsoleAppender(new PatternLayout("%r [%t] %p %l %m%n")));
        root.setLevel(Level.DEBUG);

        final PacketManager packetManager = new PacketManager(inputFile);
        final int packetLength = packetManager.getPacketLength();
        int packetStartPosition = packetManager.getPacketStartPosition();

        // observable - observer
        final ProgramAssociationSectionManager pasManager = ProgramAssociationSectionManager.getInstance();
        pasManager.setOnParseListener(new AbstractSectionManager.OnParseListener() {
            @Override
            public void onFinish(Section section) {
                ProgramAssociationSection programAssociationSection = (ProgramAssociationSection) section;
                programAssociationSection.toPrint();
                root.debug("PAT stop filter");
                packetManager.deleteObserver(pasManager);

                List<Program> programList = programAssociationSection.getProgramList();
                for (Program program : programList) {
                    int pmtPid = program.getProgramMapPid();
                    if (pmtPid > 0) {
                        // todo: modify filterPacketByPid to filter several pid packets at the same time
                        packetManager.addFilterPid(pmtPid);
                    }
                }
            }
        });

        final ServiceDescriptionSectionManager sdsManager = ServiceDescriptionSectionManager.getInstance();
        sdsManager.setOnParseListener(new AbstractSectionManager.OnParseListener() {
            @Override
            public void onFinish(Section section) {
                root.debug("SDT stop filter");
                packetManager.deleteObserver(sdsManager);
            }
        });

        // start filter
        packetManager.addObserver(pasManager);
        packetManager.addObserver(sdsManager);
        packetManager.filterPacketByPid(PAT_PID);
    }
}