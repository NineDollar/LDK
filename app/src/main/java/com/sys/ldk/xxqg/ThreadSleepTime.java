package com.sys.ldk.xxqg;

import com.sys.ldk.accessibility.api.User;

public class ThreadSleepTime {

    /**
     * 3秒
     */
    public static void sleeploglog() {
        User.Threadsleep(3);
    }

    /**
     * 随眠2秒
     */
    public static void sleeplog() {
        User.Threadsleep(2);
    }

    /**
     * 随眠一秒
     */
    public static void sleep() {
        User.Threadsleep(1);
    }


    /**
     * 睡眠0.5秒
     */
    public static void sleepshort() {
        User.Threadsleep500();
    }

    /**
     * 睡眠0.2秒
     */
    public static void sleepshorts() {
        User.Threadsleep200();
    }
}
