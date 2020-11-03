package com.sys.ldk.xxqg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.security.acl.LastOwnerException;
import java.util.List;

public class AutoRead {
    private static final int read_time = 100;
    private static int reading_times = 10;

    public static boolean auto_read() {
        LogUtil.D("要闻---开始阅读");
        tuijianORyao("要闻");
        return true;
    }

    public static boolean tuijianORyao(String tuijianORyaowen) {
//        观看次数
        int q = 1;
        AcessibilityApi.clickTextViewByText(tuijianORyaowen);
        User.Threadsleep(2);
        AcessibilityApi.clickTextViewByText(tuijianORyaowen);
        User.Threadsleep(2);
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.findViewByid_list("cn.xuexi.android:id/general_card_title_id");
        int m = accessibilityNodeInfoList.size();
        LogUtil.E("size： " + m);
        if (m < reading_times) {
            reading_times = m;
        }
        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            LogUtil.I("观看: " + reading_times);
            LogUtil.D(a.getText() + "");
            if (!AcessibilityApi.performViewClick(a)) {
                return false;
            }
//            阅读时间
            User.Threadsleep(read_time);
            if (reading_times > q && q >= 0) {
                User.Threadsleep(1);
                if (!shoucangAndfenxiang()) {
                    LogUtil.E("未找到发表你的观点");
                } else {
                    q -= 1;
                }
            }else{
                LogUtil.D("收藏任务完成");
            }
            User.Threadsleep500();
            AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);

            reading_times -= 1;

            User.Threadsleep(1);
            if (reading_times <= 0) {
                LogUtil.D("观看结束");
                return true;
            }
        }
        LogUtil.D("观看出错");
        return false;
    }

    //  收藏和分享
    private static boolean shoucangAndfenxiang() {
        if (!User.findtext("欢迎发表你的观点")) {
            return false;
        }
        LogUtil.D("开始收藏和分享");
        List<AccessibilityNodeInfo> allInfo = AcessibilityApi.getAllNode(null, null);
        int size = allInfo.size();
        LogUtil.E("sixe: " + size);
        if (allInfo.isEmpty()) {
            return false;
        }
        try {
            AcessibilityApi.performViewClick(allInfo.get(size - 1));
            User.Threadsleep(1);
            AcessibilityApi.performViewClick(allInfo.get(size - 2));
            User.Threadsleep(1);
        } catch (Exception e) {
            LogUtil.E("出错： " + e);
        }
        AcessibilityApi.clickTextViewByText("分享到学习强国");
        User.Threadsleep(1);
        AcessibilityApi.clickTextViewByText("余波...");
        User.Threadsleep(1);
        AcessibilityApi.clickTextViewByText("取消");
        User.Threadsleep(1);
        AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
        return true;
    }
}


