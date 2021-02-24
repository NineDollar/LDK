package com.sys.ldk.sdcard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.sys.ldk.R;
import com.sys.ldk.accessibility.util.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Nine_Dollar
 * @date: 2020/11/8
 * @description 调试信息存储到sd卡
 */
public class SaveLog {
    private final static String str1 = "Log-";//文件后缀
    private final static String str2 = ".txt";//文件后缀
    public static String FileName = "LDK";
    private static String save_file_name = str1 + FileName + str2;

    public static void setSave_file_name(String save_file_name) {
        SaveLog.save_file_name = str1+save_file_name+str2;
    }

    public static String getSave_file_name() {
        return save_file_name;
    }

    /**
     * @param text:内容
     * @param fileName:文件名
     * @return
     * @description 保存调试信息
     * @author Nine_Dollar
     * @time 2020/11/8 1:12
     */
    public static boolean saveLog(String text, String fileName) {
        try {
            // 判断当前的手机是否有sd卡
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                // 已经挂载了sd卡
                return false;
            }

            File sdCardFile = Environment.getExternalStorageDirectory();
//            sdCardFile:路径
            File file = new File(sdCardFile + "/LDK");
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(sdCardFile + "/LDK", fileName);
            FileOutputStream fos = new FileOutputStream(file, true);
            String data = text;
            fos.write(data.getBytes());
            fos.write("\r\n".getBytes());
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getLog(String FileName) {
        String log_text = "";
        try {
            // 判断当前的手机是否有sd卡
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                // 已经挂载了sd卡
                return null;
            }
            File sdCardFile = Environment.getExternalStorageDirectory();
            File file = new File(sdCardFile+ "/LDK", FileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String string = null;
            while ((string = br.readLine())!=null){
                log_text += string+"\n";
            }
            br.close();
            if (!TextUtils.isEmpty(log_text)) {
                return log_text;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    获取时间
    public static String get_time_name() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        String date = format.format(new Date(System.currentTimeMillis()));
        return date;// 2012年10月03日 23:41:31
    }

    public static void save(String text) {
        saveLog(text, save_file_name);
    }
}
