package com.sys.ldk.serverset;

import android.annotation.SuppressLint;
import android.content.Context;

import com.sys.ldk.MyApplication;

import static android.content.Context.KEYGUARD_SERVICE;

public class Keyguard {

    @SuppressLint("StaticFieldLeak")
    public static Context context = MyApplication.getContext();
    //    键盘是否锁定
    public static boolean isKeyguardLocked = false;
    //    判断是否设置了图案或者PIN密码
    public static boolean isDeviceSecure = false;
    //    keyguardManager
    public static boolean isKeyguardSecure = false;
    //    判断当前设备是否需要图案或者PIN输入
    public static boolean isDeviceLocked = false;
    //    密码

    public Keyguard(Context context) {
        Keyguard.context = context;
    }

    public void KeyguardManager() {
        android.app.KeyguardManager keyguardManager = (android.app.KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        //            判断键盘是否锁定
        isKeyguardLocked = keyguardManager.isKeyguardLocked();
//           判断是否设置了图案或者PIN密码
        isDeviceSecure = keyguardManager.isDeviceSecure();
//           判断是否由图案或者PIN锁定
        isKeyguardSecure = keyguardManager.isKeyguardSecure();
//           判断当前设备是否需要图案或者PIN输入
        isDeviceLocked = keyguardManager.isDeviceLocked();
    }
}
