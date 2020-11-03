package com.sys.ldk.power;

import android.annotation.SuppressLint;

import android.content.Context;

import android.os.PowerManager;

import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.serverset.Keyguard;

import static android.content.Context.POWER_SERVICE;

public class PowerThread {
    private Context context;

    public PowerThread(Context context) {
        this.context = context;
    }


    public boolean openphone() {
        Keyguard keyguard = new Keyguard(context);
        keyguard.KeyguardManager();
        if(!wakeUpAndUnlock()){
            return false;
        }
        return true;
    }

    /**
     * 唤醒手机屏幕并解锁
     */
    public boolean wakeUpAndUnlock() {
        // 获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        LogUtil.D("屏幕亮：" + screenOn);
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire(1000); // 点亮屏幕
            wl.release(); // 释放

            LogUtil.D(Keyguard.isKeyguardLocked + " isKeyguardLocked");
            LogUtil.D(Keyguard.isDeviceSecure + " isDeviceSecure");
            LogUtil.D(Keyguard.isKeyguardSecure + " isKeyguardSecure");
            LogUtil.D(Keyguard.isDeviceLocked + " isDeviceLocked");
            if (Keyguard.isKeyguardLocked && !(Keyguard.isDeviceSecure && Keyguard.isKeyguardSecure && Keyguard.isDeviceLocked)) {
//                没有密码
                LogUtil.D("不需要输入密码");
                unlock();
            } else if (!Keyguard.isDeviceLocked) {
//                没有密码
                LogUtil.D("当前不需要密码");
                unlock();
            } else if (Keyguard.isKeyguardLocked && Keyguard.isDeviceSecure && Keyguard.isKeyguardSecure && Keyguard.isDeviceLocked) {
//              设置了密码
                LogUtil.D("输入密码");
                password();
            }
        } else {
            LogUtil.D("不需要亮屏");
        }
//            判断键盘是否锁定
         /*  LogUtil.V("键盘是否锁定："+keyguardManager.isKeyguardLocked());
//           判断是否设置了图案或者PIN密码
           LogUtil.V("判断是否设置了图案或者PIN密码："+keyguardManager.isDeviceSecure());
//           判断是否由图案或者PIN锁定
           LogUtil.V("判断是否由图案或者PIN锁定："+keyguardManager.isKeyguardSecure());
//           判断当前设备是否需要图案或者PIN输入
           LogUtil.V("判断当前设备是否需要图案或者PIN输入："+keyguardManager.isDeviceLocked());*/
        User.Threadsleep500();
        if (Keyguard.isDeviceLocked) {
            LogUtil.E("解锁失败");
            return false;
        }else {
            LogUtil.D("解锁成功");
        }
        return true;
    }

    public void unlock() {
        wakeUpAndUnlock1(context);
    }

    public void password() {
        wakeUpAndUnlock1(context);
    }

    @SuppressLint("Wakelock")
    @SuppressWarnings("deprecation")
    public void wakeUpAndUnlock1(Context context) {
        // 获取电源管理器对象
        PowerManager pm = (PowerManager) context
                .getSystemService(POWER_SERVICE);
        // 获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
        // 点亮屏幕
        wl.acquire();
        // 释放
        wl.release();
        // 得到键盘锁管理器对象
        android.app.KeyguardManager km = (android.app.KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        android.app.KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        // 解锁
        kl.disableKeyguard();
    }
}