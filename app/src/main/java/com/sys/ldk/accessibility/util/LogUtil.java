package com.sys.ldk.accessibility.util;

import android.util.Log;

import com.sys.ldk.sdcard.SaveLog;

import static com.sys.ldk.dg.LdkConfig.isSave;

public class LogUtil {
    private static final String TS = "SYS: ";

    public static void E(String msg) {

        Log.e(TS + " ", msg);

        if (isSave()) {
            SaveLog.save("E: " + msg);
        }
    }

    public static void D(String msg) {

        Log.d(TS + " ", msg);

        if (isSave()) {
            SaveLog.save("D: " + msg);
        }
    }

    public static void I(String msg) {

        Log.i(TS + " ", msg);

        if (isSave()) {
            SaveLog.save("I: " + msg);
        }
    }

    public static void V(String msg) {

//        Log.v(TS + " ", msg);

//        线程睡眠不保存
        /*if (issave) {
            SaveLog.save("V: " + msg);
        }*/
    }

    public static void W(String msg) {

        Log.w(TS + " ", msg);

        if (isSave()) {
            SaveLog.save("W: " + msg);
        }
    }

}
