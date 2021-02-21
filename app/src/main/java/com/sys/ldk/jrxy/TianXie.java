package com.sys.ldk.jrxy;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TianXie {

    public static boolean tianxie() {
        if (ThreadSleepTime.sleep1()) {
            return false;
        }
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = new ArrayList<>();
        HashMap<String[], Integer> hashMap = new HashMap<>();
        HashMap<String[], AccessibilityNodeInfo> afterInfomap = new HashMap<String[], AccessibilityNodeInfo>();
        if (!User.clik_text_Info("<37.3℃")) {
            return false;
        }
        if(ThreadSleepTime.sleep0D5()){
        return true;
    }

        String wd = wendu() + "";
        if (User.getEditableInfo().size() == 1) {
            AcessibilityApi.inputTextByNode(User.getEditableInfo().get(0), wd);
        } else {
            LogUtil.E("未找到输入框");
            return false;
        }

        if(ThreadSleepTime.sleep0D5()){
        return true;
    }
        List<AccessibilityNodeInfo> ScrollNodeInfoList = User.getScrollNodeInfo();
        AcessibilityApi.ScrollNode(ScrollNodeInfoList.get(0));

        hashMap.clear();
        afterInfomap.clear();
        String[] strings1 = new String[]{"3.当前身体状况 （必填）", "正常"};
        hashMap.put(strings1, 1);

        String[] strings2 = new String[]{"4.近14天是否去过疫情中、高风险区 （必填）", "否"};
        hashMap.put(strings2, 2);

        String[] strings3 = new String[]{"5.近14天是否接触过疑似或确诊病例 （必填）", "否"};
        hashMap.put(strings3, 2);

        String[] strings4 = new String[]{"6.本人是否承诺以上信息如实填写，准确无误 （必填）", "是"};
        hashMap.put(strings4, 1);

        String[] strings5 = new String[]{"确认已认真查看，且填写信息无误", "确认已认真查看，且填写信息无误"};
        hashMap.put(strings5, 0);

//      记录次数，判断是否点击完整
        int times = 5;
        afterInfomap = User.getallafterInfo(hashMap);
        for (HashMap.Entry e : afterInfomap.entrySet()
        ) {
            if(AcessibilityApi.performViewClick(afterInfomap.get(e.getKey()))){
                times -= 1;
                if(times == 3){
                    AcessibilityApi.ScrollNode(ScrollNodeInfoList.get(0));
                }
                if(ThreadSleepTime.sleep0D5()){
        return true;
    }
            }
        }
        if (times!=0) {
            LogUtil.E("点击不完整");
            return false;
        }

        if(ThreadSleepTime.sleep0D5()){
        return true;
    }
        if(!User.clik_text_Info("提交给辅导员")){
            LogUtil.E("提交辅导员失败");
            return false;
        }

        if(ThreadSleepTime.sleep0D5()){
        return true;
    }
        if(!User.clik_text_Info("提交")){
            LogUtil.E("提交失败");
            return false;
        }

        return true;
    }

    private static double wendu() {
        double[] strings = new double[]{36.2, 36.3, 36.4, 36.5, 36.6};
        int second = getsecond(new Date());
        int index = second % 10;
        if (index >= 5) {
            index -= 5;
        }
        return strings[index];
    }

    public static int getsecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }
}

