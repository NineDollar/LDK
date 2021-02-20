package com.sys.ldk.xxqg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sys.ldk.accessibility.api.User.Threadsleep;
import static com.sys.ldk.xxqg.ReturnType.CLIK;
import static com.sys.ldk.xxqg.ReturnType.FAILURE;
import static com.sys.ldk.xxqg.ReturnType.OVER;
import static com.sys.ldk.xxqg.ReturnType.SUCCESS;

public class Autoanswer {
    private final static String mei_ri = "每日答题";
    private final static String mei_zhou = "每周答题";
    private final static String zhuan_xiang = "专项答题";
    private final static int chen_gong = 1;
    private final static int shi_bai = -1;

    public static boolean doactivity() {
//进入积分页面
        String temp1 = "{"
                + "'maxWClickMSec':1000,"
                + "'click':{'id':'cn.xuexi.android:id/comm_head_xuexi_score'},"
                + "'page':"
                + "{"
                + "'maxMustMills':5000,"
                + "'maxOptionMills':5000,"
                + "'must':{'text':[],'id':[],'desc':[]},"
                + "'option':{'text':[],'id':[],'desc':[]}"
                + "}"
                + "}";
        if (!UiApi.jumpToNeedPage(new String[]{temp1})) {
            return false;
        }

//        自动答题
        Threadsleep(2);
        if (auto_answer()) {
            return false;
        }

        return true;
    }

    public static boolean auto_answer() {
//      向上滚动
        AcessibilityApi.ScrollNode(AcessibilityApi.findViewByCls("android.webkit.WebView").get(0));

        Threadsleep(3);

        if (!into_da_ti()) {
            return false;
        }
        return true;
    }


    public static boolean into_da_ti() {
        String[] strings = {"每日答题", "去答题"};
        String[] strings1 = {"每周答题", "去答题"};
        String[] strings2 = {"专项答题", "去看看"};

        HashMap<String[], Integer> hashMap = new HashMap<>();
        hashMap.put(strings, 4);
        hashMap.put(strings1, 4);
        hashMap.put(strings2, 4);
        HashMap<String[], AccessibilityNodeInfo> hashMap1 = User.getallafterInfo(hashMap);

        for (HashMap.Entry e : hashMap1.entrySet()
        ) {
//            LogUtil.D("" + hashMap1.get(e.getKey()).getText()); //获取info
            String[] key = (String[]) e.getKey();
            switch (key[0]) {
                case mei_ri:
                    ThreadSleepTime.sleeploglog();
                    AcessibilityApi.performViewClick(hashMap1.get(e.getKey()));
                    ThreadSleepTime.sleeplog();
                    LogUtil.D(mei_ri);
                    if (!startanswer()) {
                        return false;
                    }
                    break;
                case mei_zhou:
                    ThreadSleepTime.sleeploglog();
                    AcessibilityApi.performViewClick(hashMap1.get(e.getKey()));
                    ThreadSleepTime.sleeplog();
                    LogUtil.D(mei_zhou);
                    switch (find_wei_zuo_da("未作答")) {
                        case chen_gong:
                            ThreadSleepTime.sleeplog();
                            if (!startanswer()) {
                                return false;
                            }
                        case shi_bai:
                            AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
                            break;
                    }
                    break;
                case zhuan_xiang:
                    ThreadSleepTime.sleeploglog();
                    AcessibilityApi.performViewClick(hashMap1.get(e.getKey()));
                    ThreadSleepTime.sleeplog();
                    LogUtil.D(zhuan_xiang);
                    switch (find_wei_zuo_da("开始答题")) {
                        case chen_gong:
                            ThreadSleepTime.sleeplog();
                            if (!startanswer()) {
                                return false;
                            }
                        case shi_bai:
                            AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
                            break;
                    }
                    break;
            }
        }
//        AcessibilityApi.performViewClick(hashMap1.get(strings));
        return true;
    }

    private static int find_wei_zuo_da(String string) {
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.getAllNode(null, null);
        List<AccessibilityNodeInfo> accessibilityNodeInfoList1 = new ArrayList<>();
        for (AccessibilityNodeInfo a : accessibilityNodeInfoList) {
            if (string.equals(a.getText() + "")) {
                accessibilityNodeInfoList1.add(a);
            }
        }
        if (accessibilityNodeInfoList1.isEmpty()) {
            LogUtil.W("没有未作答");
            return shi_bai;
        }
        LogUtil.V("" + accessibilityNodeInfoList1.size());
        AcessibilityApi.performViewClick(accessibilityNodeInfoList1.get(0));
        return chen_gong;
    }

    public static boolean startanswer() {
        do {
            int retype = IntoAnswer.intoanswer();
            switch (retype) {
                case CLIK:
                case SUCCESS:
                    LogUtil.D("一次答题成功");
                    break;
                case FAILURE:
                    LogUtil.E("答题失败结束程序");
                    back();
                    return false;
                case OVER:
                    LogUtil.D("答题结束");
                    return true;
            }
//            下一题等待时间
            ThreadSleepTime.sleep();
        } while (true);
    }

    public static boolean back() {
//        返回两次
        int count = 2;
        do {
            if (UiApi.findNodeByTextWithTimeOut(2000,"学习积分") != null) {
                LogUtil.D("回到学习积分页面");
                return true;
            } else if (count-- <= 0) {
                return false;
            }
            AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
        } while (UiApi.findNodeByTextWithTimeOut(2000,"学习积分") == null);
        return true;
    }

}
