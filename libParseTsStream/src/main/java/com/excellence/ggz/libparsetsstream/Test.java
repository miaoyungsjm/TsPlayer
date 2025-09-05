package com.excellence.ggz.libparsetsstream;

import com.excellence.ggz.libparsetsstream.Logger.LoggerManager;
import com.excellence.ggz.libparsetsstream.Section.entity.Program;

import java.util.List;

public class Test {
    private static final String INPUT_FILE1_PATH = "/Users/miaoyun/Documents/workspace/001.ts";
    private static final String INPUT_FILE2_PATH = "f:/bak/sx/tools/001.ts";

    public static void main(String[] args) {

        TsManager tsManager = TsManager.getInstance();
        tsManager.parseTsFile(INPUT_FILE1_PATH);
        List<Program> list = tsManager.getProgramList();

        LoggerManager logger = LoggerManager.getInstance();
        for (Program program : list) {
            logger.debug(Test.class.getName(), program.toString());
        }
    }
}