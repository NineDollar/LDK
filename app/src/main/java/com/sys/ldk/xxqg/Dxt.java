package com.sys.ldk.xxqg;

import android.view.accessibility.AccessibilityNodeInfo;

import com.sys.ldk.ThreadSleepTime;
import com.sys.ldk.accessibility.api.AcessibilityApi;
import com.sys.ldk.accessibility.api.User;
import com.sys.ldk.accessibility.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sys.ldk.xxqg.ReturnType.FAILURE;
import static com.sys.ldk.xxqg.ReturnType.SUCCESS;
import static com.sys.ldk.xxqg.ReturnType.my_stop;

public class Dxt {
    public static int dxt(String questiontype) {

//        获取题目
        List<String> alltextlistbefore = User.getallInfottext(false);
        List<String> timu = IntoAnswer.gettimu(questiontype);
        for (String s : timu
        ) {
            LogUtil.D("题目：" + s + ", length：" + s.length());
        }

//       拿到答案选项
        HashMap<String, AccessibilityNodeInfo> hashMaptextInfo = new HashMap<>();
        HashMap<String, AccessibilityNodeInfo> hashMapabcdInfo = new HashMap<>();
        List<String> listabcdtext = new ArrayList<>();
        IntoAnswer.abcdInfoandtext(hashMaptextInfo, hashMapabcdInfo, listabcdtext);

//      拿到提示
        User.clik_text_Info("查看提示");

        if (ThreadSleepTime.sleep0D5()) {
            return my_stop;
        }
        List<String> alltextlistafter = User.getallInfottext(false);
        String tishistr = alltextlistafter.get(alltextlistbefore.size() - 3);
        if (tishistr.isEmpty()) {
            tishistr = alltextlistafter.get(alltextlistbefore.size() - 2);
        }
        LogUtil.D("提示： " + tishistr);

        if (ThreadSleepTime.sleep0D5()) {
            return my_stop;
        }
        AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);

        if (tishistr.contains("故本题选")) {
            if(funtion3(tishistr, hashMapabcdInfo)==my_stop){
                return my_stop;
            }
            return SUCCESS;
        }
//        正确/错误
        if (listabcdtext.get(0).equals("正确") || listabcdtext.get(0).equals("错误")) {
            return funtion2(timu, tishistr, hashMaptextInfo);
        }
//       提示和答案匹配
        if (ThreadSleepTime.sleep0D5()) {
            return my_stop;
        }
        String dn = funtion1(listabcdtext, tishistr);
        if (dn != null) {
            AcessibilityApi.performViewClick(hashMaptextInfo.get(dn));

            if (ThreadSleepTime.sleep0D2()) {
                return my_stop;
            }
            return IntoAnswer.cliknext(null);
        }
//        跳过
        return funtion4(hashMapabcdInfo);
    }

    private static int funtion4(HashMap<String, AccessibilityNodeInfo> hashMapabcdInfo) {
        LogUtil.E("未找到答案，跳过");
        AcessibilityApi.performViewClick(hashMapabcdInfo.get("A."));
        if (ThreadSleepTime.sleep0D2()) {
            return my_stop;
        }
        return IntoAnswer.cliknext(null);
    }

    private static int funtion3(String tishistr, HashMap<String, AccessibilityNodeInfo> hashMap) {
        String dn = tishistr.substring(tishistr.indexOf("故本题选") + 4, tishistr.indexOf("故本题选") + 5);
        LogUtil.D("故本题选" + dn);
        if (ThreadSleepTime.sleep1()) {
            return my_stop;
        }
        AcessibilityApi.performViewClick(hashMap.get(dn + "."));
        if (ThreadSleepTime.sleep0D2()) {
            return my_stop;
        }
        return IntoAnswer.cliknext(null);
    }

    private static int funtion2(List<String> timu, String tishistr, HashMap<String, AccessibilityNodeInfo> hashMaptextInfo) {
        for (String s : timu
        ) {
            if (tishistr.indexOf(s) != -1) {
                User.clik_text_Info("正确");

                if (ThreadSleepTime.sleep0D2()) {
                    return my_stop;
                }
                return IntoAnswer.cliknext(null);
            }
        }

        List<String> tj = new ArrayList<>();
        tj.add("高于");
        tj.add("低于");
        tj.add("一样");
        HashMap<String, Integer> hashMap1 = new HashMap<>();
        for (String s1 : tj
        ) {
            hashMap1.put(s1, tishistr.indexOf(s1) * tishistr.indexOf(s1));
            if (hashMap1.get(s1) > 0) {
                LogUtil.D("找到相同关键字:" + s1);
                LogUtil.D("应该选正确");
                AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);

                if (ThreadSleepTime.sleep0D2()) {
                    return my_stop;
                }
                AcessibilityApi.performViewClick(hashMaptextInfo.get("正确"));

                if (ThreadSleepTime.sleep0D2()) {
                    return my_stop;
                }
                return IntoAnswer.cliknext(null);
            } else if (hashMap1.get(s1) < 0) {
                LogUtil.D("找到不同关键字:" + s1);
                LogUtil.D("应该选不正确");
                AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);

                if (ThreadSleepTime.sleep0D2()) {
                    return my_stop;
                }
                AcessibilityApi.performViewClick(hashMaptextInfo.get("错误"));

                if (ThreadSleepTime.sleep0D2()) {
                    return my_stop;
                }
                return IntoAnswer.cliknext(null);
            } else if (hashMap1.get(s1) == 1) {
                LogUtil.D("都未找到:" + s1);
                LogUtil.D("-----");
                return FAILURE;
            }
        }
        return FAILURE;
    }

    private static String funtion1(List<String> listabcdtext, String tishistr) {
        for (String s : listabcdtext
        ) {
            if (tishistr.indexOf(s) != -1) {
                LogUtil.D("找到相关： " + s);
                return s;
            }
        }
        LogUtil.D("答案与提示没有相关匹配");
        return null;
    }

    //    分解答案关键字，再提示里面模糊查找
    private static boolean mohufind(String timu) {
        //        判断横线个数
        int linenumber = IntoAnswer.countStr(timu, " ");
//        横线开始index
        int beginindex = timu.indexOf(" ");
        int endindex = 0;
        if (beginindex > 0) {
            LogUtil.D("找到横线");
            int beginindexnew = beginindex;
            while (true) {
                char ch = timu.charAt(beginindexnew);
                String chstr = ch + "";
                if (!chstr.equals(" ")) {
                    endindex = beginindexnew;
                    LogUtil.D("横线后： " + timu.substring(endindex, endindex + 3));
                    break;
                }
                beginindexnew += 1;
            }
        } else {
            LogUtil.E("未找到横线");
            return false;
        }

        LogUtil.D("beginindex: " + beginindex);
        LogUtil.D("endindex: " + endindex);

        String wtbegin;
        String wtend;
        if (beginindex < 3) {
            wtbegin = timu.substring(0, beginindex);
            LogUtil.D("wtbegin: " + wtbegin);
        } else {
            wtbegin = timu.substring(beginindex - 3, beginindex);
            LogUtil.D("wtbegin: " + wtbegin);
        }
        if (timu.length() - beginindex < 3) {
            wtend = timu.substring(beginindex + 1);
            LogUtil.D("wtend: " + wtend);
        } else {
            wtend = timu.substring(beginindex + 1, beginindex + 4);
            LogUtil.D("wtend: " + wtend);
        }
        return true;
    }
}
