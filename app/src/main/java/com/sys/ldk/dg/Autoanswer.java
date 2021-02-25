package com.sys.ldk.dg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.FloatingWindow;
import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;
import com.sys.ldk.serverset.MyNotificationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.sys.ldk.serverset.MainService.notification;
import static com.sys.ldk.dg.ReturnType.CLIK;
import static com.sys.ldk.dg.ReturnType.FAILURE;
import static com.sys.ldk.dg.ReturnType.OVER;
import static com.sys.ldk.dg.ReturnType.SUCCESS;

public class Autoanswer {
    private final static String mei_ri = "每日答题";
    private final static String mei_zhou = "每周答题";
    private final static String zhuan_xiang = "专项答题";
    private final static int chen_gong = 1;
    private final static int shi_bai = -1;

    /**
     * 进入积分页面
     *
     * @return
     */
    public static boolean doactivity() {

        if (!into_ji_fen_page()) {
            return false;
        }

//        自动答题
        if (ThreadSleepTime.sleep2()) {
            return false;
        }
        return !auto_answer();
    }

    public static boolean into_ji_fen_page() {
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
        return UiApi.jumpToNeedPage(new String[]{temp1});
    }

    public static boolean auto_answer() {
//      向上滚动
        AcessibilityApi.ScrollNode(Objects.requireNonNull(AcessibilityApi.findViewByCls("android.webkit.WebView")).get(0));

        if (ThreadSleepTime.sleep3()) {
            return false;
        }

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
                    LogUtil.D(mei_ri + "开始");
                    ThreadSleepTime.sleep3();
                    AcessibilityApi.performViewClick(hashMap1.get(e.getKey()));
                    if (ThreadSleepTime.sleep2()) {
                        return false;
                    }
                    LogUtil.D(mei_ri);
                    if (!startanswer()) {
                        return false;
                    }
                    break;
                case mei_zhou:
                    LogUtil.D(mei_zhou + "开始");
                    ThreadSleepTime.sleep3();
                    AcessibilityApi.performViewClick(hashMap1.get(e.getKey()));
                    if (ThreadSleepTime.sleep3()) {
                        return false;
                    }
                    switch (find_wei_zuo_da("未作答")) {
                        case chen_gong:
                            if (ThreadSleepTime.sleep2()) {
                                return false;
                            }
                            if (!startanswer()) {
                                return false;
                            }
                            break;
                        case shi_bai:
                            AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
                            break;
                    }
                    break;
                case zhuan_xiang:
                    LogUtil.D(zhuan_xiang + "开始");
                    ThreadSleepTime.sleep3();
                    AcessibilityApi.performViewClick(hashMap1.get(e.getKey()));
                    if (ThreadSleepTime.sleep2()) {
                        return false;
                    }
                    switch (find_wei_zuo_da("开始答题")) {
                        case chen_gong:
                            if (ThreadSleepTime.sleep2()) {
                                return false;
                            }
                            if (!startanswer()) {
                                return false;
                            }
                            break;
                        case shi_bai:
                            AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
                            break;
                    }
                    break;
            }
        }
//        AcessibilityApi.performViewClick(hashMap1.get(strings));
        if (ThreadSleepTime.sleep1()) {
            return false;
        }


        fen_shu();
        return true;
    }

    public static void fen_shu() {
        AccessibilityNodeInfo accessibilityNodeInfo = User.get_text_after_info("积分规则", 1);
        if (accessibilityNodeInfo != null) {
            LogUtil.D("" + accessibilityNodeInfo.getText());
        }
        assert accessibilityNodeInfo != null;
//        回UI线程
        FloatingWindow.runimage.post(() -> {
            MyNotificationType.message1 = accessibilityNodeInfo.getText() + "";
            notification();
        });
    }

    /**
     * 点击未作答
     *
     * @param string
     * @return
     */
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
        LogUtil.D("" + accessibilityNodeInfoList1.size());
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
                    if (!XxqgFuntion.back()) {
                        return false;
                    }
                    return false;
                case OVER:
                    LogUtil.D("答题结束");
                    return true;
            }
//            下一题等待时间
            if (ThreadSleepTime.sleep0D5()) {
                return false;
            }
        } while (true);
    }

}
