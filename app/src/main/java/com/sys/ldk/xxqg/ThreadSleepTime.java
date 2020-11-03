package com.sys.ldk.xxqg;

import com.sys.ldk.accessibility.api.User;

public class ThreadSleepTime {
    public static void threadsleeplog() {
        User.Threadsleep(2);
    }

    public static void threadsleep() {
        User.Threadsleep(1);
    }

    public static void threadsleepshort() {
        User.Threadsleep500();
    }

    public static void threadsleepshortshort() {
        User.Threadsleep200();
    }
}
