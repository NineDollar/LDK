package com.sys.ldk;

public class MainConfig {
    //    private static String addresses = "http://api.tianapi.com/txapi/pyqwenan/index";
//    private static String key = "c534b2aeba869fc479c36bb5e6dbb201";
    private static String addresses;
    private static String key;

    public static String url;

    public static String getUrl() {
        url = getAddresses() + "?key=" + getKey();
        return url;
    }

    public static void setUrl(String url) {
        MainConfig.url = url;
    }

    public static String getAddresses() {
        return addresses;
    }

    public static void setAddresses(String addresses) {
        MainConfig.addresses = addresses;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        MainConfig.key = key;
    }
}
