package com.excellence.ggz.libparsetsstream.Section;

import com.excellence.ggz.libparsetsstream.Section.entity.Section;

/**
 * @author ggz
 * @date 2021/3/22
 */
public class ProgramAssociationSectionManager extends AbstractSectionManager {

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
}
