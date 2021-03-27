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
    private static int video_time = 60 * 1000 * 5;
    private static int duan_video_time = 60 * 1000 * 5;//短视频时间
    private static int reading_times = 10;//文章阅读次数
    private static int videoing_times = 10;//视频阅读次数
    private static boolean xin_wen_lian_bo = true;
    private static boolean save = false;
    private static boolean duan_video = true;
    private static boolean dati = true;
    private static boolean read = true;
    private static boolean sichuan = true;
    private static boolean video = true;

    public static boolean isSichuan() {
        return sichuan;
    }

    public static void setSichuan(boolean sichuan) {
        LdkConfig.sichuan = sichuan;
    }

    public static boolean isVideo() {
        return video;
    }

    public static void setVideo(boolean video) {
        LdkConfig.video = video;
    }

    public static boolean isRead() {
        return read;
    }

    public static void setRead(boolean read) {
        LdkConfig.read = read;
    }

    public static boolean isDati() {
        return dati;
    }

    public static void setDati(boolean dati) {
        LdkConfig.dati = dati;
    }

    public static int getDuan_video_time_second() {
        return duan_video_time / 60 / 1000;
    }

    public static int getDuan_video_time_Mill() {
        return duan_video_time;
    }

    public static void setDuan_video_time(int duan_video_time) {
        duan_video_time *= 60 * 1000;
        LdkConfig.duan_video_time = duan_video_time;
    }

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

    public static boolean isXin_wen_lian_bo() {
        return xin_wen_lian_bo;
    }

    public static void setXin_wen_lian_bo(boolean xin_wen_lian_bo) {
        LdkConfig.xin_wen_lian_bo = xin_wen_lian_bo;
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
