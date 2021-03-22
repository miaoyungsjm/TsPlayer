package com.excellence.ggz.libparsetsstream.Section;

import com.excellence.ggz.libparsetsstream.Section.entity.Section;

/**
 * @author ggz
 * @date 2021/3/22
 */
public class ServiceDescriptionSectionManager extends AbstractSectionManager {

    private static volatile ServiceDescriptionSectionManager sInstance = null;

    private ServiceDescriptionSectionManager() {
    }

    public static ServiceDescriptionSectionManager getInstance() {
        if (sInstance == null) {
            synchronized (ServiceDescriptionSectionManager.class) {
                if (sInstance == null) {
                    sInstance = new ServiceDescriptionSectionManager();
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
