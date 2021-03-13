package com.sys.ldk.serverset;

/**
 * @author: Nine_Dollar
 * @date: 2020/11/4
 * @description 通知类型
 */
public class MyNotificationType {
    public static final int case1 = 10;
    public static final String keytitle1 = "1";
    public static final String keytext1 = "2";
    public static String messagetitle1 = "还未学习";
    public static String messagetext1 = "还未学习";

    public static String getMessagetext1() {
        return messagetext1;
    }

    public static void setMessagetext1(String messagetext1) {
        MyNotificationType.messagetext1 = messagetext1;
    }

    public static String getMessagetitle1() {
        return messagetitle1;
    }

    public static void setMessagetitle1(String messagetitle1) {
        MyNotificationType.messagetitle1 = messagetitle1;
    }
}
