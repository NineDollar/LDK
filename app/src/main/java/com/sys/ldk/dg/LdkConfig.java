package com.sys.ldk.dg;

/**
 * <p></p>
 *
 * @author: Nine_Dollar
 * @date: 2021/2/23
 */
public class LdkConfig {
    //    毫秒
    private static int read_time = 60 * 1000;
    private static int video_time = 60 * 1000;
    private static int reading_times = 5;//文章阅读次数
    private static int videoing_times = 5;//视频阅读次数
    private static boolean is_xin_wen_lian_bo = false;
    private static boolean save = true;
    private static boolean duan_video = false;

    public static boolean isDuan_video() {
        return duan_video;
    }

    public static void setDuan_video(boolean duan_video) {
        LdkConfig.duan_video = duan_video;
    }

    public static int getVideoing_times() {
        return videoing_times;
    }

    public static void setVideoing_times(int videoing_times) {
        LdkConfig.videoing_times = videoing_times;
    }

    public static boolean isIs_xin_wen_lian_bo() {
        return is_xin_wen_lian_bo;
    }

    public static void setIs_xin_wen_lian_bo(boolean is_xin_wen_lian_bo) {
        LdkConfig.is_xin_wen_lian_bo = is_xin_wen_lian_bo;
    }

    public static boolean isSave() {
        return save;
    }

    public static void setSave(boolean save) {
        LdkConfig.save = save;
    }

    /**
     * 文章阅读次数
     *
     * @return
     */
    public static int getReading_times() {
        return reading_times;
    }

    public static void setReading_times(int reading_times) {
        LdkConfig.reading_times = reading_times;
    }

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
     *
     * @return
     */
    public static int getRead_time_Mill() {
        return read_time;
    }

    /**
     * 毫秒
     *
     * @param read_time
     */
    public static void setRead_time(int read_time) {
        LdkConfig.read_time = read_time;
    }

    public static int getVideo_time_second() {
        int m = video_time / 1000;
        return m;
    }

    public static int getVideo_time_Mill() {

        return video_time;
    }

    public static void setVideo_time(int video_time) {
        LdkConfig.video_time = video_time;
    }
}
