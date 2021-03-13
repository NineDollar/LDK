package com.sys.ldk.accessibility.api;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.view.accessibility.AccessibilityNodeInfo;


import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.shellService.SocketClient;
import com.sys.ldk.accessibility.util.ApiUtil;
import com.sys.ldk.accessibility.util.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class User {

    //    启动APP，上下文，包名，启动类
    public static boolean startAPP(Context context, String pkg, String cls) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(pkg)) {
                    Intent intent = new Intent();
                    ComponentName componentName = new ComponentName(pkg, cls);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(componentName);
                    context.startActivity(intent);
                    LogUtil.D(pn);
                    return true;
                }
            }
        }
        LogUtil.E("包名不正确或未安装");
        return false;
    }

    /**
     * 单位毫秒
     *
     * @param time
     * @return
     */
    public static boolean sleep(long time) {
        float time1 = (float) time / 1000;
        LogUtil.V("线程睡眠" + time1 + "秒");
        if (ApiUtil.sleepTime(time)) {
            return true;
        }
        LogUtil.V("睡眠结束");
        return false;
    }

    public static void authority_alerdialog(Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("使用应用服务需要打开辅助功能")
                .setNegativeButton("取消", null)
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
//                        show_root(context);
                    }
                })
                .create();
        alertDialog.show();
    }

    public static void show_root(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SocketClient("###AreYouOK", new SocketClient.onServiceSend() {
                    @Override
                    public void getSend(String result) {
                        showTextOnTextView(context, result);
                    }
                });
            }
        }).start();
    }

    public static void showTextOnTextView(Context context, final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (text.equals("###IamOK#") && User.isApkInDebug(context)) {
                    auto_acess();
                } else {
                    LogUtil.E("未提权，请手动点击");
                }
            }
        }).start();
    }

    public static void get_text_id_des(AccessibilityNodeInfo accessibilityNodeInfo) {
        LogUtil.D("text:" + accessibilityNodeInfo.getText() + "--id:" + accessibilityNodeInfo.getViewIdResourceName() + "--des:" + accessibilityNodeInfo.getContentDescription());
    }

    //  遍历所有节点，更具text点击节点
    public static boolean clik_Info_key(String key_text, String vau_text) {
        AccessibilityNodeInfo accessibilityNodeInfo = null;
        boolean flag = false;
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.getAllNode(null, null);
        String[] strings = new String[300];
        for (int n = 0; n < accessibilityNodeInfoList.size(); n++) {
            accessibilityNodeInfo = accessibilityNodeInfoList.get(n);
            String s = accessibilityNodeInfo.getText() + "";
            strings[n] = s;
            if (s == key_text || s.equals(key_text)) {
                LogUtil.E("" + n);
                flag = true;
            }/*else if(s.equals("以完成")){
//                该项已完成
                return false;
            }*/
            if (flag) {
                if (s == vau_text || s.equals(vau_text)) {
                    AcessibilityApi.performViewClick(accessibilityNodeInfoList.get(n));
                    return true;
                }
            }
        }
        LogUtil.E("未找到" + key_text + "---" + vau_text);
        return false;
    }

    //    通过text查找Info
    public static AccessibilityNodeInfo getInfoBytext(String text) {
        AccessibilityNodeInfo accessibilityNodeInfo = null;
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.getAllNode(null, null);
        for (int n = 0; n < accessibilityNodeInfoList.size(); n++) {
            accessibilityNodeInfo = accessibilityNodeInfoList.get(n);
            String s = accessibilityNodeInfo.getText() + "";
            if (s.equals(text)) {
                LogUtil.E("找到" + text + n);
                return accessibilityNodeInfoList.get(n);
            }
        }
        LogUtil.E("未找到Info：" + text);
        return null;
    }

    //遍历所有节点，更具text点击节点
    public static boolean clik_text_Info(String text) {
        AccessibilityNodeInfo accessibilityNodeInfo = null;
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.getAllNode(null, null);
        for (int n = 0; n < accessibilityNodeInfoList.size(); n++) {
            accessibilityNodeInfo = accessibilityNodeInfoList.get(n);
            String s = accessibilityNodeInfo.getText() + "";
            if (s.equals(text)) {
                LogUtil.E("" + n);
                AcessibilityApi.performViewClick(accessibilityNodeInfoList.get(n));
                return true;
            }
        }
        return false;
    }

    @NotNull
    public static List<AccessibilityNodeInfo> getScrollNodeInfo() {
        List<AccessibilityNodeInfo> getScrollNodeInfo = new ArrayList<>();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.getAllNode(null, null);
        int n = 0;
        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            if (a.isScrollable()) {
                getScrollNodeInfo.add(a);
                LogUtil.D("ScrollNodeInfo: " + n);
            }
            n += 1;
        }
        return getScrollNodeInfo;
    }

    /**
     * 获取所有节点text
     *
     * @param open 是否显示text
     * @return List<String>
     */
    public static List<String> getallInfottext(boolean open) {
        List<AccessibilityNodeInfo> listInfo = AcessibilityApi.getAllNode(null, null);
        List<String> stringListtext = new ArrayList<>();
        for (int i = 0; i < listInfo.size(); i++) {
            String text = listInfo.get(i).getText() + "";
            if (open) {
                LogUtil.D("Info " + i + " text: " + text);
            }
            stringListtext.add(text);
        }
        return stringListtext;
    }

    /**
     * 获取页面所有info{packageName，className，text，viewIdResName，clickable，enabled，scrollable}
     */
    public static void getallInfo() {
        int count = 0;
        String textstr = null;
        List<AccessibilityNodeInfo> listInfo = AcessibilityApi.getAllNode(null, null);
        if (listInfo.isEmpty()) {
            LogUtil.W("没有info");
            return;
        }
        for (AccessibilityNodeInfo a : listInfo
        ) {
            count++;
            if (a.getText() == null) {
                textstr = "null";
            } else {
                textstr = a.getText().toString();
            }

            LogUtil.D("Info " + count + " : text: " + textstr + ", getPackageName: " + a.getPackageName() + ", getClassName: " + a.getClassName() +
                    ", viewIdResName: " + a.getViewIdResourceName() + ", clickable: " + a.isClickable() + ", enabled: " + ", scrollable: " + a.isScrollable());
        }
    }

    /**
     * 函数功能是查找text后或者前的第几个Info，String【】第一个是查找的text，第二个是查找N个后的text，确认查找正确
     *
     * @param getInfoBytextaftermap
     * @return
     */
    public static HashMap<String[], AccessibilityNodeInfo> getallafterInfo(HashMap<String[], Integer> getInfoBytextaftermap) {
        HashMap<String[], AccessibilityNodeInfo> InfoList = new HashMap<>();
        List<AccessibilityNodeInfo> listInfo = AcessibilityApi.getAllNode(null, null);
//        查找的下标
        int m = 0;
        for (AccessibilityNodeInfo s : listInfo
        ) {
            String Infotext = s.getText() + "";
            for (HashMap.Entry e : getInfoBytextaftermap.entrySet()
            ) {
                String[] key = (String[]) e.getKey();
                int vaule = (int) e.getValue();
                if (Infotext.equals(key[0])) {
                    AccessibilityNodeInfo Info = listInfo.get(m + vaule);
                    String text1 = Info.getText() + "";
                    if (text1.equals(key[1])) {
                        LogUtil.D("查找" + key[0] + "后第" + vaule + "个Info的Text确认成功");
                        InfoList.put(key, Info);
                        LogUtil.D("找到：" + key + ", InfoisClickable: " + Info.isClickable() + ", InfoisEditable: " + Info.isEditable() + "InfoisScrollable: " + Info.isScrollable());
                    } else {
                        LogUtil.E("查找" + key[0] + "后第" + vaule + "个Info的Text确认失败");
                    }
                }
            }
            m += 1;
        }
        return InfoList;
    }

    /**
     * 查找text后n个info
     *
     * @param text
     * @param n
     * @return
     */
    public static AccessibilityNodeInfo get_text_after_info(String text, int n) {
        List<AccessibilityNodeInfo> accessibilityNodeInfos = AcessibilityApi.getAllNode(null, null);
        for (int i = 0; i < accessibilityNodeInfos.size(); i++) {
            if (text.equals(accessibilityNodeInfos.get(i).getText() + "")) {
                LogUtil.D("找到" + text + "后" + "第" + n + "个info");
                return accessibilityNodeInfos.get(i + n);
            }
        }
        return null;
    }

    public static HashMap<String[], AccessibilityNodeInfo> get_alter_info(List<AccessibilityNodeInfo> accessibilityNodeInfoList,
                                                                          HashMap<String[], Integer> hashMap) {
        int m = 0;
        HashMap<String[], AccessibilityNodeInfo> hashMap1 = new HashMap();
        for (AccessibilityNodeInfo accessibilityNodeInfo : accessibilityNodeInfoList
        ) {
            String text1 = accessibilityNodeInfo.getText() + "";
            for (Map.Entry e : hashMap.entrySet()
            ) {
                String[] key = (String[]) e.getKey();
                int index = (int) e.getValue();
                LogUtil.V(key[0]+" " + index+"");
                if (text1.equals(key[0])) {
                    LogUtil.D("查找：" + text1 + " 成功");
                    AccessibilityNodeInfo accessibilityNodeInfo1 = accessibilityNodeInfoList.get(m + index);
                    String text2 = accessibilityNodeInfo1.getText() + "";
                    LogUtil.D("text2:"+text2);
                    if (text2.equals(key[1])) {
                        LogUtil.D("查找" + key[0] + "后第" + index + "个Info的Text确认成功");
                        hashMap1.put(key, accessibilityNodeInfo);
                    }
                }
            }
            m++;
        }
        return hashMap1;
    }

    //    查找页面是否有存在text
    public static boolean findtext(String findtext) {
        List<String> stringList = User.getallInfottext(false);
        for (String s : stringList
        ) {
            if (s.equals(findtext)) {
                LogUtil.D("找到 " + findtext);
                return true;
            }
        }
        return false;
    }

    //    获取可以点击Info
    public static List<AccessibilityNodeInfo> getclickallInfo() {
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.getAllNode(null, null);
        List<AccessibilityNodeInfo> accessibilityNodeInfoList1 = new ArrayList<>();
        int i = 0;
        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            if (a.isClickable()) {
                i += 1;
                accessibilityNodeInfoList1.add(a);
            }
        }
        LogUtil.D("总共： " + accessibilityNodeInfoList1.size());
        return accessibilityNodeInfoList1;
    }

    //    获取节点是否可编辑
    public static List<AccessibilityNodeInfo> getEditableInfo() {
        List<AccessibilityNodeInfo> listInfo = AcessibilityApi.getAllNode(null, null);
        List<AccessibilityNodeInfo> editableInfo = new ArrayList<>();
        for (AccessibilityNodeInfo a : listInfo
        ) {
            if (a.isEditable()) {
                editableInfo.add(a);
            }
        }
        return editableInfo;
    }

    //    判断是否为debug
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static String gettime() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    @NotNull
    public static String gettimelog() {
        SimpleDateFormat df = new SimpleDateFormat("YYYY_MM_dd_HH_mm");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    //    自动点击辅助功能
    private static void auto_acess() {
        ThreadSleepTime.sleep0D5();

        ApiUtil.perforGlobalClick(482, 1306);

        ThreadSleepTime.sleep1D5();
        ApiUtil.perforGlobalSwipe(493, 1650, 477, 859);

        ApiUtil.perforGlobalClick(573, 1516);

        ThreadSleepTime.sleep0D5();

        ApiUtil.perforGlobalClick(960, 337);

        ThreadSleepTime.sleep0D5();

        ApiUtil.perforGlobalClick(802, 2095);

        ThreadSleepTime.sleep0D5();

        ApiUtil.execRootCmdSilent(" input keyevent 4 ");

        ThreadSleepTime.sleep0D5();

        ApiUtil.execRootCmdSilent(" input keyevent 4 ");

        ThreadSleepTime.sleep0D5();

        ApiUtil.execRootCmdSilent(" input keyevent 4 ");
    }
}
