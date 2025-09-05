package com.excellence.ggz.libparsetsstream.Logger;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.SimpleLayout;

import java.io.IOException;

public class LoggerManager {
    private static final String LOGGER_OUTPUT_PATTERN = "%r [%t] %p %l %m%n";
    private static final String LOGGER_OUTPUT_FILE = "ts.log";

    private static class LoggerManagerHolder {
        private static final LoggerManager sInstance = new LoggerManager();
    }

    public static LoggerManager getInstance() {
        return LoggerManagerHolder.sInstance;
    }

    private LoggerManager() {
        try {
            Logger logger = Logger.getRootLogger();
            logger.addAppender(new ConsoleAppender(new PatternLayout(LOGGER_OUTPUT_PATTERN)));
            logger.addAppender(new FileAppender(new SimpleLayout(), LOGGER_OUTPUT_FILE));
            logger.setLevel(Level.DEBUG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void debug(String moduleName, String msg) {
        Logger logger = Logger.getLogger(moduleName);
        logger.debug(msg);
    }
}
