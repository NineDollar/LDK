package com.sys.ldk.xxqg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.DG_Thread;
import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.List;

public class AutoRead {
    private static final int read_time = 1 * 5;
    private static int reading_times = 10;

    public static boolean auto_read() {
        LogUtil.D("要闻---开始阅读");
        return yao_wen("要闻");
    }

    public static boolean yao_wen(String tuijianORyaowen) {
//        观看次数
        int q = 1;
        AcessibilityApi.clickTextViewByText(tuijianORyaowen);
        if (ThreadSleepTime.sleep2()) {
            return false;
        }
        AcessibilityApi.clickTextViewByText(tuijianORyaowen);
        if (ThreadSleepTime.sleep2()) {
            return false;
        }
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.findViewByid_list("cn.xuexi.android:id/general_card_title_id");
        assert accessibilityNodeInfoList != null;
        int m = accessibilityNodeInfoList.size();
        LogUtil.I("size： " + m);
        for (AccessibilityNodeInfo a:accessibilityNodeInfoList
             ) {
            LogUtil.V("text: " + a.getText());
        }
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
//            如果页面有视频，则点击页面视频button
            video();
//            阅读时间
            LogUtil.W("阅读：" + read_time + "秒");
            if (ThreadSleepTime.sleep(read_time * 1000)) {
                return false;
            }

            if (reading_times > q && q >= 0) {
                if (ThreadSleepTime.sleep1()) {
            return false;
        }
                if (!shoucangAndfenxiang()) {
                    LogUtil.E("未找到发表你的观点");
                } else {
                    q -= 1;
                }
            } else {
                LogUtil.D("收藏任务完成");
            }
            if (ThreadSleepTime.sleep0D5()) {
                return false;
            }
            AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
            reading_times -= 1;
            if (ThreadSleepTime.sleep1()) {
                return false;
            }
            if (reading_times <= 0) {
                LogUtil.D("观看结束");
                return true;
            }
        }
        LogUtil.D("观看出错");
        return false;
    }

    private static void video() {
        List<AccessibilityNodeInfo> accessibilityNodeInfos = AcessibilityApi.findViewByCls("android.widget.Button");
        assert accessibilityNodeInfos != null;
        if(accessibilityNodeInfos.size()>0){
            AcessibilityApi.performViewClick(accessibilityNodeInfos.get(0));
        }else {
            LogUtil.D("页面没有视频");
        }
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
            if (ThreadSleepTime.sleep1()) {
            return false;
        }
            AcessibilityApi.performViewClick(allInfo.get(size - 2));
            if (ThreadSleepTime.sleep1()) {
            return false;
        }
        } catch (Exception e) {
            LogUtil.E("出错： " + e);
        }
        AcessibilityApi.clickTextViewByText("分享到学习强国");
        if (ThreadSleepTime.sleep1()) {
            return false;
        }
        AcessibilityApi.clickTextViewByText("余波...");
        if (ThreadSleepTime.sleep1()) {
            return false;
        }
        AcessibilityApi.clickTextViewByText("取消");
        if (ThreadSleepTime.sleep1()) {
            return false;
        }
        AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
        return true;
    }
}


