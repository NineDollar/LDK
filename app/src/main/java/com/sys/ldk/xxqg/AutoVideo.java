package com.sys.ldk.xxqg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.UiApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sys.ldk.accessibility.api.User.Threadsleep;

public class AutoVideo {
    //    最大观看时间5分钟
    private static int MAXshijina = 10 * 60;
//    翻页次数
    private static int times = 2;
    private static int count ;

    public static boolean auto_video() {
        count = 2;
        AcessibilityApi.clickTextViewByText("电视台");
        User.Threadsleep(2);
        AcessibilityApi.clickTextViewByText("第一频道");
        User.Threadsleep(2);
        startvido(di_yi_ping_dao());
        User.Threadsleep(2);
//        联播频道
        /*AcessibilityApi.clickTextViewByText("联播频道");
        User.Threadsleep(2);
        startvido(XxqgFuntion.listinfo("cn.xuexi.android:id/general_card_title_id","中央广播电视总台"));*/

        /*while (count < times) {
            count++;
            LogUtil.D("观看第"+count+"次");
            User.Threadsleep(2);
            List<AccessibilityNodeInfo> List = User.getScrollNodeInfo();
            AcessibilityApi.ScrollNode(List.get(4));
            User.Threadsleep(3);
            startvido();
        }*/
        return true;
    }

    public static boolean startvido(List<AccessibilityNodeInfo>  accessibilityNodeInfos) {
        if(accessibilityNodeInfos.isEmpty()){
            return false;
        }
        for (AccessibilityNodeInfo a: accessibilityNodeInfos
             ) {
            LogUtil.V("time: " + a.getText());
        }
        for (AccessibilityNodeInfo a : accessibilityNodeInfos
        ) {
            ThreadSleepTime.sleeplog();
            LogUtil.V("text: " + a.getText());
            AcessibilityApi.performViewClick(a);
            if(UiApi.findNodeByTextWithTimeOut(2000, "欢迎发表你的观点")==null){
                LogUtil.W("观看失败");
                return false;
            }else {
                LogUtil.I("正在观看");
            }
//            流量播放
            AcessibilityApi.performViewClick(UiApi.findNodeByTextWithTimeOut(2000, "继续播放"));
            ThreadSleepTime.sleeplog();
//                判断观看是否结束
            int shijian = 0;
            while (true) {
                shijian += 1;
                if (watch_end() || shijian >= MAXshijina) {
                    LogUtil.V("结束");
                    AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
                    break;
                }
            }
        }
        LogUtil.D("视频观看完毕");
        return true;
    }

    //    观看是否结束
    private static boolean watch_end() {
        if (AcessibilityApi.findViewByText("重新播放") != null) {
            LogUtil.E("观看完毕");
            return true;
        } else {
            Threadsleep(1);
        }
        return false;
    }

    public static Boolean Data(String str) {
        String regex = "[0-9]{2}:[0-9]{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(str);
        boolean dateFlag = m.matches();
        if (!dateFlag) {
            return false;
        }
        return true;
    }

    /**
     * 获取第一视频里的info
     * @return
     */
    public static List<AccessibilityNodeInfo> di_yi_ping_dao() {
        int sum = 0;
        int j = 0;
        int i = 0;
        List<AccessibilityNodeInfo> timeinfo = new ArrayList<>();
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = AcessibilityApi.getAllNode(null, null);
        sum = accessibilityNodeInfoList.size() - 2;
        for (AccessibilityNodeInfo a : accessibilityNodeInfoList
        ) {
            if (j < sum) {
                i = j;
            }
            if (AutoVideo.Data(a.getText() + "")) {
                /*if ("中央广播电视总台".equals(accessibilityNodeInfoList.get(i - 2).getText() + "")) {
                    timeinfo.add(accessibilityNodeInfoList.get(i - 2));
                } else if ("中央广播电视总台".equals(accessibilityNodeInfoList.get(i + 2).getText() + "")) {
                    timeinfo.add(accessibilityNodeInfoList.get(i + 2));
                }*/
                if("中央广播电视总台".equals(accessibilityNodeInfoList.get(i - 2).getText() + "")||"中央广播电视总台".equals(accessibilityNodeInfoList.get(i + 2).getText() + "")){
                    timeinfo.add(a);
                }
            }
            i = 0;
            j++;
        }
        if (timeinfo.isEmpty()) {
            return null;
        }
        LogUtil.V("第一视频总数：" + timeinfo.size());
        return timeinfo;
    }
}
