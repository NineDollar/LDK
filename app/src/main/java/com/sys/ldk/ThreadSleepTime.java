package com.sys.ldk;

import com.sys.ldk.accessibility.api.User;

import static com.sys.ldk.dg.ReturnType.my_stop;

public class ThreadSleepTime {

    /**
     * 自己设定，单位秒
     *
     * @param time
     * @return
     */
    public static boolean user_sleep(long time) {
        return User.sleep(time);
    }

    /**
     * 自己设定，单位毫秒
     *
     * @param time
     * @return
     */
    public static boolean sleep(long time) {
        return DG_Thread.MyThread.sleepmy(time) == my_stop;
    }

    /**
     * 3秒
     */
    public static boolean sleep3() {
        return DG_Thread.MyThread.sleepmy(3000) == my_stop;
    }

    /**
     * 睡眠两秒
     *
     * @return
     */
    public static boolean sleep2() {
        return DG_Thread.MyThread.sleepmy(2000) == my_stop;
    }

    /**
     * 随眠1.5秒
     */
    public static boolean sleep1D5() {
        return DG_Thread.MyThread.sleepmy(1500) == my_stop;
    }

    /**
     * 随眠1秒
     */
    public static boolean sleep1() {
        return DG_Thread.MyThread.sleepmy(1000) == my_stop;
    }

    /**
     * 睡眠0.7秒
     */
    public static boolean sleep0D7() {
        return DG_Thread.MyThread.sleepmy(700) == my_stop;
    }

    /**
     * 睡眠0.5秒
     */
    public static boolean sleep0D5() {
        return DG_Thread.MyThread.sleepmy(500) == my_stop;
    }

    /**
     * 睡眠0.2秒
     */
    public static boolean sleep0D2() {
        return DG_Thread.MyThread.sleepmy(200) == my_stop;
    }

}
