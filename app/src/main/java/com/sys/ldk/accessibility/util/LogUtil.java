package com.sys.ldk.accessibility.util;


import android.util.Log;

import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.sdcard.SaveLog;
import com.sys.ldk.serverset.Keyguard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LogUtil {
    private static final String TAG = User.gettime();
    private static final String TS = "ldk";
    private static boolean isSHow = User.isApkInDebug(Keyguard.context);
    public static boolean issave = true;
    public static String filename = "1.txt";
    public static void E(String msg) {
        if (isSHow) {
            Log.e(TS + " " + TAG, msg);
        }
        if(issave){
            savelog(msg);
        }
    }

    public static void D(String msg) {
        if (isSHow) {
            Log.d(TS + " " + TAG, msg);
        }
        if(issave){
            savelog(msg);
        }
    }

    public static void I(String msg) {
        if (isSHow) {
            Log.i(TS + " " + TAG, msg);
        }
        if(issave){
            savelog(msg);
        }
    }

    public static void V(String msg) {
        if (isSHow) {
            Log.v(TS + " " + TAG, msg);
        }
        if(issave){
            savelog(msg);
        }
    }

    public static void W(String msg) {
        if (isSHow) {
            Log.w(TS + " " + TAG, msg);
        }
        if(issave){
            savelog(msg);
        }
    }

    public static void setFilename(String filename) {
        LogUtil.filename = filename;
    }

    public static void savelog(String msg) {
        SaveLog.saveUserInfo(msg, filename);
    }
}
