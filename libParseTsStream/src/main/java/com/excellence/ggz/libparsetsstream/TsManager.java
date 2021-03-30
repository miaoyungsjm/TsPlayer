package com.excellence.ggz.libparsetsstream;

/**
 * @author ggz
 * @date 2021/3/30
 */
public class TsManager {

    private static volatile TsManager sInstance = null;

    private TsManager() {
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


}
