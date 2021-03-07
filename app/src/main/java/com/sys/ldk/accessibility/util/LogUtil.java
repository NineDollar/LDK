package com.sys.ldk.accessibility.util;

import android.util.Log;

import com.sys.ldk.MainActivity;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.sdcard.SaveLog;
import com.sys.ldk.serverset.Keyguard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.sys.ldk.dg.Config.issave;

public class LogUtil {
    private static final String TS = "SYS: ";
    private static final boolean isSHow = User.isApkInDebug(MainActivity.getMycontext());

    public static void E(String msg) {
        if (isSHow) {
            Log.e(TS + " ", msg);
        }
        if (issave) {
            SaveLog.save("E: " + msg);
        }
    }

    public static void D(String msg) {
        if (isSHow) {
            Log.d(TS + " ", msg);
        }
        if (issave) {
            SaveLog.save("D: " + msg);
        }
    }

    public static void I(String msg) {
        if (isSHow) {
            Log.i(TS + " ", msg);
        }
        if (issave) {
            SaveLog.save("I: " + msg);
        }
    }

    public static void V(String msg) {
        if (isSHow) {
            Log.v(TS + " ", msg);
        }
//        线程睡眠不保存
        /*if (issave) {
            SaveLog.save("V: " + msg);
        }*/
    }

    public static void W(String msg) {
        if (isSHow) {
            Log.w(TS + " ", msg);
        }
        if (issave) {
            SaveLog.save("W: " + msg);
        }
    }

}
