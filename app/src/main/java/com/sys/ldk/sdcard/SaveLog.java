package com.sys.ldk.sdcard;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.sys.ldk.accessibility.util.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Nine_Dollar
 * @date: 2020/11/8
 * @description 调试信息存储到sd卡
 */
public class SaveLog {

    /**
     * @description 保存调试信息
     * @param text:内容
     * @param fileName:文件名
     * @return
     * @author Nine_Dollar
     * @time 2020/11/8 1:12
     */
    public static boolean saveUserInfo(String text, String fileName) {
        try {
            // 判断当前的手机是否有sd卡
            String state = Environment.getExternalStorageState();
            if(!Environment.MEDIA_MOUNTED.equals(state)) {
                // 已经挂载了sd卡
                return false;
            }

            File sdCardFile = Environment.getExternalStorageDirectory();
//            sdCardFile:路径
            File file = new File(sdCardFile+"/LDK");
            if(!file.exists()){
                file.mkdirs();
            }
            file = new File(sdCardFile+"/LDK",fileName);
            FileOutputStream fos = new FileOutputStream(file,true);
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

    public static Map<String, String> getUserInfo(Context context) {
        try {
            // 判断当前的手机是否有sd卡
            String state = Environment.getExternalStorageState();

            if(!Environment.MEDIA_MOUNTED.equals(state)) {
                // 已经挂载了sd卡
                return null;
            }
            File sdCardFile = Environment.getExternalStorageDirectory();
            File file = new File(sdCardFile, "data.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String text = br.readLine();
            br.close();
            if(!TextUtils.isEmpty(text)) {
                Map<String, String> userInfoMap = new HashMap<String, String>();
                String[] split = text.split("##");
                userInfoMap.put("number", split[0]);
                userInfoMap.put("password", split[1]);
                return userInfoMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
