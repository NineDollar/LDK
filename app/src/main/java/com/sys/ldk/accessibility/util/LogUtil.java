package com.sys.ldk.accessibility.util;


import android.util.Log;

import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.serverset.Keyguard;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {
    private static final String TAG = gettime();
    private static final String TS = "ldk";
    private static boolean isSHow = User.isApkInDebug(Keyguard.context);

    public static void E(String msg) {
        if (isSHow)
            Log.e(TS+" "+TAG,msg);
    }

    public static void D(String msg) {
        if (isSHow)
            Log.d(TS+" "+TAG, msg);
    }

    public static void I(String msg) {
        if (isSHow)
            Log.i(TS+" "+TAG, msg);
    }

    public static void V(String msg){
        if (isSHow)
            Log.v(TS+" "+TAG, msg);
    }

    public static void W(String msg) {
        if (isSHow)
            Log.w(TS+" "+TAG, msg);
    }

    public static String gettime() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

}
