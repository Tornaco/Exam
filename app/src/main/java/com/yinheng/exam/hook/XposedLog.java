package com.yinheng.exam.hook;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by guohao4 on 2018/1/16.
 * Email: Tornaco@163.com
 */

public class XposedLog {

    public static void boot(String format, Object... args) {
        XposedBridge.log("Nick-" + String.format(format, args));
    }

    public static void debug(String log) {
        XposedBridge.log("Nick-" + log);
    }


    public static void wtf(String format, Object... args) {
        XposedBridge.log("Nick-" + String.format(format, args));
    }

    public static void verbose(String format, Object... args) {
        XposedBridge.log("Nick-" + String.format(format, args));
    }
}
