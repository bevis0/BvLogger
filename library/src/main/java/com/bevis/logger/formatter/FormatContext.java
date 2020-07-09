package com.bevis.logger.formatter;

import com.bevis.logger.Setting;

/**
 * 暂时没想让它干什么
 */
public class FormatContext {
    private static FormatContext sGlobal;

    public static FormatContext global() {
        if(sGlobal == null) {
            synchronized (FormatContext.class) {
                if(sGlobal == null) {
                    sGlobal = new FormatContext();
                }
            }
        }
        return sGlobal;
    }

    static FormatContext create() {
        return global().fork();
    }

    private FormatContext() {
    }

    private FormatContext(FormatContext parent) {
        if(parent != null) {
            if(setting != null) {
                setting.setGson(parent.setting.getGson());
                setting.setAsyncOutput(parent.setting.isAsyncOutput());
            }
        }
    }

    private Setting setting = new Setting();

    private FormatContext fork() {
        FormatContext global = global();
        return new FormatContext(global);
    }

    public Setting getSetting() {
        return setting;
    }
}
