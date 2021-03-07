package com.sys.ldk.dg;

/**
 * <p></p>
 *
 * @author: Nine_Dollar
 * @date: 2021/2/23
 */
public class Config {
    //    毫秒
    public static int read_time = 60 * 2 * 1000;
    public static int video_time = 60 * 5 * 1000;
    public static boolean is_xin_wen_lian_bo = false;
    public static boolean issave = true;

    /**
     * 返回秒
     *
     * @return
     */
    public static int getRead_time_second() {
        int m = read_time / 1000;
        return m;
    }

    /**
     * 毫秒
     * @return
     */
    public static int getRead_time_Mill() {
        return read_time;
    }

    public static void setRead_time(int read_time) {

        Config.read_time = read_time;
    }

    public static int getVideo_time_second() {
        int m = video_time / 1000;
        return m;
    }

    public static int getVideo_time_Mill() {

        return video_time;
    }

    public static void setVideo_time(int video_time) {
        Config.video_time = video_time;
    }
}
