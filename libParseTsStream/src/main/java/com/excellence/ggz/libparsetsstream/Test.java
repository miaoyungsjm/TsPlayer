package com.excellence.ggz.libparsetsstream;

import com.excellence.ggz.libparsetsstream.Packet.PacketManager;
import com.excellence.ggz.libparsetsstream.Section.AbstractSectionManager;
import com.excellence.ggz.libparsetsstream.Section.ProgramAssociationSectionManager;
import com.excellence.ggz.libparsetsstream.Section.ProgramMapSectionManager;
import com.excellence.ggz.libparsetsstream.Section.ServiceDescriptionSectionManager;
import com.excellence.ggz.libparsetsstream.Section.entity.ProgramAssociationSection;
import com.excellence.ggz.libparsetsstream.Section.entity.ProgramMapSection;
import com.excellence.ggz.libparsetsstream.Section.entity.Section;
import com.excellence.ggz.libparsetsstream.Section.entity.ServiceDescriptionSection;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.SimpleLayout;

import java.io.IOException;

import static com.excellence.ggz.libparsetsstream.Section.ProgramAssociationSectionManager.PAT_PID;
import static com.excellence.ggz.libparsetsstream.Section.ServiceDescriptionSectionManager.SDT_PID;

public class Test {
    private static final String INPUT_FILE1_PATH = "/Users/miaoyun/Documents/ts_file/001.ts";
    private static final String INPUT_FILE2_PATH = "f:/bak/sx/tools/001.ts";

    public static void main(String[] args) throws IOException {
        String inputFile = INPUT_FILE2_PATH;

        final Logger root = Logger.getRootLogger();
        root.addAppender(new ConsoleAppender(new PatternLayout("%r [%t] %p %l %m%n")));
        root.addAppender(new FileAppender(new SimpleLayout(), "ts.log"));
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
                root.debug(programAssociationSection.toString());
                root.debug("\n[PAS] stop filter");
                packetManager.deleteObserver(pasManager);
                packetManager.removeFilterPid(PAT_PID);
                packetManager.addFilterPid(0x100);
            }
        });

        final ServiceDescriptionSectionManager sdsManager = ServiceDescriptionSectionManager.getInstance();
        sdsManager.setOnParseListener(new AbstractSectionManager.OnParseListener() {
            @Override
            public void onFinish(Section section) {
                ServiceDescriptionSection serviceDescriptionSection = (ServiceDescriptionSection) section;
                root.debug(serviceDescriptionSection.toString());
                root.debug("\n[SDS] stop filter");
                packetManager.deleteObserver(sdsManager);
                packetManager.removeFilterPid(SDT_PID);
            }
        });

        // todo: for one PMT test
        final ProgramMapSectionManager pmsManager = ProgramMapSectionManager.getInstance(0x100);
        pmsManager.setOnParseListener(new AbstractSectionManager.OnParseListener() {
            @Override
            public void onFinish(Section section) {
                ProgramMapSection programMapSection = (ProgramMapSection) section;
                root.debug(programMapSection.toString());
                root.debug("\n[PMS] stop filter");
                packetManager.deleteObserver(pmsManager);
                packetManager.removeFilterPid(0x100);
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
}