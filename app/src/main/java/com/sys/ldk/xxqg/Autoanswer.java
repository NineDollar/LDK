package com.sys.ldk.xxqg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.ApiUtil;
import com.sys.ldk.accessibility.util.LogUtil;

import java.security.acl.LastOwnerException;
import java.util.HashMap;
import java.util.List;

import static com.sys.ldk.accessibility.api.User.Threadsleep;
import static com.sys.ldk.xxqg.ReturnType.CLIK;
import static com.sys.ldk.xxqg.ReturnType.FAILURE;
import static com.sys.ldk.xxqg.ReturnType.OVER;
import static com.sys.ldk.xxqg.ReturnType.SUCCESS;

public class Autoanswer {

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

//      每日答题
        Threadsleep(3);
        LogUtil.I("每日答题");
        User.clik_Info_key("每日答题", "去答题");
        Threadsleep(3);
        if (!startanswer()) {
            return false;
        }


//      每周答题
        Threadsleep(3);
        LogUtil.I("每周答题");
        User.clik_Info_key("每周答题", "去答题");
        User.Threadsleep(3);
        if (!weizuodaAndkaishidati("未作答")) {
            return false;
        }
        User.Threadsleep500();
        UiApi.back();

//        专项答题
        Threadsleep(3);
        LogUtil.I("专项答题");
        User.clik_Info_key("专项答题", "去看看");
        Threadsleep(3);
        if (!weizuodaAndkaishidati("开始答题")) {
            return false;
        }
        return true;
    }

    public static boolean weizuodaAndkaishidati(String weidatiORkaishidati) {
        boolean flag = false;
        int n = 0;
        HashMap<String, AccessibilityNodeInfo> hashMap = findweidatiORkaishidati(weidatiORkaishidati);
        do {
            if (n >= 1) {
                if (n > 20) {
                    break;
                }
                LogUtil.D("ScrollableInfo: " + n);
                if (hashMap.containsKey("ScrollableInfo")) {
                    AcessibilityApi.ScrollNode(hashMap.get("ScrollableInfo"));
                    User.Threadsleep500();
                } else {
                    LogUtil.E("weidatiORkaishidatimap: " + "未找到");
                }
                hashMap.clear();
                hashMap = findweidatiORkaishidati(weidatiORkaishidati);
                User.Threadsleep500();
            }
            flag = hashMap.containsKey("找到");
            n += 1;
        } while (!flag);
        if (hashMap.containsKey(weidatiORkaishidati)) {
            AcessibilityApi.performViewClick(hashMap.get(weidatiORkaishidati));
//            User.Threadsleep(2);
            ThreadSleepTime.threadsleepshort();
//                    自动答题
            if (startanswer()) {
                LogUtil.D("答题完成，开始下一项");
                return true;
            } else {
                LogUtil.D("答题失败");
                return false;
            }
        } else {
            LogUtil.E("进入答题失败");
            return false;
        }
    }

    //    进入未答题页面
    private static boolean meizhoudati() {
        boolean flag = false;
        int n = 0;
        HashMap<String, AccessibilityNodeInfo> hashMap = findweizuoda();
        do {
            if (n >= 1) {
                if (n > 10) {
                    LogUtil.E("未作答: 未找到");
                    break;
                }
                LogUtil.D("ScrollableInfo: " + n);
                if (hashMap.containsKey("ScrollableInfo")) {
                    AcessibilityApi.ScrollNode(hashMap.get("ScrollableInfo"));
                } else {
                    LogUtil.E("ScrollableInfo2: " + "未找到");
                }
                hashMap.clear();
                hashMap = findweizuoda();
                User.Threadsleep500();
            }
            flag = hashMap.containsKey("找到");
            n += 1;
        } while (!flag);
        if (hashMap.containsKey("未作答")) {
            AcessibilityApi.performViewClick(hashMap.get("未作答"));
        }

        return true;
    }

    //    寻找未答题
    private static HashMap<String, AccessibilityNodeInfo> findweizuoda() {
        HashMap<String, AccessibilityNodeInfo> hashMap = new HashMap<>();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.getAllNode(null, null);
        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            String text = a.getText() + "";
            if (text.equals("未作答")) {
                hashMap.put(text, a);
                hashMap.put("找到", null);
                return hashMap;
            }
            if (a.isScrollable()) {
                hashMap.put("ScrollableInfo", a);
                return hashMap;
            }
        }
        return null;
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
                    return false;
                case OVER:
                    LogUtil.D("答题结束");
                    return true;
            }
//            下一题等待时间
            ThreadSleepTime.threadsleep();
        } while (true);
    }

    public static HashMap<String, AccessibilityNodeInfo> findweidatiORkaishidati(String weidatiORkaishidati) {
        HashMap<String, AccessibilityNodeInfo> hashMap = new HashMap<>();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.getAllNode(null, null);
        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            String text = a.getText() + "";
            if (text.equals(weidatiORkaishidati)) {
                hashMap.put(text, a);
                hashMap.put("找到", null);
                return hashMap;
            }
            if (a.isScrollable()) {
                hashMap.put("ScrollableInfo", a);
                return hashMap;
            }
        }
        return null;
    }


}
