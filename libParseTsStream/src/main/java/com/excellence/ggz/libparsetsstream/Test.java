package com.excellence.ggz.libparsetsstream;

import com.excellence.ggz.libparsetsstream.Section.entity.Program;

import org.apache.log4j.Logger;

import java.util.List;

public class Test {
    private static final String INPUT_FILE1_PATH = "/Users/miaoyun/Documents/ts_file/001.ts";
    private static final String INPUT_FILE2_PATH = "f:/bak/sx/tools/001.ts";

    public static void main(String[] args) {

        TsManager tsManager = TsManager.getInstance();
        tsManager.parseTsFile(INPUT_FILE2_PATH);
        List<Program> list = tsManager.getProgramList();

        Logger logger = Logger.getLogger(Test.class);
        for (Program program : list) {
            logger.debug(program.toString());
        }
    }
}